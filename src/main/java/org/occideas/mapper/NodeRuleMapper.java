package org.occideas.mapper;

import org.occideas.entity.NodeRule;
import org.occideas.vo.NodeRuleVO;

import java.util.List;

public interface NodeRuleMapper {

  NodeRule convertToNodeRule(NodeRuleVO vo);

  NodeRuleVO convertToNodeRuleVO(NodeRule entity);

  List<NodeRule> convertToNodeRuleList(List<NodeRuleVO> voList);

}
