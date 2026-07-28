package org.occideas.email.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.email.service.EmailService;
import org.occideas.vo.EmailReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Base64;

@Path("/emailreport")
public class EmailReportRestController {

  private static final Logger log = LogManager.getLogger(EmailReportRestController.class);

  @Autowired
  private EmailService emailService;

  @POST
  @Path(value = "/send")
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  public Response send(EmailReportVO request) {
    if (request == null || StringUtils.isBlank(request.getEmail()) || StringUtils.isBlank(request.getPdfBase64())) {
      return Response.status(Response.Status.BAD_REQUEST).type("text/plain")
        .entity("email and pdfBase64 are required").build();
    }
    try {
      byte[] pdfBytes = Base64.getDecoder().decode(request.getPdfBase64());
      String fileName = StringUtils.isNotBlank(request.getFileName()) ? request.getFileName() : "assessment-report.pdf";
      emailService.sendReportPdf(request.getEmail(), fileName, pdfBytes);
      return Response.ok().build();
    } catch (Throwable e) {
      log.error("Failed to email report for interviewId={}", request.getInterviewId(), e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("text/plain")
        .entity("Failed to send email").build();
    }
  }
}
