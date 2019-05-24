package org.occideas.mapper;

import org.occideas.entity.InterviewFiredRules;
import org.occideas.vo.InterviewFiredRulesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterviewFiredRulesMapperImpl implements InterviewFiredRulesMapper {

  @Autowired
  private InterviewMapper interviewMapper;
  @Autowired
  private RuleMapper ruleMapper;

  @Override
  public InterviewFiredRulesVO convertToInterviewFiredRulesVO(InterviewFiredRules entity) {
    if (entity == null) {
      return null;
    }
    InterviewFiredRulesVO vo = new InterviewFiredRulesVO();
    vo.setIdinterview(entity.getIdinterview());
    vo.setId(entity.getId());
    vo.setIdRule(entity.getIdRule());
    return vo;
  }

  @Override
  public InterviewFiredRules convertToInterviewFiredRules(InterviewFiredRulesVO vo) {
    if (vo == null) {
      return null;
    }
    InterviewFiredRules entity = new InterviewFiredRules();
    entity.setIdinterview(vo.getIdinterview());
    entity.setId(vo.getId());
    entity.setIdRule(vo.getIdRule());
    return entity;
  }

  @Override
  public List<InterviewFiredRulesVO> convertToInterviewFiredRulesVOList(List<InterviewFiredRules> entity) {
    if (entity == null) {
      return null;
    }
    List<InterviewFiredRulesVO> list = new ArrayList<>();
    for (InterviewFiredRules rules : entity) {
      list.add(convertToInterviewFiredRulesVO(rules));
    }
    return list;
  }

  @Override
  public InterviewFiredRulesVO convertToInterviewFiredRulesVOWithRules(InterviewFiredRules entity) {
    if (entity == null) {
      return null;
    }
    InterviewFiredRulesVO vo = new InterviewFiredRulesVO();
    vo.setIdinterview(entity.getIdinterview());
    vo.setId(entity.getId());
    vo.setIdRule(entity.getIdRule());
    //vo.setInterviews(interviewMapper.convertToInterviewVOnoQsList(entity.getInterviews()));
    vo.setRules(ruleMapper.convertToRuleVOExcPaList(entity.getRules()));
    return vo;
  }

  @Override
  public List<InterviewFiredRulesVO> convertToInterviewFiredRulesVOWithRulesList(List<InterviewFiredRules> entity) {
    if (entity == null) {
      return null;
    }
    List<InterviewFiredRulesVO> list = new ArrayList<>();
    for (InterviewFiredRules rules : entity) {
      list.add(convertToInterviewFiredRulesVOWithRules(rules));
    }
    return list;
  }

}
