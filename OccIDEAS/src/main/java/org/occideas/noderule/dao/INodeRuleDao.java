package org.occideas.noderule.dao;

import java.util.List;

import org.occideas.entity.NodeRule;

public interface INodeRuleDao {

	void saveOrUpdate(NodeRule nodeRule);

	void save(NodeRule nodeRule);

	void deleteAll();

	void saveBatchNodeRule(List<NodeRule> nrules);

}
