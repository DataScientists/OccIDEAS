package org.occideas.language.dao;

import org.hibernate.SessionFactory;
import org.occideas.entity.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class LanguageDao implements ILanguageDao {
  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public void batchSave(List<Language> languages) {
    for (Language language : languages) {
      sessionFactory.getCurrentSession().saveOrUpdate(language);
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteAll() {
    sessionFactory.getCurrentSession().createSQLQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();
    sessionFactory.getCurrentSession().createSQLQuery("truncate table Language").executeUpdate();
    sessionFactory.getCurrentSession().createSQLQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();
  }
}
