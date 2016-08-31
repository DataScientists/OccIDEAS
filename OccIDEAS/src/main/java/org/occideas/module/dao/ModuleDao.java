package org.occideas.module.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Module;
import org.occideas.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Module save(Module module){
      return (Module) sessionFactory.getCurrentSession().save(module);
    }
	
	public void saveCopy(Module module){
	  sessionFactory.getCurrentSession().save(module);
	}

    public void delete(Module module){
      sessionFactory.getCurrentSession().delete(module);
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
		if(!list.isEmpty()){
			idNode = list.get(0).getIdNode();
		}
    	return idNode;
    }

}
