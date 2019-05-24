package org.occideas.noderule.dao;

import org.occideas.entity.NodeRule;

import java.util.List;

public interface INodeRuleDao {

  void saveOrUpdate(NodeRule nodeRule);

  void save(NodeRule nodeRule);

  void deleteAll();

  void saveBatchNodeRule(List<NodeRule> nrules);

}
