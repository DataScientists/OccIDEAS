package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Node;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.NodeVO;
import org.springframework.stereotype.Component;

@Component
public class NodeMapperImpl implements NodeMapper{

	    @Override
	    public NodeVO convertToNodeVO(Node node) {
	        if ( node == null ) {
	            return null;
	        }

	        NodeVO nodeVO = new NodeVO();

	        nodeVO.setIdNode( node.getIdNode() );
	        nodeVO.setName( node.getName() );
	        nodeVO.setDescription( node.getDescription() );
	        nodeVO.setType( node.getType() );
	        nodeVO.setSequence( node.getSequence() );
	        nodeVO.setNumber( node.getNumber() );
//	        nodeVO.setParent( convertToNodeVO( node.getParent() ) );
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
	    public Node convertToModule(NodeVO nodeVO) {
	        if ( nodeVO == null ) {
	            return null;
	        }

	        Node node = new Node();

	        node.setIdNode( nodeVO.getIdNode() );
	        node.setName( nodeVO.getName() );
	        node.setDescription( nodeVO.getDescription() );
	        node.setType( nodeVO.getType() );
	        node.setSequence( nodeVO.getSequence() );
//	        node.setParent( convertToModule( nodeVO.getParent() ) );
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
	            list_.add( convertToModule( nodeVO_ ) );
	        }

	        return list_;
	    }
}