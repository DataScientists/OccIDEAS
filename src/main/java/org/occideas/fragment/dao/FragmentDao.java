package org.occideas.fragment.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Fragment;
import org.occideas.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FragmentDao implements IFragmentDao {

  private final String FRAGMENT_PARENT =
    "select a.number, a.parent_idNode, " +
      " (select name from Node where idNode = a.topNodeId) as name," +
      " (select topNodeId from Node where idNode = a.parent_IdNode) as topNodeId," +
      " idNode" +
      " from Node a" +
      " where link = :link order by name";
  @Autowired
  private SessionFactory sessionFactory;

  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#save(org.occideas.entity.Fragment)
   */
  @Override
  public void save(Fragment fragment) {
    sessionFactory.getCurrentSession().persist(fragment);
  }

  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#findByName(java.lang.String)
   */
  @Override
  public List<Fragment> findByName(String name) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Fragment.class)
      .add(Restrictions.eq("name", name));
    return crit.list();
  }

  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#getLinkingNodeById(long)
   */
  @Override
  public List<Question> getLinkingNodeById(long idNode) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Question.class)
      .add(Restrictions.eq("link", idNode))
      .add(Restrictions.eq("deleted", 0));
    return crit.list();
  }


  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#delete(org.occideas.entity.Fragment)
   */
  @Override
  public void delete(Fragment fragment) {
    sessionFactory.getCurrentSession().delete(fragment);
  }

  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#get(java.lang.Long)
   */
  @Override
  public Fragment get(Long id) {
    return (Fragment) sessionFactory.getCurrentSession().get(Fragment.class, id);
  }

  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#merge(org.occideas.entity.Fragment)
   */
  @Override
  public Fragment merge(Fragment fragment) {
    return (Fragment) sessionFactory.getCurrentSession().merge(fragment);
  }

  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#saveOrUpdate(org.occideas.entity.Fragment)
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void saveOrUpdate(Fragment fragment) {
    sessionFactory.getCurrentSession().saveOrUpdate(fragment);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveOrUpdateIgnoreFK(Fragment fragment) {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    sessionFactory.getCurrentSession().saveOrUpdate(fragment);
  }

  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#getAll()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<Fragment> getAll() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Fragment.class);
    return crit.list();
  }

  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#getAllActive()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<Fragment> getAllActive() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Fragment.class)
      .addOrder(Order.asc("name"))
      .add(Restrictions.eq("deleted", 0))
      .setProjection(Projections.projectionList()
        .add(Projections.property("idNode"), "idNode")
        .add(Projections.property("type"), "type")
        .add(Projections.property("name"), "name")
        .add(Projections.property("description"), "description"))
      .setResultTransformer(Transformers.aliasToBean(Fragment.class));
    return crit.list();
  }

  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#getAll(boolean)
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<Fragment> getAll(boolean isIncludeChild) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Fragment.class);
    crit.add(Restrictions.eq("deleted", 0));
    crit.addOrder(Order.asc("name"));
    return crit.list();
  }

  /* (non-Javadoc)
   * @see org.occideas.fragment.dao.IFragmentDao#getFragmentParents(java.lang.Long)
   */
  @Override
  public List<Fragment> getFragmentParents(Long id) {

    final Session session = sessionFactory.getCurrentSession();
    Query sqlQuery = session.createSQLQuery(FRAGMENT_PARENT);
    sqlQuery.setParameter("link", id);

    List<Object[]> rows = sqlQuery.list();

    List<Fragment> fragments = new ArrayList();

    //Map manually
    for (Object[] row : rows) {
      Fragment fragment = new Fragment();
      fragment.setNumber((String) row[0]);

      if (((BigInteger) row[3]).intValue() != 0) {
        fragment.setIdNode(((BigInteger) row[3]).longValue());
      } else {
        fragment.setIdNode(((BigInteger) row[1]).longValue());
      }
      fragment.setName((String) row[2]);
      fragment.setOriginalId(((BigInteger) row[4]).longValue());
      fragments.add(fragment);
    }

    return fragments;
  }
}
