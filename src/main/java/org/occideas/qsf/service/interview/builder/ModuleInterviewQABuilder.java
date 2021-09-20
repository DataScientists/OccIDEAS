package org.occideas.qsf.service.interview.builder;

import org.occideas.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ModuleInterviewQABuilder extends InterviewQuestionAnswerBuilder {

    @Override
    protected InterviewQuestion createInterviewQuestion(Node node, Question question, long interviewId, int sequence) {
        InterviewQuestion interviewQuestion = new InterviewQuestion();
        interviewQuestion.setDeleted(0);
        interviewQuestion.setDescription(question.getDescription());
        interviewQuestion.setIdInterview(interviewId);
        interviewQuestion.setIntQuestionSequence(sequence);
        interviewQuestion.setLink(question.getLink());
        interviewQuestion.setName(question.getName());
        interviewQuestion.setNodeClass(question.getNodeclass());
        interviewQuestion.setNumber(question.getNumber());
        interviewQuestion.setQuestionId(question.getIdNode());
        interviewQuestion.setModCount(1);
        interviewQuestion.setType(question.getType());
        interviewQuestion.setParentModuleId(node.getIdNode());
        if (node.getIdNode() != Long.valueOf(question.getParentId())) {
            interviewQuestion.setParentAnswerId(Long.valueOf(Optional.ofNullable(question.getParentId()).orElse("0")));
        }
        interviewQuestion.setProcessed(true);
        interviewQuestion.setTopNodeId(node.getIdNode());
        return interviewQuestion;
    }

    @Override
    protected InterviewAnswer createInterviewAnswer(long interviewId, PossibleAnswer possibleAnswer, long interviewQID, String answer) {
        InterviewAnswer interviewAnswer = new InterviewAnswer();
        interviewAnswer.setName(possibleAnswer.getName());
        interviewAnswer.setProcessed(true);
        interviewAnswer.setAnswerFreetext(answer);
        interviewAnswer.setAnswerId(possibleAnswer.getIdNode());
        interviewAnswer.setDeleted(0);
        interviewAnswer.setModCount(1);
        interviewAnswer.setIdInterview(interviewId);
        interviewAnswer.setInterviewQuestionId(interviewQID);
        interviewAnswer.setNodeClass(possibleAnswer.getNodeclass());
        interviewAnswer.setNumber(possibleAnswer.getNumber());
        interviewAnswer.setParentQuestionId(Optional.ofNullable(Long.valueOf(possibleAnswer.getParentId())).orElse(0L));
        interviewAnswer.setTopNodeId(possibleAnswer.getTopNodeId());
        interviewAnswer.setType(possibleAnswer.getType());
        interviewAnswer.setDescription(possibleAnswer.getDescription());
        return interviewAnswer;
    }
}
