package org.occideas.node.dao;

import java.util.List;

import org.occideas.entity.Node;

public interface INodeDao
{

    public Node getNode(Long id);

	void deleteAll();

	void saveBatchNodes(List<Node> nodes);
    
}
