package org.occideas.agent.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AgentDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Agent save(Agent module){
      return (Agent) sessionFactory.getCurrentSession().save(module);
    }


    public void delete(Agent module){
      sessionFactory.getCurrentSession().delete(module);
    }

	public Agent get(Long id){
      return (Agent) sessionFactory.getCurrentSession().get(Agent.class, id);
    }

	public Agent merge(Agent module)   {
      return (Agent) sessionFactory.getCurrentSession().merge(module);
    }

    public void saveOrUpdate(Agent module){
      sessionFactory.getCurrentSession().saveOrUpdate(module);
    }

    @SuppressWarnings("unchecked")
	public List<Agent> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(Agent.class)
    		  						.setProjection(Projections.projectionList()
    		  						.add(Projections.property("idAgent"),"idAgent")
    		  						.add(Projections.property("name"),"name")
    		  						.add(Projections.property("description"),"description"))
    		  						.setResultTransformer(Transformers.aliasToBean(Agent.class));
      return crit.list();
    }
    @SuppressWarnings("unchecked")
   	public List<Agent> getAllActive() {
         final Session session = sessionFactory.getCurrentSession();
         final Criteria crit = session.createCriteria(Agent.class)
        		 					.add(Restrictions.eq("deleted", 0))
       		  						.setProjection(Projections.projectionList()
       		  						.add(Projections.property("idAgent"),"idAgent")
       		  						.add(Projections.property("agentGroup"),"agentGroup")
       		  						.add(Projections.property("name"),"name")
       		  						.add(Projections.property("description"),"description"))
       		  						.setResultTransformer(Transformers.aliasToBean(Agent.class));
         return crit.list();
       }

}
