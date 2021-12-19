package org.occideas.reporthistory.service;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.occideas.entity.CustomMappingStrategy;
import org.occideas.entity.InterviewRuleReport;
import org.occideas.mapper.InterviewRulesMapper;
import org.occideas.mapper.ReportHistoryMapper;
import org.occideas.reporthistory.dao.ReportHistoryDao;
import org.occideas.vo.ReportHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

@Service
@Transactional
public class ReportHistoryServiceImpl implements ReportHistoryService {

  @Autowired
  private ReportHistoryDao dao;
  @Autowired
  private ReportHistoryMapper mapper;
  @Autowired
  private InterviewRulesMapper interviewRuleMapper;

  @Override
  public List<ReportHistoryVO> getAll() {
    return mapper.convertToReportHistoryVOList(dao.getAll());
  }

  @Override
  public List<ReportHistoryVO> getByType(String type) {
    return mapper.convertToReportHistoryVOList(dao.getByType(type));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ReportHistoryVO save(ReportHistoryVO vo) {
    return mapper.convertToReportHistoryVO(dao.save(mapper.
            convertToReportHistory(vo)));
  }

  @Override
  public ReportHistoryVO saveNewTransaction(ReportHistoryVO vo) {
    return mapper.convertToReportHistoryVO(dao.saveNewTransaction(mapper.
            convertToReportHistory(vo)));
  }


  @Override
  public void delete(ReportHistoryVO vo) {
    File file = new File(vo.getPath());
    file.delete();
    dao.delete(mapper.convertToReportHistory(vo));
  }

  @Override
  /**
   * Get latest record for the given report type
   */
  public ReportHistoryVO getLatestByType(String type) {
    return mapper.convertToReportHistoryVO(dao.getLatestByType(type));
  }

  @Override
  public Integer generateInterviewRuleFilterReport(String filepath, List<Long> agentIds) throws Exception {
    List<InterviewRuleReport> list = dao.getInterviewRuleReportFilter(agentIds);
    CustomMappingStrategy<InterviewRuleReport> mappingStrategy = new CustomMappingStrategy<>();
    mappingStrategy.setType(InterviewRuleReport.class);
    Writer writer = new FileWriter(filepath);
    StatefulBeanToCsv<InterviewRuleReport> beanToCsv =
            new StatefulBeanToCsvBuilder<InterviewRuleReport>(writer)
                    .withMappingStrategy(mappingStrategy).withSeparator(',').withApplyQuotesToAll(false).build();
    beanToCsv.write(list);
    writer.flush();
    writer.close();
    if (list.isEmpty()) {
      return 0;
    }
    return list.size();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public long getMaxId() {
    return dao.getMaxId();
  }


}
