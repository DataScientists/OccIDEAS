package org.occideas.email.service;

import org.occideas.vo.EmailReportVO;

public interface ReportPdfService {

  byte[] generatePdf(EmailReportVO reportData);

}
