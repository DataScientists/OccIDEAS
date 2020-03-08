package org.occideas.possibleanswer.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.PossibleAnswerVO;

import java.util.List;

public interface PossibleAnswerService extends BaseService<PossibleAnswerVO> {

  List<PossibleAnswerVO> findByIdWithChildren(Long id);

  PossibleAnswerVO findAnswerWithRulesById(long id);

  PossibleAnswerVO findByTopNodeIdAndNumber(long moduleId, String answerNumber);
}
