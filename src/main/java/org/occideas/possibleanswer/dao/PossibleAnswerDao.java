package org.occideas.possibleanswer.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.base.dao.BaseDao;
import org.occideas.entity.PossibleAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PossibleAnswerDao implements IPossibleAnswerDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Autowired
  private BaseDao baseDao;

  @Override
  public PossibleAnswer get(long id) {
    return baseDao.get(PossibleAnswer.class, id);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveOrUpdate(PossibleAnswer answer) {
    sessionFactory.getCurrentSession().saveOrUpdate(answer);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveOrUpdateIgnoreFK(PossibleAnswer answer) {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    sessionFactory.getCurrentSession().saveOrUpdate(answer);
  }

  @Override
  public PossibleAnswer findByTopNodeIdAndNumber(long moduleId, String answerNumber) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(PossibleAnswer.class)
            .add(Restrictions.eq("topNodeId", moduleId))
            .add(Restrictions.eq("number",answerNumber))
            .add(Restrictions.eq("deleted", 0));
    final List<PossibleAnswer> list = crit.list();
    if(list.isEmpty()){
      return null;
    }else {
      return list.get(0);
    }
  }


}
