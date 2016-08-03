package org.occideas.security.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.occideas.security.model.UserUserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserUserProfileDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void save(UserUserProfile profile) {
		final Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(profile);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
