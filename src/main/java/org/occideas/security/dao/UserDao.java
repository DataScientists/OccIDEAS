package org.occideas.security.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

  @Autowired
  private SessionFactory sessionFactory;

  public User save(User user) {
    final Session session = sessionFactory.getCurrentSession();
    session.saveOrUpdate(user);
    return user;
  }

  public User findById(long id) {
    final Session session = sessionFactory.getCurrentSession();
    return (User) session.get(User.class, id);
  }

  public User findBySSO(String sso) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(User.class);
    crit.add(Restrictions.eq("ssoId", sso));
    return (User) crit.uniqueResult();
  }

  public List<User> findAll() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(User.class);
    return crit.list();
  }

}
