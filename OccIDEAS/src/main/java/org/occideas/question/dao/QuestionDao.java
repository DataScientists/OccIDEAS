package org.occideas.question.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.Module;
import org.occideas.entity.Question;
import org.occideas.vo.InterviewQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("QuestionDao")
public class QuestionDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	public Question getQuestionByModuleIdAndNumber(String parentId,String number){
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(Question.class)
				.add(Restrictions.eq("parentId",parentId))
				.add(Restrictions.eq("number",number))
						.add(Restrictions.eq("deleted",0));
		if(!crit.list().isEmpty()){
			return (Question) crit.list().get(0);
		}
		return null;
		
	}
	public List<Question> getQuestionsByParentId(String parentId){
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(Question.class)
				.add(Restrictions.eq("parentId",parentId))
				.add(Restrictions.eq("deleted",0));
		if(!crit.list().isEmpty()){
			return crit.list();
		}
		return null;	
	}

	public Module getModuleByParentId(Long idNode) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(Module.class)
						.add(Restrictions.eq("idNode",idNode))
						.add(Restrictions.eq("deleted",0));
		if(!crit.list().isEmpty()){
			return (Module) crit.list().get(0);
		}
		return null;
	}
	
}
