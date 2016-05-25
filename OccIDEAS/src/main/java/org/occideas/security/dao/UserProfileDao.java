package org.occideas.security.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.occideas.security.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
    public List<UserProfile> findAll(){
		final Criteria crit = createEntityCriteria();
        crit.addOrder(Order.asc("type"));
        return (List<UserProfile>)crit.list();
    }

	private Criteria createEntityCriteria() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(UserProfile.class);
		return crit;
	}
     
    public UserProfile findById(long id) {
    	final Session session = sessionFactory.getCurrentSession();
        return (UserProfile)session.get(UserProfile.class,id);
    }
     
    public UserProfile findByType(String type) {
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("type", type));
        return (UserProfile) crit.uniqueResult();
    }
	
}
