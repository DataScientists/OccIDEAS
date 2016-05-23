package org.occideas.interviewdisplay.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.InterviewDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class InterviewDisplayDao {

	@Autowired
	private SessionFactory sessionFactory;

	public InterviewDisplay save(InterviewDisplay id){
      return (InterviewDisplay) sessionFactory.getCurrentSession().save(id);
    }

    public void delete(InterviewDisplay id){
      sessionFactory.getCurrentSession().delete(id);
    }

	public List<InterviewDisplay> get(Long id){
		final Criteria crit = sessionFactory.getCurrentSession().createCriteria(InterviewDisplay.class)
					.add(Restrictions.eq("deleted", 0))
					.add(Restrictions.eq("idInterview", id));
		List<InterviewDisplay> list = crit.list();
      return list;
    }
	
	public InterviewDisplay merge(InterviewDisplay id)   {
      return (InterviewDisplay) sessionFactory.getCurrentSession().merge(id);
    }

    public InterviewDisplay saveOrUpdate(InterviewDisplay id){
      sessionFactory.getCurrentSession().saveOrUpdate(id);
      return id;
    }

    @SuppressWarnings("unchecked")
	public List<InterviewDisplay> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(InterviewDisplay.class);
      return crit.list();
    }
    @SuppressWarnings("unchecked")
    public List<InterviewDisplay> findByInterviewId(Long interviewId) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewDisplay.class);
        if (interviewId != null) {
            crit.add(Restrictions.eq("idinterview", interviewId));
            crit.add(Restrictions.eq("deleted", 0));
        }
        return crit.list();
    }
    
	
}
