package org.occideas.admin.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.admin.dao.IAdminDao;
import org.occideas.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService implements IAdminService
{

    private Logger log = LogManager.getLogger(this.getClass());
    
    @Autowired
    private IAdminDao dao;
    
    @Override
    public void cleanOrphans()
    {
        List<Node> listOfOrphanNodes = dao.getListOfOrphanNodes();
        logNodesToBeDeleted(listOfOrphanNodes);
        dao.setDeletedFlagForListOfNodes(listOfOrphanNodes);
        dao.setForeignKeyCheck(0);
        dao.deleteOrphanNodes();
        dao.setForeignKeyCheck(1);
        boolean isOrphanRemoved = checkIfAllOrphanNodesAreRemoved();
        if(!isOrphanRemoved){
            cleanOrphans();
        }
    }

    @Override
    public boolean checkIfAllOrphanNodesAreRemoved()
    {
        List<Node> listOfOrphanNodes = dao.getListOfOrphanNodes();
        return listOfOrphanNodes.isEmpty();
    }

    private void logNodesToBeDeleted(List<Node> listOfOrphanNodes)
    {
        log.info("cleanOrphan nodes triggered.");
        for(Node node:listOfOrphanNodes){
            log.info("cleanOrphan node detected and will be removed "+node.getIdNode());
        }
    }
    
    
    
    

}
