package org.occideas.genericnode.dao;

import org.hibernate.SessionFactory;
import org.occideas.entity.GenericNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GenericNodeDao {

	@Autowired
	private SessionFactory sessionFactory;

	public GenericNode get(Long id) {
		return (GenericNode) sessionFactory.getCurrentSession().get(GenericNode.class, id);
	}
}
