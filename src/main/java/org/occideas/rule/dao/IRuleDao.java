package org.occideas.rule.dao;

import org.occideas.entity.Rule;
import org.occideas.entity.RulePlain;

import java.util.List;

public interface IRuleDao {

  Long save(Rule rule);

  void delete(Rule rule);

  void deleteAll();

  void deleteAll(List<Long> ruleIds);

  Rule get(Long id);

  Rule merge(Rule rule);

  void saveOrUpdate(Rule rule);

  List<Rule> getAll();

  List<Rule> findByAgentId(long agentId);

  Long getMaxRuleId();

  void saveBatchRule(List<RulePlain> rules);

}
