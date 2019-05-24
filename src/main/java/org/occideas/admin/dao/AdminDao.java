package org.occideas.admin.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.occideas.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository 
public class AdminDao implements IAdminDao
{

    @Autowired
    private SessionFactory sessionFactory;

    private final String SELECT_ORPHAN_NODES = "Select * from Node where parent_idNode NOT IN (SELECT idNode FROM Node)";
    
    private final String DELETE_NODES_WITH_DELETEFLAG 
    = " DELETE FROM Node WHERE deleted=1 AND idNode>0";
    
    private final String SET_FOREIGN_KEY = "SET FOREIGN_KEY_CHECKS=:flag";
    
    @Override
    public void setDeletedFlagForListOfNodes(List<Node> listOfOrphanNodes)
    {
        final Session currentSession = sessionFactory.getCurrentSession();
        for(Node node : listOfOrphanNodes) {
            node.setDeleted(1);
            currentSession.save(node);
        }
    }
    
    @Override
    public void setForeignKeyCheck(int flag)
    {
        SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(SET_FOREIGN_KEY);
        sqlQuery.setParameter("flag", flag);
        sqlQuery.executeUpdate();
    }

    @Override
    public void deleteOrphanNodes()
    {
        SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(DELETE_NODES_WITH_DELETEFLAG);
        sqlQuery.executeUpdate();
    }

    @Override
    public List<Node> getListOfOrphanNodes()
    {
        final Session session = sessionFactory.getCurrentSession();
        Query sqlQuery = session.createSQLQuery(SELECT_ORPHAN_NODES).addEntity(Node.class);;
        List<Node> list = (List<Node>) sqlQuery.list();
        return list;
    }

	@Override
	public void deleteAllInterviews() {
		final Session session = sessionFactory.getCurrentSession();
		session.createSQLQuery("truncate table Interview").executeUpdate();
		session.createSQLQuery("truncate table Interview_Answer").executeUpdate();
		session.createSQLQuery("truncate table Interview_AutoAssessedRules").executeUpdate();
		session.createSQLQuery("truncate table Interview_Display").executeUpdate();
		session.createSQLQuery("truncate table Interview_DisplayAnswer").executeUpdate();
		session.createSQLQuery("truncate table Interview_FiredRules").executeUpdate();
		session.createSQLQuery("truncate table Interview_ManualAssessedRules").executeUpdate();
		session.createSQLQuery("truncate table Interview_Module").executeUpdate();
		session.createSQLQuery("truncate table Interview_Question").executeUpdate();
	}

	@Override
	public void deleteAllParticipants() {
		final Session session = sessionFactory.getCurrentSession();
		session.createSQLQuery("truncate table Participant").executeUpdate();
	}
    
    
    
}
