package org.occideas.module.dao;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Module;
import org.occideas.entity.Node;
import org.occideas.entity.PossibleAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleDao implements IModuleDao{

	@Autowired
	private SessionFactory sessionFactory;

	public Module save(Module module){
      return (Module) sessionFactory.getCurrentSession().save(module);
    }
	
	@Transactional(value=TxType.REQUIRES_NEW)
	public void saveCopy(Module module){
	  sessionFactory.getCurrentSession().saveOrUpdate(module);
	}

    public void delete(Module module){
      sessionFactory.getCurrentSession().delete(module);
    }
    
    public List<Module> findByName(String name){
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(Module.class)
				.add(Restrictions.eq("name", name));
		return crit.list();
	}

	public Module get(Long id){
      return (Module) sessionFactory.getCurrentSession().get(Module.class, id);
    }
	
	public Module merge(Module module)   {
      return (Module) sessionFactory.getCurrentSession().merge(module);
    }

    public void saveOrUpdate(Module module){
      sessionFactory.getCurrentSession().saveOrUpdate(module);
    }

    @SuppressWarnings("unchecked")
	public List<Module> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(Module.class)
    		  						.setProjection(Projections.projectionList()
    		  						.add(Projections.property("idNode"),"idNode")
    		  						.add(Projections.property("name"),"name")
    		  						.add(Projections.property("description"),"description"))
    		  						.setResultTransformer(Transformers.aliasToBean(Module.class));
      return crit.list();
    }
    @SuppressWarnings("unchecked")
   	public List<Module> getAllActive() {
         final Session session = sessionFactory.getCurrentSession();
         final Criteria crit = session.createCriteria(Module.class)
        		 					.addOrder(Order.asc("name"))
        		 					.add(Restrictions.eq("deleted", 0))
       		  						.setProjection(Projections.projectionList()
       		  						.add(Projections.property("idNode"),"idNode")
       		  						.add(Projections.property("type"),"type")
       		  						.add(Projections.property("name"),"name")
       		  						.add(Projections.property("description"),"description"))
       		  						.setResultTransformer(Transformers.aliasToBean(Module.class));
         return crit.list();
       }
    @SuppressWarnings("unchecked")
    public Long generateIdNode(){
    	final Session session = sessionFactory.getCurrentSession();
    	final Criteria crit = session.createCriteria(Node.class)
    			.addOrder(Order.desc("idNode"))
				.setMaxResults(1)
    			.setProjection(Projections.projectionList()
    						.add(Projections.property("idNode"),"idNode"))
    						.setResultTransformer(Transformers.aliasToBean(Node.class));
    	List<Node> list = (List<Node>)crit.list();
		Long idNode = 0l;
		if(list.isEmpty()){
			Query query = session.createSQLQuery("SELECT AUTO_INCREMENT FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = \'occideas\' AND   TABLE_NAME   = \'Node\'");
			BigInteger id = (BigInteger) query.list().get(0);
			idNode = id.longValue();
		}else{
			idNode = list.get(0).getIdNode();
		}
    	return idNode;
    }

	@Override
	public Node getNodeById(Long idNode) {
		return (Node) sessionFactory.getCurrentSession().get(Node.class, idNode);
	}

	private final String GET_NODE_BY_LINK_AND_MOD_ID = "SELECT * FROM Node where idNode "+ 
			" in (select parent_idNode from node where link = :link and topNodeId = :modId)";
	
	@Override
	public List<PossibleAnswer> getNodeByLinkAndModId(Long link, Long modId) {
		final Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(GET_NODE_BY_LINK_AND_MOD_ID).
				addEntity(PossibleAnswer.class);
		sqlQuery.setParameter("modId", String.valueOf(modId));
		sqlQuery.setParameter("link", String.valueOf(link));
		List<PossibleAnswer> list = sqlQuery.list();
		return list;
	}

    
}
