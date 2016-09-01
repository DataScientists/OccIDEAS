package org.occideas.fragment.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Fragment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FragmentDao {

	@Autowired
	private SessionFactory sessionFactory;

	
	public void save(Fragment fragment){
		sessionFactory.getCurrentSession().persist(fragment);
    }


    public void delete(Fragment fragment){
      sessionFactory.getCurrentSession().delete(fragment);
    }

	public Fragment get(Long id){
      return (Fragment) sessionFactory.getCurrentSession().get(Fragment.class, id);
    }

	public Fragment merge(Fragment fragment)   {
      return (Fragment) sessionFactory.getCurrentSession().merge(fragment);
    }

    public void saveOrUpdate(Fragment fragment){
      sessionFactory.getCurrentSession().saveOrUpdate(fragment);
    }

    @SuppressWarnings("unchecked")
	public List<Fragment> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(Fragment.class)
    		  						.setProjection(Projections.projectionList()
    		  						.add(Projections.property("idNode"),"idNode")
    		  						.add(Projections.property("name"),"name")
    		  						.add(Projections.property("description"),"description"))
    		  						.setResultTransformer(Transformers.aliasToBean(Fragment.class));
      return crit.list();
    }
    @SuppressWarnings("unchecked")
   	public List<Fragment> getAllActive() {
         final Session session = sessionFactory.getCurrentSession();
         final Criteria crit = session.createCriteria(Fragment.class)
        		 					.addOrder(Order.asc("name"))
        		 					.add(Restrictions.eq("deleted", 0))
       		  						.setProjection(Projections.projectionList()
       		  						.add(Projections.property("idNode"),"idNode")
       		  						.add(Projections.property("type"),"type")
       		  						.add(Projections.property("name"),"name")
       		  						.add(Projections.property("description"),"description"))
       		  						.setResultTransformer(Transformers.aliasToBean(Fragment.class));
         return crit.list();
       }

}
