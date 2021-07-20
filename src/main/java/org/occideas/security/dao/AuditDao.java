package org.occideas.security.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.occideas.security.model.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class AuditDao implements IAuditDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public void save(AuditLog auditLog) {
    final Session session = sessionFactory.getCurrentSession();
    session.saveOrUpdate(auditLog);
  }


}
