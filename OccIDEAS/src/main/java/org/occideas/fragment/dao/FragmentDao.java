package org.occideas.fragment.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

@Repository
public class FragmentDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private final String FRAGMENT_PARENT = 
			"select a.number, a.parent_idNode as idNode, " +
			" (select name from Node where idNode = a.topNodeId) as name," +
			" (select topNodeId from Node where idNode = a.parent_IdNode) as topNodeId" +
			" from Node a"+
			" where link = :link order by name";
	
	public void save(Fragment fragment){
		sessionFactory.getCurrentSession().persist(fragment);
    }
	
	public List<Fragment> findByName(String name){
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(Fragment.class)
				.add(Restrictions.eq("name", name));
		return crit.list();
	}
	
	public List<Question> getLinkingNodeById(long idNode){
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(Question.class)
				.add(Restrictions.eq("link",idNode))
				.add(Restrictions.eq("deleted",0));
		return crit.list();
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
    
    /**
     * Get all with children nodes
     * @param isIncludeChild
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Fragment> getAll(boolean isIncludeChild) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(Fragment.class);
		crit.add(Restrictions.eq("deleted", 0));
		crit.addOrder(Order.asc("name"));
		return crit.list();
	}

	public List<Fragment> getFragmentParents(Long id) {
		
		final Session session = sessionFactory.getCurrentSession();
		Query sqlQuery = session.createSQLQuery(FRAGMENT_PARENT);
		sqlQuery.setParameter("link", id);
		
		List<Object[]> rows = sqlQuery.list();
		
		List<Fragment> fragments = new ArrayList();
		
		//Map manually
		for (Object[] row : rows) {
		    Fragment fragment = new Fragment();
		    fragment.setNumber((String)row[0]);
		    
		    if(((BigInteger)row[3]).intValue() != 0){
		    	 fragment.setIdNode(((BigInteger)row[3]).longValue());				   
		    }
		    else { 
		    	fragment.setIdNode(((BigInteger)row[1]).longValue());		    
		    }
		    fragment.setName((String)row[2]);
		    fragments.add(fragment);
		}		
		
		return fragments;
	}
}
