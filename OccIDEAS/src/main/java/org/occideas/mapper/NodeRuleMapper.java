package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.NodeRule;
import org.occideas.vo.NodeRuleVO;

public interface NodeRuleMapper {

	NodeRule convertToNodeRule(NodeRuleVO vo);

	NodeRuleVO convertToNodeRuleVO(NodeRule entity);

	List<NodeRule> convertToNodeRuleList(List<NodeRuleVO> voList);
	
}
