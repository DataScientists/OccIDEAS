package org.occideas.noderule.dao;

import org.hibernate.SessionFactory;
import org.occideas.entity.NodeRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NodeRuleDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void saveOrUpdate(NodeRule nodeRule) {
		sessionFactory.getCurrentSession().saveOrUpdate(nodeRule);
	}
	
	public void save(NodeRule nodeRule){		
		sessionFactory.getCurrentSession().save(nodeRule);
    }

}
