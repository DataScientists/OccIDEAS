package org.occideas.node.dao;

import org.occideas.entity.Node;
import org.occideas.entity.NodePlain;

import java.util.List;

public interface INodeDao {

  Node getNode(Long id);

  void deleteAll();

  void saveBatchNodes(List<Node> nodes);

  void saveBatchNodesPlain(List<NodePlain> nodes);

}
