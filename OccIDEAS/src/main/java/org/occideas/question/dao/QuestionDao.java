package org.occideas.question.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.Module;
import org.occideas.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("QuestionDao")
public class QuestionDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	public Question getQuestionByModuleIdAndNumber(String moduleId,String number){
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(Question.class)
						.add(Restrictions.eq("parentId",moduleId))
						.add(Restrictions.eq("number",number))
						.add(Restrictions.eq("deleted",0));
		if(!crit.list().isEmpty()){
			return (Question) crit.list().get(0);
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
