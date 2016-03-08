package org.occideas.rule.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Rule;
import org.occideas.mapper.RuleMapper;
import org.occideas.rule.dao.RuleDao;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

    @Override
    public RuleVO create(RuleVO o) {
        Rule rule = new Rule();
        rule.setIdRule(dao.save(mapper.convertToRule(o)));
        rule.setAgentId(o.getAgentId());
        return mapper.convertToRuleVO(rule);
    }

    @Override
    public void saveOrUpdate(RuleVO o) {
        dao.saveOrUpdate(mapper.convertToRule(o));
    }

    @Override
    public void delete(RuleVO o) {
        dao.delete(mapper.convertToRule(o));
    }

	@Override
	public void update(RuleVO o) {
		dao.saveOrUpdate(mapper.convertToRule(o));	
	}
    
}
