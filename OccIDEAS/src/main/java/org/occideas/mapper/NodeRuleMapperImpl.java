package org.occideas.mapper;

import org.occideas.entity.NodeRule;
import org.occideas.vo.NodeRuleVO;
import org.springframework.stereotype.Component;

@Component
public class NodeRuleMapperImpl implements NodeRuleMapper{

	@Override
	public NodeRule convertToNodeRule(NodeRuleVO vo) {
		if(vo == null){
			return null;
		}
		NodeRule entity = new NodeRule();
		entity.setIdNode(vo.getIdNode());
		entity.setIdRule(vo.getIdRule());
		return entity;
	}

}
