package org.occideas.rule.dao;

import org.hibernate.SessionFactory;
import org.occideas.entity.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RuleDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Rule save(Rule module){
      return (Rule) sessionFactory.getCurrentSession().save(module);
    }

    public void delete(Rule module){
      sessionFactory.getCurrentSession().delete(module);
    }

	public Rule get(Long id){
      return (Rule) sessionFactory.getCurrentSession().get(Rule.class, id);
    }

	public Rule merge(Rule module)   {
      return (Rule) sessionFactory.getCurrentSession().merge(module);
    }

    public void saveOrUpdate(Rule module){
      sessionFactory.getCurrentSession().saveOrUpdate(module);
    }
 
}
