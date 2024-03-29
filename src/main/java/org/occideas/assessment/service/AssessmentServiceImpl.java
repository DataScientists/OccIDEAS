package org.occideas.assessment.service;

import org.occideas.assessment.dao.AssessmentDao;
import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.utilities.PageUtil;
import org.occideas.vo.AssessmentAnswerSummaryFilterVO;
import org.occideas.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

  @Autowired
  private AssessmentDao dao;
  private PageUtil<AssessmentAnswerSummary> pageUtilIntMod = new PageUtil<>();

  @Override
  public PageVO<AssessmentAnswerSummary> getAnswerSummaryByName(AssessmentAnswerSummaryFilterVO filter) {
    List<AssessmentAnswerSummary> list = dao.getAnswerSummary(filter);
    PageVO<AssessmentAnswerSummary> page = pageUtilIntMod.populatePage(list, filter.getPageNumber(), filter.getSize());
    page.setTotalSize(dao.getAnswerSummaryTotalCount(filter).intValue());
    return page;
  }


}
