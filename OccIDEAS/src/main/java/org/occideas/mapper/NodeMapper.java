package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.Node;
import org.occideas.vo.NodeVO;

public interface NodeMapper {

	NodeVO convertToNodeVO(Node node);
	
	List<NodeVO> convertToNodeVOList(List<Node> node);

	Node convertToModule(NodeVO nodeVO);
	
	List<Node> convertToNodeList(List<NodeVO> nodeVO);
	
}
