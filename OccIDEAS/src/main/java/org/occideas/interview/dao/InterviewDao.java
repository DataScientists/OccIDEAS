package org.occideas.interview.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.InterviewQuestionAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class InterviewDao {

    @Autowired
    private SessionFactory sessionFactory;

    public List<InterviewQuestionAnswer> findById(Long interviewId, Long questionId, Long answerId) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestionAnswer.class);
        if (interviewId != null) {
            crit.add(Restrictions.eq("interview.idinterview", interviewId));
        }
        if (questionId != null) {
            crit.add(Restrictions.eq("question.idNode", questionId));
        }
        if (answerId != null) {
            crit.add(Restrictions.eq("answer.idNode", answerId));
        }
        return crit.list();
    }
}
