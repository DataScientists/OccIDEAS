package org.occideas.email.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

  private static final Logger log = LogManager.getLogger(EmailServiceImpl.class);

  @Autowired
  private JavaMailSender mailSender;

  @Value("${email.from}")
  private String fromAddress;

  @Override
  public void sendReportPdf(String toAddress, String fileName, byte[] pdfBytes) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(fromAddress);
      helper.setTo(toAddress);
      helper.setSubject("Your OccIDEAS Assessment Report");
      helper.setText("Please find your assessment report attached.");
      helper.addAttachment(fileName, new ByteArrayResource(pdfBytes));
      mailSender.send(message);
    } catch (Exception e) {
      log.error("Failed to send report email to {}", toAddress, e);
      throw new RuntimeException("Failed to send report email", e);
    }
  }
}
