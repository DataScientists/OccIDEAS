package org.occideas.reporthistory.dao;

import org.occideas.entity.InterviewRuleReport;
import org.occideas.entity.ReportHistory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IReportHistoryDao {

    List<ReportHistory> getAll();

    List<ReportHistory> getByType(String type);

    ReportHistory getLatestByType(String type);

    ReportHistory save(ReportHistory entity);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    ReportHistory saveNewTransaction(ReportHistory entity);

    void delete(ReportHistory entity);

    List<InterviewRuleReport> getInterviewRuleReport();

    List<InterviewRuleReport> getInterviewRuleReportFilter(List<Long> agentIds);

    long getMaxId();

}
