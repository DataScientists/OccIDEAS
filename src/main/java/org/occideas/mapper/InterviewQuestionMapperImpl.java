package org.occideas.mapper;

import org.occideas.entity.InterviewQuestion;
import org.occideas.vo.InterviewQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterviewQuestionMapperImpl implements InterviewQuestionMapper {

  @Autowired
  private InterviewAnswerMapper answerMapper;

  @Override
  public InterviewQuestionVO convertToInterviewQuestionVO(InterviewQuestion question, boolean isIncludeAnswer) {
    if (question == null) {
      return null;
    }
    InterviewQuestionVO vo = new InterviewQuestionVO();
    vo.setId(question.getId());
    vo.setIdInterview(question.getIdInterview());
    vo.setName(question.getName());
    vo.setNodeClass(question.getNodeClass());
    vo.setNumber(question.getNumber());
    vo.setQuestionId(question.getQuestionId());
    vo.setType(question.getType());
    vo.setDescription(question.getDescription());
    vo.setDeleted(question.getDeleted());
    vo.setParentModuleId(question.getParentModuleId());
    vo.setTopNodeId(question.getTopNodeId());
    vo.setParentAnswerId(question.getParentAnswerId());
    vo.setModCount(question.getModCount());
    vo.setLink(question.getLink());
    if (isIncludeAnswer) {
      vo.setAnswers(answerMapper.convertToInterviewAnswerVOList(question.getAnswers()));
    }
    vo.setIntQuestionSequence(question.getIntQuestionSequence());
    vo.setProcessed(question.isProcessed());
    vo.setLastUpdated(question.getLastUpdated());

    return vo;
  }

  @Override
  public InterviewQuestionVO convertToInterviewQuestionNoAnswersVO(InterviewQuestion question) {
    if (question == null) {
      return null;
    }
    InterviewQuestionVO vo = new InterviewQuestionVO();
    vo.setId(question.getId());
    vo.setIdInterview(question.getIdInterview());
    vo.setName(question.getName());
    vo.setNodeClass(question.getNodeClass());
    vo.setNumber(question.getNumber());
    vo.setQuestionId(question.getQuestionId());
    vo.setType(question.getType());
    vo.setDescription(question.getDescription());
    vo.setDeleted(question.getDeleted());
    vo.setParentModuleId(question.getParentModuleId());
    vo.setTopNodeId(question.getTopNodeId());
    vo.setParentAnswerId(question.getParentAnswerId());
    vo.setModCount(question.getModCount());
    vo.setLink(question.getLink());
    //vo.setAnswers(answerMapper.convertToInterviewAnswerVOList(question.getAnswers()));
    vo.setIntQuestionSequence(question.getIntQuestionSequence());
    vo.setProcessed(question.isProcessed());
    vo.setLastUpdated(question.getLastUpdated());

    return vo;
  }

  @Override
  public InterviewQuestionVO convertToInterviewQuestionWithRulesVO(InterviewQuestion question) {
    if (question == null) {
      return null;
    }
    InterviewQuestionVO vo = new InterviewQuestionVO();
    vo.setId(question.getId());
    vo.setIdInterview(question.getIdInterview());
    vo.setName(question.getName());
    vo.setNodeClass(question.getNodeClass());
    vo.setNumber(question.getNumber());
    vo.setQuestionId(question.getQuestionId());
    vo.setType(question.getType());
    vo.setDescription(question.getDescription());
    vo.setDeleted(question.getDeleted());
    vo.setParentModuleId(question.getParentModuleId());
    vo.setTopNodeId(question.getTopNodeId());
    vo.setParentAnswerId(question.getParentAnswerId());
    vo.setLink(question.getLink());
    vo.setModCount(question.getModCount());
    vo.setAnswers(answerMapper.convertToInterviewAnswerWithRulesVOList(question.getAnswers()));
    vo.setIntQuestionSequence(question.getIntQuestionSequence());
    vo.setProcessed(question.isProcessed());

    return vo;
  }

  @Override
  public List<InterviewQuestionVO> convertToInterviewQuestionVOList(List<InterviewQuestion> question, boolean isIncludeAnswer) {
    if (question == null) {
      return null;
    }
    List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
    for (InterviewQuestion iq : question) {
      // if(iq.getDeleted() == 0){
      list.add(convertToInterviewQuestionVO(iq, isIncludeAnswer));
      // }
    }
    return list;
  }

  @Override
  public List<InterviewQuestionVO> convertToInterviewQuestionNoAnswersVOList(List<InterviewQuestion> question) {
    if (question == null) {
      return null;
    }
    List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
    for (InterviewQuestion iq : question) {
      // if(iq.getDeleted() == 0){
      list.add(convertToInterviewQuestionNoAnswersVO(iq));
      // }
    }
    return list;
  }

  @Override
  public List<InterviewQuestionVO> convertToInterviewQuestionVOListExcAnswers(List<InterviewQuestion> question) {
    if (question == null) {
      return null;
    }
    List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
    for (InterviewQuestion iq : question) {
      // if(iq.getDeleted() == 0){
      list.add(convertToInterviewQuestionVOExcAnswers(iq));
      // }
    }
    return list;
  }

  @Override
  public InterviewQuestionVO convertToInterviewQuestionVOExcAnswers(InterviewQuestion question) {
    if (question == null) {
      return null;
    }
    InterviewQuestionVO vo = new InterviewQuestionVO();
    vo.setId(question.getId());
    vo.setIdInterview(question.getIdInterview());
    vo.setName(question.getName());
    vo.setNodeClass(question.getNodeClass());
    vo.setNumber(question.getNumber());
    vo.setQuestionId(question.getQuestionId());
    vo.setType(question.getType());
    vo.setDescription(question.getDescription());
    vo.setDeleted(question.getDeleted());
    vo.setParentModuleId(question.getParentModuleId());
    vo.setTopNodeId(question.getTopNodeId());
    vo.setParentAnswerId(question.getParentAnswerId());
    vo.setModCount(question.getModCount());
    vo.setLink(question.getLink());
    vo.setIntQuestionSequence(question.getIntQuestionSequence());
    vo.setProcessed(question.isProcessed());
    vo.setLastUpdated(question.getLastUpdated());

    return vo;
  }

  @Override
  public List<InterviewQuestionVO> convertToInterviewQuestionWithRulesVOList(List<InterviewQuestion> question) {
    if (question == null) {
      return null;
    }
    List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
    for (InterviewQuestion iq : question) {
      // if(iq.getDeleted() == 0){
      list.add(convertToInterviewQuestionWithRulesVO(iq));
      // }
    }
    return list;
  }

  @Override
  public InterviewQuestion convertToInterviewQuestion(InterviewQuestionVO questionVO) {
    if (questionVO == null) {
      return null;
    }
    InterviewQuestion question = new InterviewQuestion();
    question.setId(questionVO.getId());
    question.setIdInterview(questionVO.getIdInterview());
    question.setName(questionVO.getName());
    question.setDeleted(questionVO.getDeleted());
    question.setDescription(questionVO.getDescription());
    question.setNodeClass(questionVO.getNodeClass());
    question.setNumber(questionVO.getNumber());
    question.setQuestionId(questionVO.getQuestionId());
    question.setType(questionVO.getType());
    question.setParentModuleId(questionVO.getParentModuleId());
    question.setTopNodeId(questionVO.getTopNodeId());
    question.setParentAnswerId(questionVO.getParentAnswerId());
    question.setLink(questionVO.getLink());
    question.setModCount(questionVO.getModCount());
    //question.setAnswers(answerMapper.convertToInterviewAnswerList(questionVO.getAnswers()));
    question.setIntQuestionSequence(questionVO.getIntQuestionSequence());
    question.setProcessed(questionVO.isProcessed());

    return question;
  }

  @Override
  public InterviewQuestion convertToInterviewQuestionWithAnswers(InterviewQuestionVO questionVO) {
    if (questionVO == null) {
      return null;
    }
    InterviewQuestion question = new InterviewQuestion();
    question.setId(questionVO.getId());
    question.setIdInterview(questionVO.getIdInterview());
    question.setName(questionVO.getName());
    question.setDeleted(questionVO.getDeleted());
    question.setDescription(questionVO.getDescription());
    question.setNodeClass(questionVO.getNodeClass());
    question.setNumber(questionVO.getNumber());
    question.setQuestionId(questionVO.getQuestionId());
    question.setType(questionVO.getType());
    question.setParentModuleId(questionVO.getParentModuleId());
    question.setTopNodeId(questionVO.getTopNodeId());
    question.setParentAnswerId(questionVO.getParentAnswerId());
    question.setLink(questionVO.getLink());
    question.setModCount(questionVO.getModCount());
    question.setAnswers(answerMapper.convertToInterviewAnswerList(questionVO.getAnswers()));
    question.setIntQuestionSequence(questionVO.getIntQuestionSequence());
    question.setProcessed(questionVO.isProcessed());

    return question;
  }

  @Override
  public List<InterviewQuestion> convertToInterviewQuestionList(List<InterviewQuestionVO> iq) {
    if (iq == null) {
      return null;
    }
    List<InterviewQuestion> list = new ArrayList<InterviewQuestion>();
    for (InterviewQuestionVO iqu : iq) {
      // if(iqu.getDeleted() == 0){
      list.add(convertToInterviewQuestion(iqu));
      // }
    }
    return list;
  }

  @Override
  public List<InterviewQuestion> convertToInterviewQuestionListWithAnswers(List<InterviewQuestionVO> iq) {
    if (iq == null) {
      return null;
    }
    List<InterviewQuestion> list = new ArrayList<InterviewQuestion>();
    for (InterviewQuestionVO iqu : iq) {
      // if(iqu.getDeleted() == 0){
      list.add(convertToInterviewQuestionWithAnswers(iqu));
      // }
    }
    return list;
  }

  @Override
  public InterviewQuestionVO convertToInterviewQuestionWithoutAnswers(InterviewQuestion question) {
    if (question == null) {
      return null;
    }
    InterviewQuestionVO vo = new InterviewQuestionVO();
    vo.setId(question.getId());
    vo.setIdInterview(question.getIdInterview());
    vo.setName(question.getName());
    vo.setNodeClass(question.getNodeClass());
    vo.setDeleted(question.getDeleted());
    vo.setNumber(question.getNumber());
    vo.setQuestionId(question.getQuestionId());
    vo.setType(question.getType());
    vo.setDescription(question.getDescription());
    vo.setParentModuleId(question.getParentModuleId());
    vo.setTopNodeId(question.getTopNodeId());
    vo.setParentAnswerId(question.getParentAnswerId());
    vo.setLink(question.getLink());
    vo.setModCount(question.getModCount());
    vo.setIntQuestionSequence(question.getIntQuestionSequence());
    return vo;
  }

  @Override
  public List<InterviewQuestionVO> convertToInterviewQuestionWithoutAnswersList(List<InterviewQuestion> question) {
    if (question == null) {
      return null;
    }
    List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
    for (InterviewQuestion iq : question) {
      if (iq.getDeleted() == 0 && iq.isProcessed()) {
        list.add(convertToInterviewQuestionWithoutAnswers(iq));
      }
    }
    return list;
  }

  @Override
  public InterviewQuestionVO convertToInterviewQuestionUnprocessedVO(InterviewQuestion question) {
    if (question == null || question.isProcessed() || question.getDeleted() == 1) {
      return null;
    }
    InterviewQuestionVO vo = new InterviewQuestionVO();
    vo.setId(question.getId());
    vo.setIdInterview(question.getIdInterview());
    vo.setName(question.getName());
    vo.setNodeClass(question.getNodeClass());
    vo.setNumber(question.getNumber());
    vo.setQuestionId(question.getQuestionId());
    vo.setType(question.getType());
    vo.setDescription(question.getDescription());
    vo.setDeleted(question.getDeleted());
    vo.setParentModuleId(question.getParentModuleId());
    vo.setTopNodeId(question.getTopNodeId());
    vo.setParentAnswerId(question.getParentAnswerId());
    vo.setModCount(question.getModCount());
    vo.setLink(question.getLink());
    //vo.setAnswers(answerMapper.convertToInterviewAnswerVOList(question.getAnswers()));
    vo.setIntQuestionSequence(question.getIntQuestionSequence());
    vo.setProcessed(question.isProcessed());
    vo.setLastUpdated(question.getLastUpdated());

    return vo;
  }

  @Override
  public List<InterviewQuestionVO> convertToInterviewQuestionUnprocessedVOList(List<InterviewQuestion> question) {
    if (question == null) {
      return null;
    }
    List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
    for (InterviewQuestion iq : question) {
      // if(iq.getDeleted() == 0){
      list.add(convertToInterviewQuestionUnprocessedVO(iq));
      // }
    }
    return list;
  }

  @Override
  public InterviewQuestionVO convertToInterviewQuestionVO(InterviewQuestion question) {
    return convertToInterviewQuestionVO(question, true);
  }

  @Override
  public List<InterviewQuestionVO> convertToInterviewQuestionVOList(List<InterviewQuestion> question) {
    return convertToInterviewQuestionVOList(question, false);
  }

  @Override
  public List<InterviewQuestionVO> convertToInterviewQuestionVOWithAnswerList(List<InterviewQuestion> question) {
    return convertToInterviewQuestionVOList(question, true);
  }

  @Override
  public List<InterviewQuestionVO> convertToInterviewQuestionVOList(List<InterviewQuestion> question,
                                                                    Long qId) {
    return convertToInterviewQuestionVOWithSelectedAnswer(question, false, qId);
  }

  private List<InterviewQuestionVO> convertToInterviewQuestionVOWithSelectedAnswer(List<InterviewQuestion> question,
                                                                                   boolean b, Long qId) {
    if (question == null) {
      return null;
    }
    List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
    for (InterviewQuestion iq : question) {
      // if(iq.getDeleted() == 0){
      if (iq.getId() == qId) {
        list.add(convertToInterviewQuestionVO(iq, true));
      } else {
        list.add(convertToInterviewQuestionVOExcAnswers(iq));
      }
      // }
    }
    return list;
  }

}
