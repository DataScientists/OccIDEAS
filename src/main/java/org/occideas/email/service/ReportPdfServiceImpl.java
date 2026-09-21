package org.occideas.email.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.commons.lang3.StringEscapeUtils;
import org.occideas.vo.EmailReportVO;
import org.occideas.vo.IndividualFindingVO;
import org.occideas.vo.ReportTreeNodeVO;
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

/**
 * Renders the individual-facing exposure report as a small, dedicated HTML/CSS document
 * (block/inline-block layout only - openhtmltopdf does not support flexbox, grid, sticky
 * positioning or box-shadow) and converts it directly to PDF. This deliberately avoids the
 * live app's stylesheet and any DOM-screenshot approach (jsPDF/html2canvas), which proved
 * unreliable for this content.
 *
 * Mirrors the on-screen calibrated report (see interview.html's #si-report-content): a
 * binary verdict driven only by PROBABLE_HIGH findings, plus a lower-key section for
 * everything else that was noted. This is deliberately not the full technical breakdown
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

    appendHeader(sb);
    appendVerdict(sb, reportData);

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
        sb.append("<p class=\"finding-action\">Talk to your supervisor or OH&amp;S representative about this finding.</p>");
        sb.append("</div>");
      }
    }

    List<IndividualFindingVO> otherFindings = reportData.getOtherFindings();
    if (otherFindings != null && !otherFindings.isEmpty()) {
      sb.append("<h1>Other Things Worth Noting</h1>");
      sb.append("<p class=\"section-note\">Lower-confidence or less certain matches. Being within safe limits "
        + "doesn't mean there's nothing worth reducing further &#8212; these are worth being aware of, even "
        + "though we're not flagging them as a problem.</p>");
      for (IndividualFindingVO finding : otherFindings) {
        String levelClass = "probLow".equals(finding.getLevel()) ? "noted-item-low" : "";
        sb.append("<div class=\"noted-item ").append(levelClass).append("\">");
        sb.append("<span class=\"noted-name\">").append(escape(finding.getAgentName())).append("</span>");
        sb.append("<p class=\"noted-text\">").append(escape(finding.getText())).append("</p>");
        sb.append("</div>");
      }
    }

    sb.append("<div class=\"disclaimer\"><strong>This is an educational screening tool, not a medical "
      + "diagnosis.</strong> It's based only on what you told us about your job, and it can't account for "
      + "everything. If anything here concerns you, the best next step is talking to your OH&amp;S "
      + "representative, supervisor, or a doctor.</div>");

    if (reportData.getTree() != null && !reportData.getTree().isEmpty()) {
      sb.append("<h1>Interview Responses</h1>");
      appendTree(sb, reportData.getTree(), 0);
    }

    sb.append("</body></html>");
    return sb.toString();
  }

  private void appendHeader(StringBuilder sb) {
    sb.append("<div class=\"report-header\">");
    if (LOGO_DATA_URI != null) {
      sb.append("<img class=\"report-logo\" src=\"").append(LOGO_DATA_URI).append("\" alt=\"OccIDEAS\"/>");
    }
    sb.append("<span class=\"report-date\">Generated: ")
      .append(escape(LocalDateTime.now().format(REPORT_DATE_FORMAT)))
      .append("</span>");
    sb.append("</div>");
  }

  private void appendVerdict(StringBuilder sb, EmailReportVO reportData) {
    boolean flagged = "flagged".equals(reportData.getVerdictState());
    sb.append("<div class=\"verdict verdict-").append(flagged ? "flagged" : "clear").append("\">");
    sb.append("<div class=\"verdict-eyebrow\">Result</div>");
    if (flagged) {
      sb.append("<h2 class=\"verdict-title\">Your answers suggest an exposure above safe limits</h2>");
      sb.append("<p class=\"verdict-body\">Based on your answers, your exposure is estimated to be above "
        + "the levels occupational health and safety standards treat as safe. This is estimated from what "
        + "you told us, not measured directly. Details below.</p>");
    } else {
      sb.append("<h2 class=\"verdict-title\">Your exposure is within safe limits</h2>");
      sb.append("<p class=\"verdict-body\">Based on your answers, nothing reached the level occupational "
        + "health and safety standards treat as high exposure.</p>");
    }
    sb.append("</div>");
  }

  private void appendTree(StringBuilder sb, List<ReportTreeNodeVO> nodes, int depth) {
    for (ReportTreeNodeVO node : nodes) {
      sb.append("<div class=\"tree-node\" style=\"margin-left:").append(depth * 16).append("px;\">");
      sb.append("<span class=\"badge\">")
        .append(escape(node.getHeader())).append(" ").append(escape(node.getNumber()))
        .append("</span> ");
      sb.append("<span class=\"node-text\">").append(escape(node.getName())).append("</span>");
      sb.append("</div>");
      if (node.getNodes() != null && !node.getNodes().isEmpty()) {
        appendTree(sb, node.getNodes(), depth + 1);
      }
    }
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
      + ".verdict-title { font-size: 13pt; margin: 4px 0 6px; }"
      + ".verdict-body { margin: 0; font-size: 9.5pt; color: #5b5c53; }"
      + ".finding { padding: 6px 0 6px 10px; border-top: 1px solid #edf2f5; border-left: 4px solid #b3452f; }"
      + ".finding-name { font-weight: bold; font-size: 10.5pt; }"
      + ".finding-rationale { margin: 3px 0 0; font-size: 9pt; color: #5b5c53; }"
      + ".finding-action { margin: 3px 0 0; font-size: 9pt; font-weight: bold; color: #2e7d95; }"
      + ".noted-item { padding: 6px 0 6px 10px; border-top: 1px solid #edf2f5; border-left: 4px solid #b8752b; }"
      + ".noted-item-low { border-left-color: #a68a2c; }"
      + ".noted-name { font-weight: bold; font-size: 10pt; }"
      + ".noted-text { margin: 3px 0 0; font-size: 9pt; color: #5b5c53; }"
      + ".noted-empty { font-size: 9pt; color: #8b8c80; }"
      + ".disclaimer { margin-top: 16px; padding: 8px 12px; background-color: #edf2f5; border-radius: 4px; font-size: 8pt; color: #5b5c53; }"
      + ".disclaimer strong { color: #2e7d95; }"
      + ".tree-node { padding: 2px 0; }"
      + ".badge { background-color: #edf2f5; border-radius: 3px; padding: 1px 4px; font-size: 8pt; }"
      + ".node-text { font-size: 9pt; }";
  }
}
