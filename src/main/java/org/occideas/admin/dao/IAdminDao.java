package org.occideas.admin.dao;

import org.occideas.entity.Node;

import java.util.List;

public interface IAdminDao {

  List<Node> getListOfOrphanNodes();

  void deleteOrphanNodes();

  void setDeletedFlagForListOfNodes(List<Node> listOfOrphanNodes);

  void setForeignKeyCheck(int flag);

  void deleteAllInterviews();

  void deleteAllParticipants();

}
