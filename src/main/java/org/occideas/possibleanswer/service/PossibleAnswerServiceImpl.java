package org.occideas.possibleanswer.service;

import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.mapper.PossibleAnswerMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.possibleanswer.dao.IPossibleAnswerDao;
import org.occideas.question.dao.IQuestionDao;
import org.occideas.vo.PossibleAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PossibleAnswerServiceImpl implements PossibleAnswerService {

  @Autowired
  private IPossibleAnswerDao possibleAnswerDao;
  @Autowired
  private IQuestionDao questionDao;

  @Autowired
  private PossibleAnswerMapper mapper;

  @Autowired
  private QuestionMapper questionMapper;

  @Override
  public List<PossibleAnswerVO> listAll() {
    return null;
  }

  @Override
  public List<PossibleAnswerVO> findById(Long id) {
    PossibleAnswer answer = possibleAnswerDao.get(id);
    PossibleAnswerVO paVO = mapper.convertToPossibleAnswerVO(answer, false);

    Question question = questionDao.get(Question.class, Long.valueOf(answer.getParentId()));
    paVO.setParent(questionMapper.convertToQuestionVO(question));

    List<PossibleAnswerVO> list = new ArrayList<PossibleAnswerVO>();
    list.add(paVO);
    return list;
  }

  @Override
  public List<PossibleAnswerVO> findByIdWithChildren(Long id) {
    PossibleAnswer answer = possibleAnswerDao.get(id);
    PossibleAnswerVO paVO = mapper.convertToPossibleAnswerVO(answer, true);

    List<PossibleAnswerVO> list = new ArrayList<PossibleAnswerVO>();
    list.add(paVO);
    return list;
  }

  @Override
  public PossibleAnswerVO create(PossibleAnswerVO o) {
    return null;
  }

  @Override
  public void update(PossibleAnswerVO o) {
  }

  @Override
  public void delete(PossibleAnswerVO o) {
  }

  @Override
  public PossibleAnswerVO findAnswerWithRulesById(long id) {
    PossibleAnswer answer = possibleAnswerDao.get(id);
    return mapper.convertToPossibleAnswerWithModuleRuleVO(answer);
  }

  @Override
  public PossibleAnswerVO findByTopNodeIdAndNumber(long moduleId, String answerNumber) {
    PossibleAnswer answer = possibleAnswerDao.findByTopNodeIdAndNumber(moduleId,answerNumber);
    PossibleAnswerVO paVO = mapper.convertToPossibleAnswerVOExcQuestionAnsChild(answer);
    return paVO;
  }

  @Override
  public PossibleAnswerVO findByIdExcludeChildren(Long id) {
    PossibleAnswer answer = possibleAnswerDao.get(id);
    return mapper.convertToPossibleAnswerVO(answer,false);
  }

}
