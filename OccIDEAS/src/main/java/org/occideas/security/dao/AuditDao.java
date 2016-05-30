package org.occideas.security.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.occideas.security.model.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuditDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void save(AuditLog auditLog) {
		final Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(auditLog);
	}
	
	
}
