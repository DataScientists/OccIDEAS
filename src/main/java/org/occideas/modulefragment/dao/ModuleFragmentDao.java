package org.occideas.modulefragment.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.ModuleFragment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ModuleFragmentDao implements IModuleFragmentDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public List<ModuleFragment> getModuleFragmentByModuleId(long moduleId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ModuleFragment.class)
      .add(Restrictions.eq("moduleId", moduleId));
    return crit.list();
  }

}
