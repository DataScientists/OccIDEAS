package org.occideas.interviewquestionanswer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.InterviewQuestionAnswer;
import org.occideas.entity.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class InterviewQuestionAnswerDao {

	@Autowired
	private SessionFactory sessionFactory;

	public InterviewQuestionAnswer save(InterviewQuestionAnswer iqa){
      return (InterviewQuestionAnswer) sessionFactory.getCurrentSession().save(iqa);
    }

    public void delete(InterviewQuestionAnswer iqa){
      sessionFactory.getCurrentSession().delete(iqa);
    }

	public InterviewQuestionAnswer get(Long id){
      return (InterviewQuestionAnswer) sessionFactory.getCurrentSession().get(InterviewQuestionAnswer.class, id);
    }
	
	public InterviewQuestionAnswer merge(InterviewQuestionAnswer iqa)   {
      return (InterviewQuestionAnswer) sessionFactory.getCurrentSession().merge(iqa);
    }

    public void saveOrUpdate(InterviewQuestionAnswer iqa){
      sessionFactory.getCurrentSession().saveOrUpdate(iqa);
    }

    @SuppressWarnings("unchecked")
	public List<InterviewQuestionAnswer> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(InterviewQuestionAnswer.class)
    		  						.setResultTransformer(Transformers.aliasToBean(InterviewQuestionAnswer.class));
      return crit.list();
    }
    @SuppressWarnings("unchecked")
	public List<InterviewQuestionAnswer> getAllActive() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestionAnswer.class)
				.add(Restrictions.eq("deleted", 0))
				.setResultTransformer(Transformers.aliasToBean(InterviewQuestionAnswer.class));
		return crit.list();
	}
    @SuppressWarnings("unchecked")
    public List<InterviewQuestionAnswer> findById(Long interviewId, Long questionId, Long answerId) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestionAnswer.class)
					.setResultTransformer(Transformers.aliasToBean(InterviewQuestionAnswer.class));
        if (interviewId != null) {
            crit.add(Restrictions.eq("idinterview", interviewId));
        }
        if (questionId != null) {
            crit.add(Restrictions.eq("question_idNode", questionId));
        }
        if (answerId != null) {
            crit.add(Restrictions.eq("answer_idNode", answerId));
        }
        return crit.list();
    }
}