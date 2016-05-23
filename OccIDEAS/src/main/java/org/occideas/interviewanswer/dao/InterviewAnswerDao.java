package org.occideas.interviewanswer.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.occideas.entity.InterviewAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class InterviewAnswerDao {

	@Autowired
	private SessionFactory sessionFactory;

	public List<InterviewAnswer> saveOrUpdate(List<InterviewAnswer> ia) {
		List<InterviewAnswer> list = new ArrayList<>();
		for(InterviewAnswer a:ia){
			sessionFactory.getCurrentSession().saveOrUpdate(a);
			list.add(a);
		}
		return list;
	}

}
