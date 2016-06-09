package org.occideas.interviewanswer.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewQuestion;
import org.occideas.possibleanswer.service.PossibleAnswerService;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewAnswerDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
    private PossibleAnswerService possibleAnswerService;
	
	public List<InterviewAnswer> saveOrUpdate(List<InterviewAnswer> ia) {
		List<InterviewAnswer> list = new ArrayList<>();
		for(InterviewAnswer a:ia){
			sessionFactory.getCurrentSession().saveOrUpdate(a);
			list.add(a);
		}
		return list;
	}
	public List<InterviewAnswer> saveAnswerAndQueueQuestions(List<InterviewAnswer> ia) {
		List<InterviewAnswer> list = new ArrayList<>();
		for(InterviewAnswer a:ia){
			sessionFactory.getCurrentSession().saveOrUpdate(a);
			if(a.getDeleted()==0){
				for(PossibleAnswerVO pa :possibleAnswerService.findByIdWithChildren(a.getAnswerId())){			
					int intQuestionSequence = 1;
					List<QuestionVO> queueQuestions = pa.getChildNodes();
			        Collections.sort(queueQuestions); 
					for(QuestionVO question: queueQuestions){
						InterviewQuestion iq = new InterviewQuestion();
						iq.setIdInterview(a.getIdInterview());
						iq.setName(question.getName());
						iq.setNodeClass(question.getNodeclass());
						iq.setNumber(question.getNumber());
						iq.setModCount(a.getModCount());
						iq.setLink(question.getLink());
						iq.setParentAnswerId(a.getAnswerId());
						iq.setQuestionId(question.getIdNode());
						iq.setDescription(question.getDescription());
						iq.setTopNodeId(a.getTopNodeId());
						iq.setIntQuestionSequence(intQuestionSequence);
						iq.setDeleted(0);
						intQuestionSequence++;
						sessionFactory.getCurrentSession().saveOrUpdate(iq);
					}	
				}		
			}
			list.add(a);
		}
		return list;
	}
}
