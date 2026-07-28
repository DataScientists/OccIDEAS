package org.occideas.email.service;

public interface EmailService {

  void sendReportPdf(String toAddress, String fileName, byte[] pdfBytes);

}
