package org.occideas.rule.dao;

import java.util.List;

import org.occideas.entity.Rule;
import org.occideas.entity.RulePlain;

public interface IRuleDao {

	Long save(Rule rule);

	void delete(Rule rule);
	
	void deleteAll();

	Rule get(Long id);

	Rule merge(Rule rule);

	void saveOrUpdate(Rule rule);

	List<Rule> getAll();

	List<Rule> findByAgentId(long agentId);

	Long getMaxRuleId();

	void saveBatchRule(List<RulePlain> rules);

}
