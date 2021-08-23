package org.occideas.ipsos.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.common.NodeType;
import org.occideas.entity.Constant;
import org.occideas.exceptions.StudyIntroModuleNotFoundException;
import org.occideas.fragment.service.FragmentService;
import org.occideas.interview.service.InterviewService;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.module.service.ModuleService;
import org.occideas.node.service.INodeService;
import org.occideas.participant.service.ParticipantService;
import org.occideas.possibleanswer.service.PossibleAnswerService;
import org.occideas.question.service.QuestionService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.CsvUtil;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class IPSOSService implements IIPSOSService {

    private static final Logger log = LogManager.getLogger(IPSOSService.class);

    private static final String FREETEXT = "FREETEXT";
    private static final String RADIO = "RADIO";
    private static final String CHECK = "CHECK";

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private INodeService nodeService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private InterviewQuestionService interviewQuestionService;

    @Autowired
    private InterviewAnswerService interviewAnswerService;

    @Autowired
    private FragmentService fragmentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PossibleAnswerService possibleAnswerService;

    @Autowired
    private SystemPropertyService systemPropertyService;

    @Value("${ipsos.input.path}")
    private String input;

    @Value("#{'${ipsos.non.question.labels}'.split(',')}")
    private List<String> nonQuestionLabels;

    private String getNodeKey(String nodeName) {
        return nodeName.substring(0, 4);
    }

    private String getInterviewUniqueKey(Long idNode, Long interviewId) {
        return idNode + "_" + interviewId;
    }

    @Override
    public void importResponse() throws IOException {
        log.info("initiating ipsos responses");
        processCSV();
        log.debug("done importing ipsos responses");
    }

    private void processCSV() throws IOException {
        List<File> files = Files.list(Paths.get(input)).map(Path::toFile).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(files)) {
            SystemPropertyVO introModule = Optional.ofNullable(systemPropertyService.getByName(Constant.STUDY_INTRO))
                    .orElseThrow(StudyIntroModuleNotFoundException::new);
            NodeVO node = moduleService.getNodeById(Long.valueOf(introModule.getValue()));
            files.forEach(file -> {
                try {
                    Map<String, Map<String, String>> responses = convertToObject(file);
                    if (!CollectionUtils.isEmpty(responses)) processResponses(node, file.getName(), responses);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("Unable to read file ", e.getMessage());
                }
            });
        }
    }

    Map<String, Object> uniqueModules = null;

    private void processResponses(NodeVO node, String fileName, Map<String, Map<String, String>> responses) throws IOException {
        String fileNameKey = getNodeKey(fileName.toUpperCase());
        ModuleVO module = moduleService.getModuleByNameLength(fileNameKey, 4);
        if (module == null) {
            log.info("Not able to find module for key={}", fileNameKey);
            return;
        }

        String moduleKey = getNodeKey(module.getName());
        responses.forEach((responseKey, answers) -> {
            String responseId = answers.get("RESPONSE_ID");
            uniqueModules = new HashMap<>();
            if (hasAnyAnswer(answers)) {
                if (participantService.getByReferenceNumber(responseId) == null) {
                    ParticipantVO participant = createParticipant(responseId);
                    InterviewVO interview = createInterview(participant);

                    String introModuleIUniqueKey = getInterviewUniqueKey(node.getIdNode(), interview.getInterviewId());
                    if (!uniqueModules.containsKey(introModuleIUniqueKey)) {
                        createIntroModuleQuestion(node, interview.getInterviewId());
                        uniqueModules.put(introModuleIUniqueKey, null);
                    }

                    processIntroModule(module, node, interview.getInterviewId());

                    String moduleIUniqueKey = getInterviewUniqueKey(module.getIdNode(), interview.getInterviewId());
                    if (!uniqueModules.containsKey(moduleIUniqueKey) && "M".equals(module.getNodeclass())) {
                        createModuleInterviewQuestion(node, module, interview.getInterviewId(), 1);
                        uniqueModules.put(moduleIUniqueKey, null);
                    }

                    AtomicInteger qCounter = new AtomicInteger(1);
                    answers.forEach((label, answer) -> {
                        if (!nonQuestionLabels.contains(label)) {
                            log.debug("participantId={}, label={}, answer={}", responseId, label, answer);
                            String[] labelKeys = label.split("_");
                            String nodeKey = labelKeys[0];
                            String qType = labelKeys[1];
                            String freetext = null;
                            String qNumber = null;
                            String aNumber = null;
                            boolean isJobModule = moduleKey.equals(nodeKey);
                            if (answer != null && !StringUtils.isBlank(answer)) {
                                if (RADIO.equals(qType)) {
                                    qNumber = isJobModule ? labelKeys[2] : labelKeys[3];
                                    if (label.endsWith(FREETEXT)) {
                                        freetext = answer;
                                        aNumber = labelKeys[3];
                                    } else {
                                        aNumber = answer.split("_")[1];
                                    }
                                } else if (CHECK.equals(qType)) {
                                    qNumber = isJobModule ? labelKeys[2] : labelKeys[3];
                                    aNumber = isJobModule ? labelKeys[3] : labelKeys[4];
                                    if (label.endsWith(FREETEXT)) {
                                        freetext = answer;
                                    }
                                }
                            }

                            if (qNumber != null && aNumber != null) {
                                if (isJobModule) {
                                    // job module questions
                                    createInterviewQuestionsAndAnswers(module.getIdNode(), interview.getInterviewId(),
                                            qNumber, aNumber, qCounter, freetext);
                                } else {
                                    // linked task module questions
                                    log.debug("task module processing nodeKey={}", nodeKey);
                                    String linkedNumber = labelKeys[2];
                                    QuestionVO linkAJSM = questionService.getQuestionByTopIdAndNumber(module.getIdNode(), linkedNumber);
                                    if (linkAJSM != null) {
                                        List<FragmentVO> fragments = fragmentService.findByIdForInterview(linkAJSM.getLink());
                                        if (!fragments.isEmpty()) {
                                            FragmentVO fragment = fragments.get(0);
                                            String fragmentIUniqueKey = getInterviewUniqueKey(fragment.getIdNode(), interview.getInterviewId());
                                            if (!uniqueModules.containsKey(fragmentIUniqueKey)) {
                                                createAJSMInterviewQuestion(linkAJSM, fragment, interview.getInterviewId(), qCounter.incrementAndGet());
                                                uniqueModules.put(fragmentIUniqueKey, null);
                                            }
                                            createInterviewQuestionsAndAnswers(fragment.getIdNode(), interview.getInterviewId(),
                                                    qNumber, aNumber, qCounter, freetext);
                                        }
                                    }
                                }
                            }
                        } else {
                            log.debug("Disregard label={}", label);
                        }
                    });
                } else {
                    log.debug("ParticipantId={} found. will not process", responseId);
                }
            }
        });
    }

    private boolean hasAnyAnswer(Map<String, String> answers) {
        return answers.entrySet().stream().anyMatch(entry -> entry.getValue() != null && !StringUtils.EMPTY.equals(entry.getValue()));
    }

    private Map<String, Map<String, String>> convertToObject(File file) throws IOException {
        List<String[]> extract = CsvUtil.readAll(file.getAbsolutePath(), ';');
        Map<String, Map<String, String>> formatted = new LinkedHashMap<>();
        String[] labels = extract.get(0);
        int index = 0;
        for (String[] data : extract) {
            if (index > 0) {
                Map<String, String> entry = new LinkedHashMap<>();
                int dataIndex = 0;
                for (String value : data) {
                    entry.put(labels[dataIndex], value);
                    dataIndex++;
                }
                formatted.put(data[0], entry);
            }
            index++;
        }
        log.debug("formatted={}", formatted);
        return formatted;
    }

    private ParticipantVO createParticipant(String reference) {
        ParticipantVO partVO = new ParticipantVO();
        partVO.setReference(reference);
        partVO.setStatus(2);
        return participantService.create(partVO);
    }

    private InterviewVO createInterview(ParticipantVO participant) {
        InterviewVO interview = new InterviewVO();
        interview.setParticipant(participant);
        interview.setReferenceNumber(participant.getReference());
        return interviewService.create(interview);
    }

    private InterviewQuestionVO createIntroModuleQuestion(NodeVO node, long interviewId) {
        InterviewQuestionVO interviewQuestion = new InterviewQuestionVO();
        interviewQuestion.setDeleted(0);
        interviewQuestion.setDescription(node.getDescription());
        interviewQuestion.setIdInterview(interviewId);
        interviewQuestion.setIntQuestionSequence(1);
        interviewQuestion.setLink(node.getIdNode());
        interviewQuestion.setName(node.getName());
        interviewQuestion.setNodeClass("M");
        interviewQuestion.setNumber("1");
        interviewQuestion.setParentModuleId(0);
        interviewQuestion.setProcessed(true);
        interviewQuestion.setTopNodeId(node.getIdNode());
        interviewQuestion.setType("M_IntroModule");
        return interviewQuestionService.updateIntQ(interviewQuestion);
    }

    private void processIntroModule(ModuleVO module, NodeVO node, long interviewId) {
        AtomicInteger introQCounter = new AtomicInteger(1);
        QuestionVO topQuestion = getParentQuestions(module.getIdNode(), node);
        createInterviewQuestionFromQuestion(topQuestion, interviewId, introQCounter);
    }

    private QuestionVO getParentQuestions(long moduleIdNode, NodeVO node) {
        QuestionVO linkQuestion = questionService
                .getNearestQuestionByLinkIdAndTopId(moduleIdNode, node.getIdNode());
        PossibleAnswerVO parentAnswer = possibleAnswerService.findByIdExcludeChildren(Long.valueOf(linkQuestion.getParentId()));
        QuestionVO parentQuestion = questionService.findByIdExcludeChildren(Long.valueOf(parentAnswer.getParentId()));
        parentQuestion.setChildNodes(Arrays.asList(parentAnswer));
        if (Long.valueOf(parentQuestion.getParentId()) != node.getIdNode()) {
            return getTopQuestion(node.getIdNode(), parentQuestion);
        } else {
            return parentQuestion;
        }
    }

    private QuestionVO getTopQuestion(long topNodeId, QuestionVO question) {
        final Long parentId = Long.valueOf(question.getParentId());
        if (topNodeId == parentId) {
            return question;
        }
        PossibleAnswerVO possibleAnswer = possibleAnswerService.findByIdExcludeChildren(parentId);
        possibleAnswer.setChildNodes(Arrays.asList(question));
        final long answerParentId = Long.valueOf(possibleAnswer.getParentId());
        QuestionVO parentQuestion = questionService.findByIdExcludeChildren(answerParentId);
        parentQuestion.setChildNodes(Arrays.asList(possibleAnswer));
        return getTopQuestion(topNodeId, parentQuestion);
    }

    private void createInterviewQuestionFromQuestion(QuestionVO question, long interviewId, AtomicInteger introQCounter) {
        InterviewQuestionVO interviewQuestion =
                interviewQuestionService.updateIntQ(createInterviewQuestion(question,
                        interviewId,
                        introQCounter.incrementAndGet()));
        final PossibleAnswerVO possibleAnswerVO = question.getChildNodes().get(0);
        interviewAnswerService.saveOrUpdate(createInterviewAnswer(null,
                interviewId,
                possibleAnswerVO,
                interviewQuestion.getId(), null));
        if (!possibleAnswerVO.getChildNodes().isEmpty()) {
            createInterviewQuestionFromQuestion(possibleAnswerVO.getChildNodes().get(0), interviewId, introQCounter);
        }
    }

    private InterviewQuestionVO createInterviewQuestion(NodeVO node, long interviewId, int sequence) {
        InterviewQuestionVO interviewQuestion = new InterviewQuestionVO();
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
        if (node.getTopNodeId() != Long.valueOf(node.getParentId())) {
            interviewQuestion.setParentAnswerId(Long.valueOf(Optional.ofNullable(node.getParentId()).orElse("0")));
        }
        interviewQuestion.setProcessed(true);
        interviewQuestion.setTopNodeId(node.getTopNodeId());
        return interviewQuestionService.updateIntQ(interviewQuestion);
    }

    private InterviewAnswerVO createInterviewAnswer(Long interviewAnswerId, long interviewId, PossibleAnswerVO possibleAnswerVO,
                                                    long interviewQID, String freetext) {
        InterviewAnswerVO interviewAnswer = new InterviewAnswerVO();
        if (interviewAnswerId != null && interviewAnswerId != 0L) {
            interviewAnswer.setId(interviewAnswerId);
        }
        interviewAnswer.setName(possibleAnswerVO.getName());
        interviewAnswer.setIsProcessed(true);
        interviewAnswer.setAnswerFreetext((freetext != null && !StringUtils.EMPTY.equals(freetext))
                ? freetext : possibleAnswerVO.getName());
        interviewAnswer.setAnswerId(possibleAnswerVO.getIdNode());
        interviewAnswer.setDeleted(0);
        interviewAnswer.setModCount(1);
        interviewAnswer.setIdInterview(interviewId);
        interviewAnswer.setInterviewQuestionId(interviewQID);
        interviewAnswer.setNodeClass(possibleAnswerVO.getNodeclass());
        interviewAnswer.setNumber(possibleAnswerVO.getNumber());
        interviewAnswer.setParentQuestionId(Optional.ofNullable(Long.valueOf(possibleAnswerVO.getParentId())).orElse(0L));
        interviewAnswer.setTopNodeId(possibleAnswerVO.getTopNodeId());
        interviewAnswer.setType(possibleAnswerVO.getType());
        interviewAnswer.setDescription(possibleAnswerVO.getDescription());
        interviewAnswer.setAnswerId(possibleAnswerVO.getIdNode());
        return interviewAnswerService.saveOrUpdate(interviewAnswer);
    }

    private InterviewQuestionVO createModuleInterviewQuestion(NodeVO node, NodeVO linkedModule, long interviewId, int sequence) {
        InterviewQuestionVO interviewQuestion = new InterviewQuestionVO();
        interviewQuestion.setDeleted(0);
        interviewQuestion.setDescription(linkedModule.getDescription());
        interviewQuestion.setIdInterview(interviewId);
        interviewQuestion.setIntQuestionSequence(sequence);
        interviewQuestion.setLink(linkedModule.getIdNode());
        interviewQuestion.setName(linkedModule.getName());
        interviewQuestion.setNodeClass(null);
        interviewQuestion.setNumber(node.getNumber());
        interviewQuestion.setQuestionId(0);
        interviewQuestion.setModCount(1);
        interviewQuestion.setParentModuleId(0L);
        if (node.getParentId() != null && !StringUtils.EMPTY.equals(node.getParentId())
                && node.getTopNodeId() != Long.valueOf(node.getParentId())) {
            interviewQuestion.setParentAnswerId(Optional.ofNullable(node.getIdNode()).orElse(0L));
        }
        interviewQuestion.setProcessed(true);
        interviewQuestion.setTopNodeId(linkedModule.getIdNode());
        interviewQuestion.setType(NodeType.Q_LINKEDMODULE.getDescription());
        return interviewQuestionService.updateIntQ(interviewQuestion);
    }

    private InterviewQuestionVO createAJSMInterviewQuestion(NodeVO parentModule, NodeVO linkAJSM,
                                                            long interviewId, int sequence) {
        InterviewQuestionVO interviewQuestion = new InterviewQuestionVO();
        interviewQuestion.setDeleted(0);
        interviewQuestion.setDescription(linkAJSM.getDescription());
        interviewQuestion.setIdInterview(interviewId);
        interviewQuestion.setIntQuestionSequence(sequence);
        interviewQuestion.setLink(linkAJSM.getIdNode());
        interviewQuestion.setName(linkAJSM.getName());
        interviewQuestion.setNodeClass(null);
        interviewQuestion.setNumber("1");
        interviewQuestion.setQuestionId(0);
        interviewQuestion.setModCount(1);
        interviewQuestion.setParentModuleId(parentModule.getIdNode());
        interviewQuestion.setParentAnswerId(0L);
        interviewQuestion.setProcessed(true);
        interviewQuestion.setTopNodeId(linkAJSM.getIdNode());
        interviewQuestion.setType(NodeType.Q_LINKEDAJSM.getDescription());
        return interviewQuestionService.updateIntQ(interviewQuestion);
    }

    private void createInterviewQuestionsAndAnswers(long idNode, long interviewId, String qNumber, String aNumber,
                                                    AtomicInteger qCounter, String freetext) {
        QuestionVO nodeQuestion = questionService.getQuestionByTopIdAndNumber(idNode, qNumber);
        if (nodeQuestion != null) {
            String questionIUniqueKey = getInterviewUniqueKey(nodeQuestion.getIdNode(), interviewId);
            if (!uniqueModules.containsKey(questionIUniqueKey)) {
                InterviewQuestionVO iQuestion = createInterviewQuestion(nodeQuestion, interviewId, qCounter.incrementAndGet());
                uniqueModules.put(questionIUniqueKey, iQuestion.getId());
            }

            PossibleAnswerVO nodeAnswer = possibleAnswerService.findByTopNodeIdAndNumber(idNode, aNumber);
            if (nodeAnswer != null) {
                String answerIUniqueKey = getInterviewUniqueKey(nodeAnswer.getIdNode(), interviewId);
                Long interviewAnswerId = null;
                if (uniqueModules.containsKey(answerIUniqueKey) && uniqueModules.get(answerIUniqueKey) != null) {
                    interviewAnswerId = (Long) uniqueModules.get(answerIUniqueKey);
                }
                InterviewAnswerVO interviewAnswer = createInterviewAnswer(interviewAnswerId, interviewId,
                        nodeAnswer, (Long) uniqueModules.get(questionIUniqueKey), freetext);

                if (!uniqueModules.containsKey(answerIUniqueKey)) {
                    uniqueModules.put(answerIUniqueKey, interviewAnswer.getId());
                }
            } else {
                log.warn("Answer not found for qNumber={}, aNumber={}.", qNumber, aNumber);
            }
        } else {
            log.warn("Question not found for idNode={}, number={}", idNode, qNumber);
        }

    }
}
