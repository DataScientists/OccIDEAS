package org.occideas.qsf.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.entity.*;
import org.occideas.fragment.dao.FragmentDao;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.interviewanswer.dao.InterviewAnswerDao;
import org.occideas.interviewquestion.dao.InterviewQuestionDao;
import org.occideas.module.dao.ModuleDao;
import org.occideas.node.dao.NodeDao;
import org.occideas.participant.dao.ParticipantDao;
import org.occideas.possibleanswer.dao.PossibleAnswerDao;
import org.occideas.qsf.QSFNodeTypeMapper;
import org.occideas.question.dao.QuestionDao;
import org.occideas.utilities.QualtricsUtil;
import org.occideas.vo.ResponseSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Transactional
@Service
public class QSFInterviewReplicationService {

    private final Logger log = LogManager.getLogger(this.getClass());

    private final ParticipantDao participantDao;
    private final InterviewDao interviewDao;
    private final InterviewQuestionDao interviewQuestionDao;
    private final ModuleDao moduleDao;
    private final FragmentDao fragmentDao;
    private final QuestionDao questionDao;
    private final PossibleAnswerDao possibleAnswerDao;
    private final InterviewAnswerDao interviewAnswerDao;
    private final NodeDao nodeDao;

    public QSFInterviewReplicationService(ParticipantDao participantDao, InterviewDao interviewDao, InterviewQuestionDao interviewQuestionDao, ModuleDao moduleDao, FragmentDao fragmentDao, QuestionDao questionDao, PossibleAnswerDao possibleAnswerDao, InterviewAnswerDao interviewAnswerDao, NodeDao nodeDao) {
        this.participantDao = participantDao;
        this.interviewDao = interviewDao;
        this.interviewQuestionDao = interviewQuestionDao;
        this.moduleDao = moduleDao;
        this.fragmentDao = fragmentDao;
        this.questionDao = questionDao;
        this.possibleAnswerDao = possibleAnswerDao;
        this.interviewAnswerDao = interviewAnswerDao;
        this.nodeDao = nodeDao;
    }

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public long replicateQualtricsInterviewIntoOccideas(String respondentId, String reference,
                                                        String moduleName, Map<String, ResponseSummary> responseSummary) {
		
		List<JobModule> modules = moduleDao.findByName(moduleName);
		ResponseSummary rs = responseSummary.get("AMRID");
        Participant participant = new Participant();
		if(rs != null){
            participant = participantDao.getByReferenceNumber(rs.getAnswer());
        }else{
            participant = participantDao.getByReferenceNumber(reference);
        }

        long idInterview = -1;
        if(participant!=null){
            log.info("Participant found - {}", participant.getIdParticipant());

            Interview newInterview = interviewDao.findByReferenceNumber(rs.getAnswer()).get(0);
            log.info("Interview found - {}", newInterview.getIdinterview());
            JobModule module = modules.get(0);

            processResponseAnswers(responseSummary, newInterview);
            idInterview = newInterview.getIdinterview();
        }


		return idInterview;
		
	}

    private void processResponseAnswers(Map<String, ResponseSummary> responseSummary,
                                        Interview newInterview) {
        AtomicInteger questionCounter = new AtomicInteger(1);

//        Question topQuestion = questionDao.get(Long.parseLong(responseSummary.get("QID1").getQuestionIdNode()));
//        createInterviewQuestionFromQuestion(topQuestion, newInterview.getIdinterview(), questionCounter, responseSummary);

        AtomicInteger newQuestionCounter = new AtomicInteger();
        Set<Long> uniqueModules = new HashSet<>();
        responseSummary.forEach((key, value) -> handleSimpleAnswer(newInterview, newQuestionCounter, value, uniqueModules, responseSummary));
    }

    private void handleSimpleAnswer(Interview newInterview, AtomicInteger questionCounter, ResponseSummary responseSummary, Set<Long> uniqueModules, Map<String, ResponseSummary> responses) {
        List<Long> answers = QualtricsUtil.parseAnswers(responseSummary.getAnswerIdNode());
        for (Long occideasAnswer : answers) {
            PossibleAnswer possibleAnswer = possibleAnswerDao.get(occideasAnswer);
            Node node = nodeDao.getNode(possibleAnswer.getTopNodeId());
            if (Objects.nonNull(node)) {
                if (!uniqueModules.contains(node.getIdNode()) && node.getNodeclass().equals("M")) {
                    interviewQuestionDao.saveOrUpdate(createInterviewModuleQuestion(possibleAnswer, node, newInterview, questionCounter.incrementAndGet()));
                    
                    uniqueModules.add(node.getIdNode());
                } else if (!uniqueModules.contains(node.getIdNode()) && node.getNodeclass().equals("F")) {
                    final Fragment linkedModule = fragmentDao.get(node.getIdNode());
                    if (Objects.nonNull(linkedModule)) {
                        if (!uniqueModules.contains(linkedModule.getIdNode())) {
                            interviewQuestionDao.saveOrUpdate(createInterviewAJSMQuestion(
                                    node,
                                    linkedModule,
                                    newInterview.getIdinterview(),
                                    questionCounter.incrementAndGet()));
                            uniqueModules.add(linkedModule.getIdNode());
                        }
                    } else {
                        log.error("Cant find linked module {}", node.getIdNode());
                    }
                }


                List<Question> childNodes = possibleAnswer.getChildNodes();
                final Set<Long> linkModules = childNodes.stream().map(Question::getLink).filter(link -> link > 0L).collect(Collectors.toSet());

                if (!linkModules.isEmpty()) {
                    linkModules.stream()
                            .forEach(idNode -> {
                                final Fragment linkedModule = fragmentDao.get(idNode);
                                if (Objects.nonNull(linkedModule)) {
                                    if (!uniqueModules.contains(linkedModule.getIdNode())) {
                                        interviewQuestionDao.save(createInterviewAJSMQuestion(
                                                node,
                                                linkedModule,
                                                newInterview.getIdinterview(),
                                                questionCounter.incrementAndGet()));
                                        uniqueModules.add(linkedModule.getIdNode());
                                    }
                                } else {
                                    log.warn("Cant find linked module {}", idNode);
                                }
                            });
                }

                Question question = questionDao.get(Long.parseLong(possibleAnswer.getParentId()));
                if (Objects.nonNull(question)) {
                    InterviewQuestion interviewQuestion =
                            interviewQuestionDao.saveOrUpdate(createInterviewQuestion(question,
                                    newInterview.getIdinterview(),
                                    questionCounter.incrementAndGet()));
                    String answer = findAnswer(responses, possibleAnswer);
                    interviewAnswerDao.saveOrUpdate(createInterviewAnswer(
                            newInterview.getIdinterview(),
                            possibleAnswer,
                            interviewQuestion.getId(),
                            answer));
                } else {
                    log.error("Unable to find question in survey {} with answer id {} and parent id node {}",
                            possibleAnswer.getIdNode(), newInterview.getReferenceNumber(), possibleAnswer.getParentId());
                }
            }
        }
    }

    private void createInterviewQuestionFromQuestion(Question question, long interviewId, AtomicInteger questionCounter, Map<String, ResponseSummary> responses) {
        InterviewQuestion interviewQuestion =
                interviewQuestionDao.saveOrUpdate(createInterviewQuestion(question,
                        interviewId,
                        questionCounter.incrementAndGet()));
        List<PossibleAnswer> childNodes = question.getChildNodes();
        if (!childNodes.isEmpty()) {
            final PossibleAnswer possibleAnswer = childNodes.get(0);
            String answer = findAnswer(responses, possibleAnswer);
            interviewAnswerDao.saveOrUpdate(createInterviewAnswer(
                    interviewId,
                    possibleAnswer,
                    interviewQuestion.getId(),
                    answer));
            if (!possibleAnswer.getChildNodes().isEmpty()) {
                createInterviewQuestionFromQuestion(possibleAnswer.getChildNodes().get(0), interviewId, questionCounter, responses);
            }
        }

    }

    private String findAnswer(Map<String, ResponseSummary> responses, PossibleAnswer possibleAnswer) {
        String answer = possibleAnswer.getName();
        Optional<Map.Entry<String, ResponseSummary>> foundAnswer = responses.entrySet().stream()
                .filter(response -> QualtricsUtil.parseAnswers(response.getValue().getAnswerIdNode()).get(0).equals(possibleAnswer.getIdNode()))
                .findFirst();
        if (foundAnswer.isPresent()) {
            answer = foundAnswer.get().getValue().getAnswer();
        }
        return answer;
    }

    private InterviewAnswer createInterviewAnswer(long interviewId, PossibleAnswer possibleAnswer, long interviewQID, String answer) {
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
        interviewAnswer.setParentQuestionId(Optional.of(Long.valueOf(possibleAnswer.getParentId())).orElse(0L));
        interviewAnswer.setTopNodeId(possibleAnswer.getTopNodeId());
        interviewAnswer.setType(possibleAnswer.getType());
        interviewAnswer.setDescription(possibleAnswer.getDescription());
        interviewAnswer.setAnswerId(possibleAnswer.getIdNode());
        return interviewAnswer;
    }

    private void createRootInterviewQuestion(Interview newInterview, JobModule module) {
        InterviewQuestion rootQuestion = new InterviewQuestion();
        rootQuestion.setDeleted(0);
        rootQuestion.setDescription(module.getDescription());
        rootQuestion.setIdInterview(newInterview.getIdinterview());
        rootQuestion.setIntQuestionSequence(1);
        rootQuestion.setLink(77709);
        rootQuestion.setName(module.getName());
        rootQuestion.setNodeClass(module.getNodeclass());
        rootQuestion.setNumber("1");
        rootQuestion.setParentModuleId(0);
        rootQuestion.setParentAnswerId(77706);
        rootQuestion.setProcessed(true);
        rootQuestion.setTopNodeId(77704);
        rootQuestion.setQuestionId(module.getIdNode());
        rootQuestion.setType("M_IntroModule");

        interviewQuestionDao.saveOrUpdate(rootQuestion);
        /*
        InterviewQuestion rootQuestion2 = new InterviewQuestion();
        rootQuestion2.setDeleted(0);
        rootQuestion2.setDescription("Which Module was given?");
        rootQuestion2.setIdInterview(newInterview.getIdinterview());
        rootQuestion2.setIntQuestionSequence(2);
        rootQuestion2.setLink(module.getIdNode());
        rootQuestion2.setName(module.getName());
        rootQuestion2.setNodeClass(module.getNodeclass());
        rootQuestion2.setNumber("1");
        rootQuestion2.setParentModuleId(0);
        rootQuestion2.setProcessed(true);
        rootQuestion2.setTopNodeId(module.getIdNode());
        rootQuestion2.setType("M_IntroModule");

        interviewQuestionDao.saveOrUpdate(rootQuestion);
        */
    }

    private InterviewQuestion createInterviewQuestion(Node node, long interviewId, int sequence) {
        InterviewQuestion interviewQuestion = new InterviewQuestion();
        interviewQuestion.setDeleted(0);
        interviewQuestion.setDescription(node.getDescription());
        interviewQuestion.setIdInterview(interviewId);
        interviewQuestion.setIntQuestionSequence(sequence);
        interviewQuestion.setLink(node.getLink());
        interviewQuestion.setName(node.getName());
        interviewQuestion.setNodeClass(node.getNodeclass());
        interviewQuestion.setNumber(node.getNumber());
        interviewQuestion.setQuestionId(node.getIdNode());
        interviewQuestion.setModCount(1);
        interviewQuestion.setType(node.getType());
        interviewQuestion.setParentModuleId(node.getTopNodeId());
        if (node.getTopNodeId() != Long.parseLong(node.getParentId())) {
            interviewQuestion.setParentAnswerId(Long.parseLong(Optional.ofNullable(node.getParentId()).orElse("0")));
        }
        interviewQuestion.setProcessed(true);
        interviewQuestion.setTopNodeId(node.getTopNodeId());
        return interviewQuestion;
    }

    private InterviewQuestion createInterviewModuleQuestion(Node node, Node linkedModule, Interview newInterview, int sequence) {
        InterviewQuestion interviewQuestion = new InterviewQuestion(); 
        interviewQuestion.setDeleted(0);
        interviewQuestion.setDescription(linkedModule.getDescription());
        interviewQuestion.setIdInterview(newInterview.getIdinterview());
        interviewQuestion.setIntQuestionSequence(sequence);
        interviewQuestion.setLink(linkedModule.getIdNode());
        interviewQuestion.setName(linkedModule.getName());
        interviewQuestion.setNodeClass(null);
        interviewQuestion.setNumber(node.getNumber());
        interviewQuestion.setQuestionId(0);
        interviewQuestion.setModCount(1);
        // interviewQuestion.setType(node.getType());
        interviewQuestion.setParentModuleId(0L);
        if (node.getTopNodeId() != Long.parseLong(node.getParentId())) {
            interviewQuestion.setParentAnswerId(Optional.ofNullable(node.getIdNode()).orElse(0L));
        }
        interviewQuestion.setProcessed(true);
        interviewQuestion.setTopNodeId(linkedModule.getIdNode());
        interviewQuestion.setType(QSFNodeTypeMapper.Q_LINKEDMODULE.getDescription());
        return interviewQuestion;
    }

    private InterviewQuestion createInterviewAJSMQuestion(Node module,
                                                          Node linkedAJSM, long interviewId, int sequence) {
        InterviewQuestion interviewQuestion = new InterviewQuestion();
        interviewQuestion.setDeleted(0);
        interviewQuestion.setDescription(linkedAJSM.getDescription());
        interviewQuestion.setIdInterview(interviewId);
        interviewQuestion.setIntQuestionSequence(sequence);
        interviewQuestion.setLink(linkedAJSM.getIdNode());
        interviewQuestion.setName(linkedAJSM.getName());
        interviewQuestion.setNodeClass(null);
        interviewQuestion.setNumber("1");
        interviewQuestion.setQuestionId(0);
        interviewQuestion.setModCount(1);
        interviewQuestion.setParentModuleId(module.getIdNode());
        interviewQuestion.setParentAnswerId(0L);
        interviewQuestion.setProcessed(true);
        interviewQuestion.setTopNodeId(linkedAJSM.getIdNode());
        interviewQuestion.setType(QSFNodeTypeMapper.Q_LINKEDAJSM.getDescription());
        return interviewQuestion;
    }

    public Interview createNewInterview(String referenceNumber, Participant participant) {
        Interview interview = new Interview();
        interview.setParticipant(participant);
        interview.setReferenceNumber(referenceNumber);
        interviewDao.save(interview);
        return interview;
    }

    public Participant createParticipant(String referenceNumber) {
        Participant part = new Participant();
        part.setReference(referenceNumber);
        part.setStatus(2);
        participantDao.save(part);
        return part;
    }

}
