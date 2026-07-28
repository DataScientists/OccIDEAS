package org.occideas.email.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.email.service.EmailService;
import org.occideas.email.service.ReportPdfService;
import org.occideas.vo.EmailReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/emailreport")
public class EmailReportRestController {

  private static final Logger log = LogManager.getLogger(EmailReportRestController.class);

  @Autowired
  private EmailService emailService;

  @Autowired
  private ReportPdfService reportPdfService;

  @POST
  @Path(value = "/send")
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  public Response send(EmailReportVO request) {
    if (request == null || StringUtils.isBlank(request.getEmail())) {
      return Response.status(Response.Status.BAD_REQUEST).type("text/plain")
        .entity("email is required").build();
    }
    try {
      byte[] pdfBytes = reportPdfService.generatePdf(request);
      emailService.sendReportPdf(request.getEmail(), "assessment-report.pdf", pdfBytes);
      return Response.ok().build();
    } catch (Throwable e) {
      log.error("Failed to email report for interviewId={}", request.getInterviewId(), e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain")
        .entity("Failed to send email").build();
    }
  }
}
