package org.occideas.mapper;

import org.occideas.entity.InterviewDisplay;
import org.occideas.vo.InterviewDisplayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterviewDisplayMapperImpl implements InterviewDisplayMapper {

  @Autowired
  private InterviewDisplayAnswerMapper mapper;

  @Override
  public InterviewDisplayVO convertToInterviewDisplayVO(InterviewDisplay entity) {
    if (entity == null) {
      return null;
    }
    InterviewDisplayVO vo = new InterviewDisplayVO();
    vo.setId(entity.getId());
    vo.setDeleted(entity.getDeleted());
    vo.setIdInterview(entity.getIdInterview());
    vo.setLastUpdated(entity.getLastUpdated());
    vo.setName(entity.getName());
    vo.setNumber(entity.getNumber());
    vo.setQuestionId(entity.getQuestionId());
    vo.setType(entity.getType());
    vo.setSequence(entity.getSequence());
    vo.setHeader(entity.getHeader());
    vo.setTopNodeId(entity.getTopNodeId());
    vo.setParentModuleId(entity.getParentModuleId());
    vo.setParentAnswerId(entity.getParentAnswerId());
    vo.setLink(entity.getLink());
    vo.setDescription(entity.getDescription());
    vo.setNodeClass(entity.getNodeClass());
    vo.setAnswers(mapper.convertToInterviewDisplayAnswerVOList(entity.getAnswers()));
    return vo;
  }

  @Override
  public List<InterviewDisplayVO> convertToInterviewDisplayVOList(List<InterviewDisplay> entityList) {
    if (entityList == null) {
      return null;
    }

    List<InterviewDisplayVO> list = new ArrayList<>();
    for (InterviewDisplay disp : entityList) {
      list.add(convertToInterviewDisplayVO(disp));
    }

    return list;
  }

  @Override
  public InterviewDisplay convertToInterviewDisplay(InterviewDisplayVO vo) {
    if (vo == null) {
      return null;
    }

    InterviewDisplay entity = new InterviewDisplay();
    entity.setDeleted(vo.getDeleted());
    entity.setId(vo.getId());
    entity.setIdInterview(vo.getIdInterview());
    entity.setLastUpdated(vo.getLastUpdated());
    entity.setName(vo.getName());
    entity.setNumber(vo.getNumber());
    entity.setQuestionId(vo.getQuestionId());
    entity.setType(vo.getType());
    entity.setSequence(vo.getSequence());
    entity.setHeader(vo.getHeader());
    entity.setLink(vo.getLink());
    entity.setParentModuleId(vo.getParentModuleId());
    entity.setParentAnswerId(vo.getParentAnswerId());
    entity.setTopNodeId(vo.getTopNodeId());
    entity.setDescription(vo.getDescription());
    entity.setNodeClass(vo.getNodeClass());
    return entity;
  }

  @Override
  public List<InterviewDisplay> convertToInterviewDisplayList(List<InterviewDisplayVO> voList) {
    if (voList == null) {
      return null;
    }

    List<InterviewDisplay> list = new ArrayList<>();
    for (InterviewDisplayVO disp : voList) {
      list.add(convertToInterviewDisplay(disp));
    }

    return list;
  }

}
