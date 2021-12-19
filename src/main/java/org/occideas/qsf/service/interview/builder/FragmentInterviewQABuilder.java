package org.occideas.qsf.service.interview.builder;

import org.occideas.entity.*;
import org.occideas.qsf.QSFNodeTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class FragmentInterviewQABuilder extends InterviewQuestionAnswerBuilder {

    @Override
    protected InterviewQuestion createInterviewQuestion(Node node, Question question, long interviewId, int sequence) {
        InterviewQuestion interviewQuestion = new InterviewQuestion();
        interviewQuestion.setDeleted(0);
        interviewQuestion.setDescription(node.getDescription());
        interviewQuestion.setIdInterview(interviewId);
        interviewQuestion.setIntQuestionSequence(sequence);
        interviewQuestion.setLink(node.getIdNode());
        interviewQuestion.setName(node.getName());
        interviewQuestion.setNodeClass(null);
        interviewQuestion.setNumber("1");
        interviewQuestion.setQuestionId(0);
        interviewQuestion.setModCount(1);
        interviewQuestion.setParentModuleId(node.getIdNode());
        interviewQuestion.setParentAnswerId(0L);
        interviewQuestion.setProcessed(true);
        interviewQuestion.setTopNodeId(node.getIdNode());
        interviewQuestion.setType(QSFNodeTypeMapper.Q_LINKEDAJSM.getDescription());
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
