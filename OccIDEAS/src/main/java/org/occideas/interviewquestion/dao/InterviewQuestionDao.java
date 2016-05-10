package org.occideas.interviewquestion.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.InterviewQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class InterviewQuestionDao {

	@Autowired
	private SessionFactory sessionFactory;

	public InterviewQuestion save(InterviewQuestion iq){
      return (InterviewQuestion) sessionFactory.getCurrentSession().save(iq);
    }

    public void delete(InterviewQuestion iq){
      sessionFactory.getCurrentSession().delete(iq);
    }

	public InterviewQuestion get(Long id){
		final Criteria crit = sessionFactory.getCurrentSession().createCriteria(InterviewQuestion.class)
					.add(Restrictions.eq("deleted", 0))
					.add(Restrictions.eq("interview_idinterview", id));
		List list = crit.list();
		if(list.isEmpty()){
			return new InterviewQuestion();
		}
      return (InterviewQuestion) list.get(0);
    }
	
	public InterviewQuestion merge(InterviewQuestion iq)   {
      return (InterviewQuestion) sessionFactory.getCurrentSession().merge(iq);
    }

    public InterviewQuestion saveOrUpdate(InterviewQuestion iq){
      sessionFactory.getCurrentSession().saveOrUpdate(iq);
      return iq;
    }

    @SuppressWarnings("unchecked")
	public List<InterviewQuestion> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(InterviewQuestion.class);
      return crit.list();
    }
    @SuppressWarnings("unchecked")
	public List<InterviewQuestion> getAllActive() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class)
				.add(Restrictions.eq("deleted", 0));
		return crit.list();
	}
    @SuppressWarnings("unchecked")
    public List<InterviewQuestion> findById(Long interviewId, Long questionId) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class)
					.setResultTransformer(Transformers.aliasToBean(InterviewQuestion.class));
        if (interviewId != null) {
            crit.add(Restrictions.eq("idinterview", interviewId));
        }
        if (questionId != null) {
            crit.add(Restrictions.eq("question_id", questionId));
        }
        return crit.list();
    }
    @SuppressWarnings("unchecked")
    public List<InterviewQuestion> findByInterviewId(Long interviewId) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class)
					.setResultTransformer(Transformers.aliasToBean(InterviewQuestion.class));
        if (interviewId != null) {
            crit.add(Restrictions.eq("idinterview", interviewId));
        }
        return crit.list();
    }

	public InterviewQuestion findIntQuestion(long idInterview, long questionId, Integer modCount) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class);
		crit.add(Restrictions.eq("idInterview", idInterview));
		crit.add(Restrictions.eq("questionId",questionId));
		crit.add(Restrictions.eq("modCount", modCount));
		List<InterviewQuestion> list = crit.list();
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);
	}
    
}
