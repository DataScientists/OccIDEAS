package org.occideas.module.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ModuleDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public Module save(Module module){
      return (Module) sessionFactory.getCurrentSession().save(module);
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
        		 					.add(Restrictions.eq("deleted", 0))
       		  						.setProjection(Projections.projectionList()
       		  						.add(Projections.property("idNode"),"idNode")
       		  						.add(Projections.property("type"),"type")
       		  						.add(Projections.property("name"),"name")
       		  						.add(Projections.property("description"),"description"))
       		  						.setResultTransformer(Transformers.aliasToBean(Module.class));
         return crit.list();
       }

}
