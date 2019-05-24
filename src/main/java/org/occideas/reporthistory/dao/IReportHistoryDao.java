package org.occideas.reporthistory.dao;

import org.occideas.entity.InterviewRuleReport;
import org.occideas.entity.ReportHistory;

import java.util.List;

public interface IReportHistoryDao {

  List<ReportHistory> getAll();

  List<ReportHistory> getByType(String type);

  ReportHistory getLatestByType(String type);

  ReportHistory save(ReportHistory entity);

  void delete(ReportHistory entity);

  List<InterviewRuleReport> getInterviewRuleReport();

  List<InterviewRuleReport> getInterviewRuleReportFilter(List<Long> agentIds);

}
