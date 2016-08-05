package org.occideas.mapper;

import org.occideas.entity.NodeRule;
import org.occideas.vo.NodeRuleVO;

public interface NodeRuleMapper {

	NodeRule convertToNodeRule(NodeRuleVO vo);
	
}
