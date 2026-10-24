package org.occideas.email.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.commons.lang3.StringEscapeUtils;
import org.occideas.vo.EmailReportVO;
import org.occideas.vo.IndividualFindingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Renders the individual-facing exposure report as a small, dedicated HTML/CSS document
 * (block/inline-block layout only - openhtmltopdf does not support flexbox, grid, sticky
 * positioning or box-shadow) and converts it directly to PDF. This deliberately avoids the
 * live app's stylesheet and any DOM-screenshot approach (jsPDF/html2canvas), which proved
 * unreliable for this content.
 *
 * Mirrors the on-screen calibrated report (see individualReport.html): a verdict driven by
 * the highest level found (flagged/medium/low/clear), an "agents assessed" table, the findings at that level, and any
 * lower-level findings as "also identified". This is deliberately not the full technical breakdown
 * (all rule levels/conditions) - that remains available to the employer separately.
 */
@Service
public class ReportPdfServiceImpl implements ReportPdfService {

  private static final Logger logger = LoggerFactory.getLogger(ReportPdfServiceImpl.class);

  private static final String LOGO_RESOURCE_PATH = "/images/occideas-logo.png";
  private static final DateTimeFormatter REPORT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
  private static final String LOGO_DATA_URI = loadLogoDataUri();

  private static String loadLogoDataUri() {
    try (InputStream is = ReportPdfServiceImpl.class.getResourceAsStream(LOGO_RESOURCE_PATH)) {
      if (is == null) {
        logger.warn("Report logo resource not found at {}", LOGO_RESOURCE_PATH);
        return null;
      }
      byte[] bytes = is.readAllBytes();
      return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
    } catch (IOException e) {
      logger.warn("Failed to load report logo resource", e);
      return null;
    }
  }

  @Override
  public byte[] generatePdf(EmailReportVO reportData) {
    String html = buildHtml(reportData);
    try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.withHtmlContent(html, null);
      builder.toStream(os);
      builder.run();
      return os.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate report PDF", e);
    }
  }

  private String buildHtml(EmailReportVO reportData) {
    StringBuilder sb = new StringBuilder();
    sb.append("<html><head><meta charset=\"UTF-8\"/><style>").append(css()).append("</style></head><body>");

    appendHeader(sb, reportData);
    appendVerdict(sb, reportData);
    appendAssessedAgents(sb, reportData);

    List<IndividualFindingVO> highFindings = reportData.getHighFindings();
    if (highFindings != null && !highFindings.isEmpty()) {
      sb.append("<h1>What Was Found</h1>");
      sb.append("<p class=\"section-note\">These are the exposures your answers most strongly point to.</p>");
      for (IndividualFindingVO finding : highFindings) {
        sb.append("<div class=\"finding\">");
        sb.append("<span class=\"finding-name\">").append(escape(finding.getAgentName())).append("</span>");
        if (finding.getText() != null && !finding.getText().isEmpty()) {
          sb.append("<p class=\"finding-rationale\">").append(escape(finding.getText())).append("</p>");
        }
        if (finding.getReasons() != null && !finding.getReasons().isEmpty()) {
          sb.append("<p class=\"finding-reasons-label\">Why this was estimated as High:</p><ul class=\"finding-reasons\">");
          for (String reason : finding.getReasons()) {
            sb.append("<li>").append(escape(reason)).append("</li>");
          }
          sb.append("</ul>");
        }
        sb.append("<p class=\"finding-action\">Talk to your supervisor or health and safety representative about "
          + "this finding, and see the guidance from your local work health and safety regulator.</p>");
        sb.append("</div>");
      }
    }

    String verdictState = reportData.getVerdictState();
    List<IndividualFindingVO> otherFindings = reportData.getOtherFindings();
    List<IndividualFindingVO> mediumFindings = findingsAtLevel(otherFindings, "probMedium");
    List<IndividualFindingVO> lowFindings = findingsAtLevel(otherFindings, "probLow");
    boolean flagged = "flagged".equals(verdictState);
    boolean medium = "medium".equals(verdictState);

    if (flagged && (!mediumFindings.isEmpty() || !lowFindings.isEmpty())) {
      sb.append("<h1>Also Identified</h1>");
    }
    if (!mediumFindings.isEmpty()) {
      if (medium) {
        sb.append("<h1>What Was Found</h1>");
      } else {
        sb.append("<h2>Moderate</h2>");
      }
      sb.append("<p class=\"section-note\">Below is a list of recognised occupational hazards that your "
        + "answers suggest may be present in your workplace. You have some exposure to these, but it is likely "
        + "to be below the exposure limit. Please talk to your supervisor or health and safety representative "
        + "about these findings.</p>");
      appendNotedItems(sb, mediumFindings, "");
    }
    if (!lowFindings.isEmpty()) {
      if ("low".equals(verdictState)) {
        sb.append("<h1>What Was Found</h1>");
      } else if (medium) {
        sb.append("<h1>Also Identified</h1>");
      } else {
        sb.append("<h2>Low</h2>");
      }
      sb.append("<p class=\"section-note\">Below is a list of recognised occupational hazards that your "
        + "answers suggest may be present in your workplace. Your exposure to these is estimated to be well "
        + "below the exposure limit. Low exposure isn't the same as no exposure, so they are listed here.</p>");
      appendNotedItems(sb, lowFindings, "noted-item-low");
    }

    // Same notes as on screen (individualReport.html).
    if (reportData.isEmployerShared() && flagged) {
      sb.append("<div class=\"demo-note\"><strong>What happens next</strong><br/>As agreed when you started, "
        + "OccIDEAS will let your employer know about this result (see above). We don't collect your name or "
        + "contact details, and your employer won't be told which results are yours.</div>");
    } else if (reportData.isEmployerShared()) {
      sb.append("<div class=\"demo-note\">As agreed, your results will be included in OccIDEAS's report to your "
        + "employer. We don't collect your name or contact details, and your employer won't be told which "
        + "results are yours.</div>");
    }
    if (reportData.isTrial()) {
      sb.append("<div class=\"demo-note\">As this was a trial, your answers may be used to test and improve "
        + "OccIDEAS. They won't be shared with any employer.</div>");
    }

    if (reportData.getParticipantId() != null && !reportData.getParticipantId().isEmpty()) {
      sb.append("<p class=\"participant-id\">Your participant ID: <strong>")
        .append(escape(reportData.getParticipantId()))
        .append("</strong>. Keep this if you want to contact us about your data.</p>");
    }

    sb.append("<div class=\"disclaimer\"><strong>This is an estimate of your exposure at work, not medical "
      + "advice, a health assessment or a diagnosis.</strong> It's based only on what you told us about your job, and it can't account for "
      + "everything. If anything here concerns you, the best next step is talking to your health and safety "
      + "representative or supervisor.</div>");

    if (reportData.isShowDemoNote()) {
      sb.append("<div class=\"demo-note\"><strong>Want a more detailed assessment for your workplace?</strong> "
        + "OccIDEAS can assess exposure to a wider range of chemicals and other workplace hazards. Visit "
        + "<a href=\"https://www.occideas.com\">occideas.com</a> to find out more or book a demonstration.</div>");
    }

    sb.append("</body></html>");
    return sb.toString();
  }

  private List<IndividualFindingVO> findingsAtLevel(List<IndividualFindingVO> findings, String level) {
    if (findings == null) {
      return List.of();
    }
    return findings.stream().filter(finding -> level.equals(finding.getLevel())).collect(Collectors.toList());
  }

  private void appendNotedItems(StringBuilder sb, List<IndividualFindingVO> findings, String levelClass) {
    for (IndividualFindingVO finding : findings) {
      sb.append("<div class=\"noted-item ").append(levelClass).append("\">");
      sb.append("<span class=\"noted-name\">").append(escape(finding.getAgentName())).append("</span>");
      sb.append("<p class=\"noted-text\">").append(escape(finding.getText())).append("</p>");
      sb.append("</div>");
    }
  }

  // Every agent the assessment covers with its status - same as the on-screen "Agents assessed" table.
  private void appendAssessedAgents(StringBuilder sb, EmailReportVO reportData) {
    List<IndividualFindingVO> rows = reportData.getAssessedAgents();
    if (rows == null || rows.isEmpty()) {
      return;
    }
    sb.append("<h1>Agents Assessed</h1>");
    sb.append("<p class=\"section-note\">The hazards this assessment covers, and your estimated exposure to "
      + "each based on your answers.</p>");
    sb.append("<table class=\"assessed\"><thead><tr><th>Agent</th><th>Estimated exposure</th></tr></thead><tbody>");
    for (IndividualFindingVO row : rows) {
      sb.append("<tr><td class=\"assessed-name\">").append(escape(row.getAgentName())).append("</td>");
      sb.append("<td><span class=\"assessed-dot assessed-").append(escape(row.getLevel())).append("\"></span>")
        .append(escape(row.getText())).append("</td></tr>");
    }
    sb.append("</tbody></table>");
    if (reportData.isPartial()) {
      sb.append("<p class=\"section-note\">Some questions weren't answered, so \u201cNo exposure identified\u201d "
        + "may mean the questions about that agent weren't reached.</p>");
    }
    // Key for the status column - same as individualReport.html and occideas.com.
    sb.append("<h2>What the levels mean</h2><ul class=\"levels\">");
    appendLevel(sb, "probHigh", "High:", "your exposure is likely to be above the exposure limit.");
    appendLevel(sb, "probMedium", "Moderate:", "you have some exposure, but it is likely to be below the exposure limit.");
    appendLevel(sb, "probLow", "Low:", "your exposure is estimated to be well below the exposure limit. Low "
      + "exposure isn't the same as no exposure.");
    appendLevel(sb, "none", "No exposure identified:", "your answers didn't point to exposure to this hazard. "
      + "This is an estimate, not a guarantee.");
    // Only explained when a row has it - unknown-level rules are being removed, after which it never appears.
    if (rows.stream().anyMatch(row -> "unknown".equals(row.getLevel()))) {
      appendLevel(sb, "unknown", "Couldn't be estimated:", "your answers didn't give enough information to "
        + "estimate this exposure.");
    }
    sb.append("</ul>");
    sb.append("<p class=\"section-note\">These are estimates from your answers, compared with the exposure limit. "
      + "They are not measurements. For some hazards, including those that can cause cancer, it's best to keep "
      + "exposure as low as possible, even below the limit.</p>");
  }

  private void appendLevel(StringBuilder sb, String level, String label, String text) {
    sb.append("<li><span class=\"assessed-dot assessed-").append(level).append("\"></span><strong>")
      .append(label).append("</strong> ").append(text).append("</li>");
  }

  private void appendHeader(StringBuilder sb, EmailReportVO reportData) {
    sb.append("<div class=\"report-header\">");
    if (LOGO_DATA_URI != null) {
      sb.append("<img class=\"report-logo\" src=\"").append(LOGO_DATA_URI).append("\" alt=\"OccIDEAS\"/>");
    }
    sb.append("<span class=\"report-date\">");
    if (reportData.getParticipantId() != null && !reportData.getParticipantId().isEmpty()) {
      sb.append("Participant ID: <strong>").append(escape(reportData.getParticipantId())).append("</strong> &#183; ");
    }
    sb.append("Generated: ")
      .append(escape(LocalDateTime.now().format(REPORT_DATE_FORMAT)))
      .append("</span>");
    sb.append("</div>");
  }

  private void appendVerdict(StringBuilder sb, EmailReportVO reportData) {
    String state = reportData.getVerdictState();
    if (!"flagged".equals(state) && !"medium".equals(state) && !"low".equals(state)) {
      state = "clear";
    }
    // Same wording as the on-screen report (individualReport.html) - keep the two in sync.
    String title;
    String body;
    String recommendation = null;
    switch (state) {
      case "flagged":
        // Names the High agents - built by individualReportService.js, plain text, escaped here.
        title = reportData.getVerdictTitle() != null && !reportData.getVerdictTitle().isBlank()
          ? escape(reportData.getVerdictTitle())
          : "Your exposure to one or more hazards is likely to be above the exposure limit";
        body = "This is an estimate from your answers, not a measurement at your workplace.";
        if (reportData.isEmployerShared()) {
          // OccIDEAS reports to the employer, but never which results are whose - keep this true to that.
          recommendation = "<strong>Your employer has signed up to OccIDEAS and will be told about this result, "
            + "without being told it's yours, so they can work with you and your co-workers on the next steps to "
            + "reduce exposure.</strong> You can also talk to your supervisor or health and safety representative "
            + "about this finding, and see the guidance from your local work health and safety regulator. This "
            + "result does not mean that harm to your health has been found.";
        } else {
          recommendation = "<strong>Talk to your supervisor or health and safety representative about this "
            + "finding, and see the guidance from your local work health and safety regulator.</strong> This "
            + "result does not mean that harm to your health has been found.";
        }
        break;
      case "medium":
        title = "You have some exposure, but it is likely to be below the exposure limit";
        body = "Based on your answers, you have some exposure to the agents below, but it is likely to be below "
          + "the exposure limit. Because this is an estimate, it may still warrant attention. Please talk to your "
          + "supervisor or health and safety representative about these findings, and see the guidance from your "
          + "local work health and safety regulator.";
        break;
      case "low":
        title = "Your exposure is estimated to be well below the exposure limit";
        body = "Based on your answers, your exposure to the agents below is estimated to be well below the "
          + "exposure limit. Low exposure isn't the same as no exposure, so they are listed below.";
        break;
      default:
        title = "No exposures were identified from your answers";
        body = "Based on your answers, the assessment didn't identify exposure to any of the hazards it covers. "
          + "This is an estimate from what you told us, not a guarantee that your work involves no exposure.";
    }
    sb.append("<div class=\"verdict verdict-").append(state).append("\">");
    sb.append("<div class=\"verdict-eyebrow\">Result</div>");
    sb.append("<h2 class=\"verdict-title\">").append(title).append("</h2>");
    sb.append("<p class=\"verdict-body\">Based on your answers we have estimated which substances or agents "
      + "you might be exposed to and the level of that exposure in relation to the exposure limit. These "
      + "are estimated from what you told us, not measured directly.</p>");
    sb.append("<p class=\"verdict-body\">").append(body).append("</p>");
    if (recommendation != null) {
      sb.append("<p class=\"verdict-body\">").append(recommendation).append("</p>");
    }
    sb.append("</div>");
  }

  // openhtmltopdf parses the HTML as strict XML - escapeHtml4 would emit named entities
  // (e.g. &mdash; for an em dash) that aren't declared in XML and fail parsing. escapeXml11
  // only ever emits the predefined XML entities plus numeric character references.
  private String escape(String value) {
    return value == null ? "" : StringEscapeUtils.escapeXml11(value);
  }

  private String css() {
    return "body { font-family: Helvetica, Arial, sans-serif; font-size: 10pt; color: #222222; }"
      + ".report-header { border-bottom: 2px solid #3d9cbf; padding-bottom: 8px; margin-bottom: 4px; }"
      + ".report-logo { height: 40px; vertical-align: middle; }"
      + ".report-date { float: right; font-size: 9pt; color: #666666; line-height: 40px; }"
      + "h1 { font-size: 14pt; color: #3d9cbf; border-bottom: 1px solid #3d9cbf; padding-bottom: 4px; margin-top: 20px; }"
      + ".section-note { font-size: 8.5pt; color: #8b8c80; margin: 2px 0 10px; }"
      + ".verdict { border: 1px solid #b9d8c7; background-color: #e9f2ec; border-radius: 6px; padding: 10px 14px; }"
      + ".verdict-flagged { border-color: #e2c496; background-color: #f7ecdd; }"
      + ".verdict-eyebrow { font-size: 8pt; font-weight: bold; letter-spacing: 1px; color: #3f7a5c; }"
      + ".verdict-flagged .verdict-eyebrow { color: #a8651f; }"
      + ".verdict-medium { border-color: #e6d3a3; background-color: #faf4e2; }"
      + ".verdict-medium .verdict-eyebrow { color: #8a6d1f; }"
      + ".verdict-low { border-color: #d6d9bf; background-color: #f3f4ea; }"
      + ".verdict-low .verdict-eyebrow { color: #6f7340; }"
      + ".verdict-title { font-size: 13pt; margin: 4px 0 6px; }"
      + ".verdict-body { margin: 0 0 4px; font-size: 9.5pt; color: #5b5c53; }"
      + "h2 { font-size: 11pt; color: #222222; margin: 12px 0 2px; }"
      + ".finding { padding: 6px 0 6px 10px; border-top: 1px solid #edf2f5; border-left: 4px solid #b3452f; }"
      + ".finding-name { font-weight: bold; font-size: 10.5pt; }"
      + ".finding-rationale { margin: 3px 0 0; font-size: 9pt; color: #5b5c53; }"
      + ".finding-reasons-label { margin: 4px 0 0; font-size: 9pt; font-weight: bold; color: #222222; }"
      + ".finding-reasons { margin: 2px 0 0; padding-left: 16px; font-size: 9pt; color: #5b5c53; }"
      + ".finding-action { margin: 3px 0 0; font-size: 9pt; font-weight: bold; color: #2e7d95; }"
      + ".noted-item { padding: 6px 0 6px 10px; border-top: 1px solid #edf2f5; border-left: 4px solid #b8752b; }"
      + ".noted-item-low { border-left-color: #a68a2c; }"
      + ".noted-name { font-weight: bold; font-size: 10pt; }"
      + ".noted-text { margin: 3px 0 0; font-size: 9pt; color: #5b5c53; }"
      + ".noted-empty { font-size: 9pt; color: #8b8c80; }"
      + ".disclaimer { margin-top: 16px; padding: 8px 12px; background-color: #edf2f5; border-radius: 4px; font-size: 8pt; color: #5b5c53; }"
      + ".disclaimer strong { color: #2e7d95; }"
      + ".demo-note { margin-top: 8px; padding: 8px 12px; border: 1px solid #d5e3ea; border-radius: 4px; font-size: 8pt; color: #5b5c53; }"
      + ".demo-note strong { color: #2e7d95; }"
      + ".demo-note a { color: #3d9cbf; }"
      + ".participant-id { margin-top: 16px; font-size: 9pt; color: #5b5c53; }"
      + ".assessed { width: 100%; border-collapse: collapse; border: 1px solid #edf2f5; font-size: 9pt; }"
      + ".assessed th { padding: 4px 8px; background-color: #f5f8fa; text-align: left; font-size: 8.5pt; color: #5b5c53; }"
      + ".assessed td { padding: 4px 8px; border-top: 1px solid #edf2f5; vertical-align: top; }"
      + ".assessed-name { font-weight: bold; }"
      + ".assessed-dot { display: inline-block; width: 7px; height: 7px; margin-right: 5px; border-radius: 4px; }"
      + ".assessed-probHigh { background-color: #b3452f; }"
      + ".assessed-probMedium { background-color: #b8752b; }"
      + ".assessed-probLow { background-color: #a68a2c; }"
      + ".assessed-unknown { background-color: #b9c2c8; }"
      + ".assessed-none { background-color: #3f7a5c; }"
      + ".levels { margin: 4px 0 6px; padding-left: 0; list-style-type: none; font-size: 9pt; color: #5b5c53; }"
      + ".levels li { margin: 0 0 3px; }";
  }
}
