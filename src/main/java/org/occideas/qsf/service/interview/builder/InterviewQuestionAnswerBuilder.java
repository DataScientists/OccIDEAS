package org.occideas.qsf.service.interview.builder;

import org.occideas.entity.*;
import org.occideas.interviewanswer.dao.InterviewAnswerDao;
import org.occideas.interviewquestion.dao.InterviewQuestionDao;
import org.occideas.possibleanswer.dao.PossibleAnswerDao;
import org.occideas.question.dao.QuestionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public abstract class InterviewQuestionAnswerBuilder {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private PossibleAnswerDao possibleAnswerDao;
    @Autowired
    private InterviewAnswerDao interviewAnswerDao;
    @Autowired
    private InterviewQuestionDao interviewQuestionDao;

    public void createInterviewQuestionAndAnswer(Node node,
                                                 long interviewId,
                                                 Question question,
                                                 PossibleAnswer possibleAnswer,
                                                 String answer,
                                                 Integer sequence) {
        InterviewQuestion interviewQuestion = interviewQuestionDao.saveOrUpdate(createInterviewQuestion(node, question, interviewId, sequence));
        interviewAnswerDao.saveOrUpdate(createInterviewAnswer(interviewId, possibleAnswer, interviewQuestion.getId(), answer));
    }

    protected abstract InterviewQuestion createInterviewQuestion(Node node, Question question, long interviewId, int sequence);

    protected abstract InterviewAnswer createInterviewAnswer(long interviewId, PossibleAnswer possibleAnswer, long interviewQID, String answer);
}
