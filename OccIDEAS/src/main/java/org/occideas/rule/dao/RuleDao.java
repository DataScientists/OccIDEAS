package org.occideas.rule.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RuleDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Long save(Rule rule){		
		return (Long) sessionFactory.getCurrentSession().save(rule);
    }

    public void delete(Rule rule){
    	rule.setDeleted(1);
    	sessionFactory.getCurrentSession().saveOrUpdate(rule);
    }

	public Rule get(Long id){
		return (Rule) sessionFactory.getCurrentSession().get(Rule.class, id);
    }

	public Rule merge(Rule rule)   {
		return (Rule) sessionFactory.getCurrentSession().merge(rule);
    }

    public void saveOrUpdate(Rule rule){
    	sessionFactory.getCurrentSession().saveOrUpdate(rule);
    }
    
   	@SuppressWarnings("unchecked")
	public List<Rule> getAll() {
         final Session session = sessionFactory.getCurrentSession();
         final Criteria crit = session.createCriteria(Rule.class)
        		 					.add(Restrictions.eq("deleted", 0))     		  						
       		  						.setResultTransformer(Transformers.aliasToBean(Rule.class));
         return crit.list();
       }

}
