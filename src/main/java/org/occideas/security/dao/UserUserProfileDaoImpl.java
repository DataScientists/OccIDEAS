package org.occideas.security.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.security.model.UserUserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.List;

@Repository
public class UserUserProfileDaoImpl implements UserUserProfileDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public void save(UserUserProfile profile) {
    final Session session = sessionFactory.getCurrentSession();
    session.saveOrUpdate(profile);
  }

  public void delete(int userId) {
    final Session session = sessionFactory.getCurrentSession();
    List<UserUserProfile> userUserProfileList = (List<UserUserProfile>) session.createCriteria(UserUserProfile.class)
      .add(Restrictions.eq("userId", userId)).list();
    for (UserUserProfile userUserProfile : userUserProfileList) {
      session.delete(userUserProfile);
    }
    session.flush();
  }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

}
