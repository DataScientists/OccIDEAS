package org.occideas.reporthistory.service;

import org.occideas.vo.ReportHistoryVO;

import java.util.List;

public interface ReportHistoryService {

  List<ReportHistoryVO> getAll();

  List<ReportHistoryVO> getByType(String type);

  ReportHistoryVO getLatestByType(String type);

  ReportHistoryVO save(ReportHistoryVO entity);

  ReportHistoryVO saveNewTransaction(ReportHistoryVO entity);

  void delete(ReportHistoryVO entity);

  Integer generateInterviewRuleFilterReport(String filepath, List<Long> agentIds) throws Exception;

  long getMaxId();

}
