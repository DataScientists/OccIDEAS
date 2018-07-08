package org.occideas.admin.dao;

import java.util.List;

import org.occideas.entity.Node;

public interface IAdminDao
{

    List<Node> getListOfOrphanNodes();

    void deleteOrphanNodes();

    void setDeletedFlagForListOfNodes(List<Node> listOfOrphanNodes);

    void setForeignKeyCheck(int flag);

	void deleteAllInterviews();

	void deleteAllParticipants();
    
}
