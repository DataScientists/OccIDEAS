package org.occideas.interviewdisplay.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.occideas.entity.InterviewDisplayAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewDisplayAnswerDao {

	@Autowired
	private SessionFactory sessionFactory;


    public InterviewDisplayAnswer saveOrUpdate(InterviewDisplayAnswer id){
      sessionFactory.getCurrentSession().saveOrUpdate(id);
      return id;
    }

    @SuppressWarnings("unchecked")
	public List<InterviewDisplayAnswer> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(InterviewDisplayAnswer.class);
      return crit.list();
    }
    @SuppressWarnings("unchecked")
    public List<InterviewDisplayAnswer> findByInterviewId(Long interviewId) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewDisplayAnswer.class);
        if (interviewId != null) {
//            crit.add(Restrictions.eq("idinterview", interviewId));
//            crit.add(Restrictions.eq("deleted", 0));
        }
        return crit.list();
    }

	public List<InterviewDisplayAnswer> updateList(List<InterviewDisplayAnswer> list) {
		List<InterviewDisplayAnswer> result = new ArrayList<>();
		for(InterviewDisplayAnswer intDisplay:list){
			sessionFactory.getCurrentSession().saveOrUpdate(intDisplay);
			result.add(intDisplay);
		}
		return result;
	}
    
	
}
