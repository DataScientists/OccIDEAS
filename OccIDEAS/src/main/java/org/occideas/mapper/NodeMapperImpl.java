package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Node;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.NodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeMapperImpl implements NodeMapper{
		
		@Autowired
		private ModuleRuleMapper moduleRuleMapper;
	
	    @Override
	    public NodeVO convertToNodeVO(Node node) {
	        if ( node == null ) {
	            return null;
	        }

	        NodeVO nodeVO = new NodeVO();

	        if(node.getIdNode()!=0){
	        	nodeVO.setIdNode( node.getIdNode() );
	        }
	        nodeVO.setName( node.getName() );
	        nodeVO.setDescription( node.getDescription() );
	        nodeVO.setType( node.getType() );
	        nodeVO.setSequence( node.getSequence() );
	        nodeVO.setNumber( node.getNumber() );
	        nodeVO.setParentId(node.getParentId());
	        nodeVO.setLink( node.getLink() );
	        nodeVO.setTopNodeId( node.getTopNodeId() );
	        nodeVO.setLastUpdated( node.getLastUpdated() );
	        List<Node> childNodes = node.getChildNodes();
	        if(!CommonUtil.isListEmpty(childNodes)){
	        nodeVO.setChildNodes( convertToNodeVOList( childNodes) );
	        }
	        nodeVO.setOriginalId( node.getOriginalId() );
	        nodeVO.setDeleted( node.getDeleted() );
	        nodeVO.setNodeclass( node.getNodeclass() );
	        if("P_simple".equals(nodeVO.getType())){
	        nodeVO.setModuleRule(moduleRuleMapper.convertToModuleRuleVOList(node.getModuleRule()));
	        }
	        return nodeVO;
	    }

	    @Override
	    public List<NodeVO> convertToNodeVOList(List<Node> node) {
	        if ( node == null ) {
	            return null;
	        }

	        List<NodeVO> list_ = new ArrayList<NodeVO>();
	        for ( Node node_ : node ) {
	            list_.add( convertToNodeVO( node_ ) );
	        }

	        return list_;
	    }

	    @Override
	    public Node convertToNode(NodeVO nodeVO) {
	        if ( nodeVO == null ) {
	            return null;
	        }

	        Node node = new Node();

	        node.setIdNode( nodeVO.getIdNode() );
	        node.setName( nodeVO.getName() );
	        node.setDescription( nodeVO.getDescription() );
	        node.setType( nodeVO.getType() );
	        node.setSequence( nodeVO.getSequence() );
	        node.setParentId(nodeVO.getParentId());
	        node.setLastUpdated( nodeVO.getLastUpdated() );
	        List<NodeVO> childNodes = nodeVO.getChildNodes();
	        if(!CommonUtil.isListEmpty(childNodes)){
	        node.setChildNodes( convertToNodeList(childNodes) );
	        }
	        node.setNumber( nodeVO.getNumber() );
	        node.setLink( nodeVO.getLink() );
	        node.setTopNodeId( nodeVO.getTopNodeId() );
	        node.setOriginalId( nodeVO.getOriginalId() );
	        node.setDeleted( nodeVO.getDeleted() );
	        node.setNodeclass( nodeVO.getNodeclass() );

	        return node;
	    }

	    @Override
	    public List<Node> convertToNodeList(List<NodeVO> nodeVO) {
	        if ( nodeVO == null ) {
	            return null;
	        }

	        List<Node> list_ = new ArrayList<Node>();
	        for ( NodeVO nodeVO_ : nodeVO ) {
	            list_.add( convertToNode( nodeVO_ ) );
	        }

	        return list_;
	    }
}
