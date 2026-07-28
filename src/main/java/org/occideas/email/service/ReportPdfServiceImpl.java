package org.occideas.email.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.commons.lang3.StringEscapeUtils;
import org.occideas.vo.EmailReportVO;
import org.occideas.vo.ReportAgentVO;
import org.occideas.vo.ReportConditionVO;
import org.occideas.vo.ReportRuleVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Renders the report as a small, dedicated HTML/CSS document (block/inline-block layout only -
 * openhtmltopdf does not support flexbox, grid, sticky positioning or box-shadow) and converts it
 * directly to PDF. This deliberately avoids the live app's stylesheet and any DOM-screenshot
 * approach (jsPDF/html2canvas), which proved unreliable for this content.
 */
@Service
public class ReportPdfServiceImpl implements ReportPdfService {

  private static final Logger logger = LoggerFactory.getLogger(ReportPdfServiceImpl.class);

  private static final Map<String, String> LEVEL_COLORS = new HashMap<>();
  private static final String LOGO_RESOURCE_PATH = "/images/occideas-logo.png";
  private static final DateTimeFormatter REPORT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
  private static final String LOGO_DATA_URI = loadLogoDataUri();

  static {
    LEVEL_COLORS.put("probHigh", "#ff0000");
    LEVEL_COLORS.put("probMedium", "#ffa500");
    LEVEL_COLORS.put("probLow", "#ffff00");
    LEVEL_COLORS.put("probUnknown", "#ffc0cb");
    LEVEL_COLORS.put("possUnknown", "#add8e6");
    LEVEL_COLORS.put("noExposure", "#e5e5e5");
  }

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

    sb.append("<h1>Health Hazards Found</h1>");
    List<ReportAgentVO> agents = reportData.getAgents();
    if (agents == null || agents.isEmpty()) {
      sb.append("<p class=\"no-hazards\">No health hazards were found for this interview.</p>");
    } else {
      for (ReportAgentVO agent : agents) {
        sb.append("<div class=\"agent-row\">");
        sb.append("<span class=\"agent-name\">").append(escape(agent.getName())).append("</span>");
        sb.append("<span class=\"agent-rules\">");
        appendRules(sb, agent.getRules());
        sb.append("</span></div>");
      }
    }

    sb.append("<h1>Interview Responses</h1>");
    if (reportData.getTree() != null) {
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

  private void appendRules(StringBuilder sb, List<ReportRuleVO> rules) {
    if (rules == null) {
      return;
    }
    for (ReportRuleVO rule : rules) {
      String color = LEVEL_COLORS.getOrDefault(rule.getLevel(), "#cccccc");
      List<ReportConditionVO> conditions = rule.getConditions();
      if (conditions == null || conditions.isEmpty()) {
        continue;
      }
      boolean multi = conditions.size() > 1;
      if (multi) {
        sb.append("<span class=\"rule-group-multi\">");
      }
      for (ReportConditionVO cond : conditions) {
        sb.append("<span class=\"chip\" style=\"background-color:").append(color).append(";\">")
          .append(escape(cond.getHeader())).append(" ").append(escape(cond.getNumber()))
          .append("</span>");
      }
      if (multi) {
        sb.append("</span>");
      }
    }
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

  private String escape(String value) {
    return value == null ? "" : StringEscapeUtils.escapeHtml4(value);
  }

  private String css() {
    return "body { font-family: Helvetica, Arial, sans-serif; font-size: 10pt; color: #222222; }"
      + ".report-header { border-bottom: 2px solid #3d9cbf; padding-bottom: 8px; margin-bottom: 4px; }"
      + ".report-logo { height: 40px; vertical-align: middle; }"
      + ".report-date { float: right; font-size: 9pt; color: #666666; line-height: 40px; }"
      + "h1 { font-size: 14pt; color: #3d9cbf; border-bottom: 1px solid #3d9cbf; padding-bottom: 4px; margin-top: 20px; }"
      + ".no-hazards { color: #2e7d32; font-weight: bold; }"
      + ".agent-row { padding: 4px 0; border-bottom: 1px solid #edf2f5; }"
      + ".agent-name { display: inline-block; width: 120px; font-weight: bold; vertical-align: top; }"
      + ".agent-rules { display: inline-block; }"
      + ".chip { display: inline-block; padding: 2px 5px; margin: 2px; border-radius: 3px; font-size: 8pt; font-weight: bold; }"
      + ".rule-group-multi { display: inline-block; border: 1px solid #bbbbbb; border-radius: 3px; padding: 1px 2px; margin: 2px; }"
      + ".tree-node { padding: 2px 0; }"
      + ".badge { background-color: #edf2f5; border-radius: 3px; padding: 1px 4px; font-size: 8pt; }"
      + ".node-text { font-size: 9pt; }";
  }
}
