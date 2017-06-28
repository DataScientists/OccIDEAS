package org.occideas.rule.dao;

import java.util.List;

import org.occideas.entity.Rule;

public interface IRuleDao {

	Long save(Rule rule);

	void delete(Rule rule);

	Rule get(Long id);

	Rule merge(Rule rule);

	void saveOrUpdate(Rule rule);

	List<Rule> getAll();

	List<Rule> findByAgentId(long agentId);

	Long getMaxRuleId();

}
