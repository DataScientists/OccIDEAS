package org.occideas.rule.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Rule;
import org.occideas.mapper.RuleMapper;
import org.occideas.rule.dao.IRuleDao;
import org.occideas.rule.dao.RuleDao;
import org.occideas.security.audit.Auditable;
import org.occideas.security.audit.AuditingActionType;
import org.occideas.utilities.StudyAgentUtil;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RuleServiceImpl implements RuleService {

    @Autowired
    private IRuleDao dao;

    @Autowired
    private RuleMapper mapper;

    @Autowired
    private StudyAgentUtil studyAgentUtil;
    
    @Override
    public List<RuleVO> listAll() {
        return mapper.convertToRuleVOList(dao.getAll());
    }

    @Override
    public List<RuleVO> findById(Long id) {
        Rule rule = dao.get(id);
        RuleVO ruleVO = mapper.convertToRuleVO(rule);
        List<RuleVO> list = new ArrayList<RuleVO>();
        list.add(ruleVO);
        return list;
    }

    @Auditable(actionType = AuditingActionType.CREATE_RULE)
    @Override
    public RuleVO create(RuleVO o) {
        Rule rule = new Rule();
        rule.setIdRule(dao.save(mapper.convertToRule(o)));
        rule.setAgentId(o.getAgentId());
        List<PossibleAnswerVO> conditions = o.getConditions();
    	if(conditions!=null && !conditions.isEmpty()){
    		PossibleAnswerVO possibleAnswerVO = conditions.get(0);
    		long topNodeId = possibleAnswerVO.getTopNodeId();
    		studyAgentUtil.createStudyAgentForUpdatedNode(topNodeId,possibleAnswerVO.getName());
    	}
        return mapper.convertToRuleVO(rule);
    }

    @Auditable(actionType = AuditingActionType.SAVE_UPD_RULE)
    @Override
    public void saveOrUpdate(RuleVO o) {
        dao.saveOrUpdate(mapper.convertToRule(o));
        List<PossibleAnswerVO> conditions = o.getConditions();
    	if(conditions!=null && !conditions.isEmpty()){
    		PossibleAnswerVO possibleAnswerVO = conditions.get(0);
    		long topNodeId = possibleAnswerVO.getTopNodeId();
    		studyAgentUtil.createStudyAgentForUpdatedNode(topNodeId,possibleAnswerVO.getName());
    	}
    }

    @Auditable(actionType = AuditingActionType.DELETE_RULE)
    @Override
    public void delete(RuleVO o) {
        dao.delete(mapper.convertToRule(o));
        List<PossibleAnswerVO> conditions = o.getConditions();
    	if(conditions!=null && !conditions.isEmpty()){
    		PossibleAnswerVO possibleAnswerVO = conditions.get(0);
    		long topNodeId = possibleAnswerVO.getTopNodeId();
    		studyAgentUtil.createStudyAgentForUpdatedNode(topNodeId,possibleAnswerVO.getName());
    	}
    }

	@Override
	public void update(RuleVO o) {
		dao.saveOrUpdate(mapper.convertToRule(o));
		List<PossibleAnswerVO> conditions = o.getConditions();
    	if(conditions!=null && !conditions.isEmpty()){
    		PossibleAnswerVO possibleAnswerVO = conditions.get(0);
    		long topNodeId = possibleAnswerVO.getTopNodeId();
    		studyAgentUtil.createStudyAgentForUpdatedNode(topNodeId,possibleAnswerVO.getName());
    	}
	}

    @Override
    public List<RuleVO> findByAgentId(long agentId){
        List<Rule> rules = dao.findByAgentId(agentId);
        return mapper.convertToRuleVOList(rules);
    }
}
