package org.occideas.interviewquestion.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

    public void saveOrUpdate(InterviewQuestion iq){
      sessionFactory.getCurrentSession().saveOrUpdate(iq);
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
    
}
