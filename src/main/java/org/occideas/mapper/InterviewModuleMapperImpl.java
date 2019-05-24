package org.occideas.mapper;

import org.occideas.entity.InterviewModule;
import org.occideas.vo.InterviewModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterviewModuleMapperImpl implements InterviewModuleMapper {

  @Autowired
  private InterviewQuestionMapper iqMapper;

  @Override
  public InterviewModuleVO convertToInterviewModuleVO(InterviewModule entity) {
    if (entity == null) {
      return null;
    }
    InterviewModuleVO vo = new InterviewModuleVO();
    vo.setCount(entity.getCount());
    vo.setDeleted(entity.getDeleted());
    vo.setIdInterview(entity.getIdInterview());
    vo.setIdNode(entity.getIdNode());
    vo.setName(entity.getName());
    vo.setNumber(entity.getNumber());
    vo.setParentAnswerId(entity.getParentAnswerId());
    vo.setParentNode(entity.getParentNode());
    vo.setAnswerNode(entity.getAnswerNode());
    vo.setSequence(entity.getSequence());
    vo.setTopNodeId(entity.getTopNodeId());
    vo.setQuestionsAsked(iqMapper.convertToInterviewQuestionVOList(entity.getQuestionsAsked()));
    return vo;
  }

  @Override
  public List<InterviewModuleVO> convertToInterviewModuleVOList(List<InterviewModule> entity) {
    if (entity == null) {
      return null;
    }

    List<InterviewModuleVO> list = new ArrayList<>();
    for (InterviewModule mod : entity) {
      list.add(convertToInterviewModuleVO(mod));
    }

    return list;
  }

  @Override
  public InterviewModule convertToInterviewModule(InterviewModuleVO vo) {
    if (vo == null) {
      return null;
    }
    InterviewModule entity = new InterviewModule();
    entity.setCount(vo.getCount());
    entity.setDeleted(vo.getDeleted());
    entity.setIdInterview(vo.getIdInterview());
    entity.setIdNode(vo.getIdNode());
    entity.setName(vo.getName());
    entity.setNumber(vo.getNumber());
    entity.setParentAnswerId(vo.getParentAnswerId());
    entity.setParentNode(vo.getParentNode());
    entity.setAnswerNode(vo.getAnswerNode());
    entity.setSequence(vo.getSequence());
    entity.setTopNodeId(vo.getTopNodeId());
    entity.setQuestionsAsked(iqMapper.convertToInterviewQuestionList(vo.getQuestionsAsked()));
    return entity;
  }

  @Override
  public List<InterviewModule> convertToInterviewModuleList(List<InterviewModuleVO> voList) {
    if (voList == null) {
      return null;
    }

    List<InterviewModule> list = new ArrayList<>();
    for (InterviewModuleVO mod : voList) {
      list.add(convertToInterviewModule(mod));
    }

    return list;
  }


}
