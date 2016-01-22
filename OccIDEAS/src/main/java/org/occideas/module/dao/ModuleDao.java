package org.occideas.module.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
      final Criteria crit = session.createCriteria(Module.class);
      return crit.list();
    }

}
