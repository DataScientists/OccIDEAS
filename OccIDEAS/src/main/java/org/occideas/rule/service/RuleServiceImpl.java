package org.occideas.rule.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Rule;
import org.occideas.mapper.RuleMapper;
import org.occideas.rule.dao.RuleDao;
import org.occideas.security.audit.Auditable;
import org.occideas.security.audit.AuditingActionType;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RuleServiceImpl implements RuleService {

    @Autowired
    private RuleDao dao;

    @Autowired
    private RuleMapper mapper;

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
        return mapper.convertToRuleVO(rule);
    }

    @Auditable(actionType = AuditingActionType.SAVE_UPD_RULE)
    @Override
    public void saveOrUpdate(RuleVO o) {
        dao.saveOrUpdate(mapper.convertToRule(o));
    }

    @Auditable(actionType = AuditingActionType.DELETE_RULE)
    @Override
    public void delete(RuleVO o) {
        dao.delete(mapper.convertToRule(o));
    }

	@Override
	public void update(RuleVO o) {
		dao.saveOrUpdate(mapper.convertToRule(o));	
	}

    @Override
    public List<RuleVO> findByAgentId(long agentId){
        List<Rule> rules = dao.findByAgentId(agentId);
        return mapper.convertToRuleVOList(rules);
    }
}
