package org.occideas.nodelanguage.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.NodeLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NodeLanguageDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void save(NodeLanguage entity){
       sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public void delete(NodeLanguage entity){
      sessionFactory.getCurrentSession().delete(entity);
    }

	public NodeLanguage get(Long id){
      return (NodeLanguage) sessionFactory.getCurrentSession().get(NodeLanguage.class, id);
    }
	
	public List<NodeLanguage> getNodesByLanguage(String language) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(NodeLanguage.class)
      		  						.add(Restrictions.eq("language",language));
        return crit.list();
	}
	
}
