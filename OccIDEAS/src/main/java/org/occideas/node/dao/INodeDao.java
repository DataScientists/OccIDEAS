package org.occideas.node.dao;

import java.util.List;

import org.occideas.entity.Node;
import org.occideas.entity.NodePlain;

public interface INodeDao
{

    public Node getNode(Long id);

	void deleteAll();

	void saveBatchNodes(List<Node> nodes);

	public void saveBatchNodesPlain(List<NodePlain> nodes);
    
}
