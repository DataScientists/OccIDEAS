package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	public List<NodeRule> convertToNodeRuleList(List<NodeRuleVO> voList){
		if(voList == null) {
			return null;
		}
		List<NodeRule> list = new ArrayList<>();
		for(NodeRuleVO vo:voList) {
			list.add(convertToNodeRule(vo));
		}
		return list;
	}
	
	@Override
	public NodeRuleVO convertToNodeRuleVO(NodeRule entity) {
		if(entity == null) {
			return null;
		}
		NodeRuleVO vo = new NodeRuleVO();
		vo.setIdNode(entity.getIdNode());
		vo.setIdRule(entity.getIdRule());
		return vo;
	}

}
