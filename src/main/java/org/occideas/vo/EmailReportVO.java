package org.occideas.vo;

public class EmailReportVO {

  private Long interviewId;
  private String email;
  private String fileName;
  private String pdfBase64;

  public Long getInterviewId() {
    return interviewId;
  }

  public void setInterviewId(Long interviewId) {
    this.interviewId = interviewId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getPdfBase64() {
    return pdfBase64;
  }

  public void setPdfBase64(String pdfBase64) {
    this.pdfBase64 = pdfBase64;
  }
}
