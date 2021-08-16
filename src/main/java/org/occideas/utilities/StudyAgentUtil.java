package org.occideas.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.config.QualtricsConfig;
import org.occideas.entity.Constant;
import org.occideas.module.service.ModuleService;
import org.occideas.node.service.INodeService;
import org.occideas.qsf.*;
import org.occideas.qsf.payload.Properties;
import org.occideas.qsf.payload.*;
import org.occideas.qsf.request.SurveyCreateRequest;
import org.occideas.qsf.response.*;
import org.occideas.qsf.service.IQSFService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Response;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class StudyAgentUtil {

    public static final String COMMA = ",";

    @Autowired
    private QualtricsConfig qualtricsConfig;

    @Autowired
    ServletContext context;
    private Logger log = LogManager.getLogger(this.getClass());
    @Autowired
    @Lazy
    private SystemPropertyService systemPropertyService;
    @Autowired
    @Lazy
    private ModuleService moduleService;
    @Autowired
    private INodeService nodeService;
    @Autowired
    private IQSFService iqsfService;
    @Autowired
    private IQSFClient iqsfClient;
    private Map<Long, String> idNodeQIDMap;
    private Map<Long, String> idNodeQIDMapIntro;
    private FlowResult flowResult;

    private Map<Long, String> uniqueTranslationModules;
    private Map<String, Map<String, String>> numberTranslationsMap;

    public ModuleVO getStudyAgentJson(String idNode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = getJsonFile(idNode);
        ModuleVO modVO = mapper.readValue(file, ModuleVO.class);
        return modVO;
    }

    private boolean shouldExcludeNode(List<String> idNodesAllowed, long idNode){
        return idNodesAllowed != null && !idNodesAllowed.contains(String.valueOf(idNode));
    }

    private boolean hasTranslations(String key) {
        return numberTranslationsMap != null && numberTranslationsMap.containsKey(key);
    }

    private List<String> getAvailableLanguages() {
        if (qualtricsConfig.isIncludeTranslations() && numberTranslationsMap != null && !numberTranslationsMap.isEmpty()) {
            for (Map.Entry<String, Map<String, String>> entry : numberTranslationsMap.entrySet()) {
                return new ArrayList<>(entry.getValue().keySet());
            }
        }
        return null;
    }

    private void loadTranslations() {
        try {
            List<String[]> extract = CsvUtil.readAll(qualtricsConfig.getTranslationsPath());
            numberTranslationsMap = new LinkedHashMap<>();
            String[] labels = extract.get(0);
            int index = 0;
            for (String[] data : extract) {
                if (index > 0) {
                    Map<String, String> entry = new LinkedHashMap<>();
                    int dataIndex = 0;
                    for (String value : data) {
                        if (dataIndex > 1) { //to exclude EN
                            entry.put(labels[dataIndex], value);
                        }
                        dataIndex++;
                    }
                    numberTranslationsMap.put(data[0], entry);
                }
                index++;
            }
            log.debug("translations: {}", numberTranslationsMap);
        } catch (Exception e) {
            log.error("Error encountered while loading questions and answers translations", e);
        }
    }

    public String buildQSF(ModuleVO moduleVO, boolean filter, boolean translation) {

        if ("M_IntroModule".equals(moduleVO.getType())) {
            if (qualtricsConfig.isIncludeTranslations() && !StringUtils.isBlank(qualtricsConfig.getTranslationsPath())) {
                loadTranslations();
            }

            QSFClient qsfClient = new QSFClient();
            SurveyCreateResult surveyCreateResult = createSurvey(moduleVO, null,
                    translation ? moduleVO.getName() + "_Translation" : null);
            String surveyId = surveyCreateResult.getSurveyId();
//            publishJobModules(moduleVO.getChildNodes(), qsfClient, surveyId);
            String blockId = surveyCreateResult.getDefaultBlockId();

            AtomicInteger qidCount = new AtomicInteger(0);
            idNodeQIDMapIntro = new HashMap<>();
            uniqueTranslationModules = new HashMap<>();
            List<SimpleQuestionPayload> questionPayloads = new ArrayList<>();
            for (QuestionVO qVO : moduleVO.getChildNodes()) {
                //if (shouldExcludeNode(listOfIdNodes,qVO.getIdNode())) {
                //	System.out.println("8-EXCLUDING!!" + qVO.getIdNode() + qVO.getName());
                //    continue;
                //}
                if (qVO.getDeleted() == 0) {
                    if (translation) {
                        createQSFTranslationModule(moduleVO, qVO, null, questionPayloads, qidCount, null);
                    } else {
                        createManualQuestionIntro(moduleVO, qVO, null, questionPayloads, surveyId, qidCount);
                    }
                }
            }
            log.debug("uniqueTranslationModules={}", uniqueTranslationModules);

            int i = 0;
            int size = questionPayloads.size();
            for (SimpleQuestionPayload payload : questionPayloads) {
                iqsfClient.createQuestion(surveyId, payload, null);
                System.out.println(i + " of " + size);
                i++;
            }
            GetBlockElementResult getBlockElementResult = getBlock(surveyId, blockId);
            createPageBreaks(getBlockElementResult);
//            getBlockElementResult.setBlockElement(buildBlockElements(questionPayloads.size(),moduleVO.getName()));
            iqsfClient.updateBlock(surveyId, blockId, getBlockElementResult);
            final Response surveyOptions = iqsfClient.getSurveyOptions(surveyId);
            SurveyOptionResponse options = (SurveyOptionResponse) surveyOptions.getEntity();
            options.getResult().setBackButton("true");

            List<String> availableLanguages = getAvailableLanguages();
            if (availableLanguages != null) {
                options.getResult().setAvailableLanguages(new AvailableLanguage(availableLanguages));
            }

            iqsfClient.updateSurveyOptions(surveyId, options.getResult());

            if (qualtricsConfig.isExpandModules()) {
                iqsfClient.publishSurvey(surveyId);
                iqsfClient.activateSurvey(surveyId);
            }

            return surveyId;
        } else {
        	List<String> listOfIdNodes = null;
            if (filter) {
                listOfIdNodes = moduleService.getFilterStudyAgent(moduleVO.getIdNode());
            }
        	String surveyId = manualBuildQSF(moduleVO, null, listOfIdNodes);

            return surveyId;
        }
    }

    private String publishJobModules(ModuleVO moduleVO, List<String> filter) {
    	idNodeQIDMap = new HashMap<>();
        AtomicInteger qidCount = new AtomicInteger(0);
        SurveyCreateResult result = createSurvey(moduleVO, null, null);
        String surveyId = result.getSurveyId();
        String blockId = result.getDefaultBlockId();

        List<SimpleQuestionPayload> questionPayloads = new ArrayList<>();
        for (QuestionVO qVO : moduleVO.getChildNodes()) {
            if (shouldExcludeNode(filter,qVO.getIdNode())) {
            	System.out.println("7-EXCLUDING!!" + qVO.getIdNode() + qVO.getName());
                continue;
            }
            createManualQuestion(moduleVO, qVO, null, questionPayloads, qidCount,filter);
        }

        int i = 0;
        int iSize = questionPayloads.size();
        for (SimpleQuestionPayload payload : questionPayloads) {
            System.out.println(payload.getQuestionText());
            System.out.println(i + " of " + iSize);
            iqsfClient.createQuestion(surveyId, payload, null);
            i++;
        }

        GetBlockElementResult getBlockElementResult = getBlock(surveyId, blockId);
        createPageBreaks(getBlockElementResult);
//        getBlockElementResult.setBlockElement(buildBlockElements(questionPayloads.size(),moduleVO.getName()));
        iqsfClient.updateBlock(surveyId, blockId, getBlockElementResult);

        //adding embedded data flow
        Response getFlowResponse = iqsfClient.getFlow(surveyId);
        FlowResult flowResult = ((GetFlowResponse) getFlowResponse.getEntity()).getResult();
        List<EmbeddedData> embeddedDataList = new ArrayList<>();
        EmbeddedData embeddedData = new EmbeddedData("Recipient", "IDAnswer", "String");
        embeddedDataList.add(embeddedData);
        List<Flow> flows = new ArrayList<>();
        flows.add(new Flow(null, "EmbeddedData", "FL_3", embeddedDataList));
        flows.addAll(flowResult.getFlows());
        Flow mainFlow = new Flow(null, "Root", flowResult.getFlowId(), null,
                flows, null, null, null);
        iqsfClient.updateFlow(surveyId, mainFlow);

        final Response surveyOptions = iqsfClient.getSurveyOptions(surveyId);
        SurveyOptionResponse options = (SurveyOptionResponse) surveyOptions.getEntity();
        options.getResult().setBackButton("true");
        iqsfClient.updateSurveyOptions(surveyId, options.getResult());

        iqsfClient.publishSurvey(surveyId);
        iqsfClient.activateSurvey(surveyId);
        return surveyId;
    }

    private void createPageBreaks(GetBlockElementResult getBlockElementResult) {
        int size = getBlockElementResult.getBlockElement().size();
        int x = 0;
        for (int i = 0; i < size; i++) {
            getBlockElementResult.getBlockElement().add(x + 1, new BlockElement("Page Break", null));
            x += 2;
        }
    }

    public void createManualQuestionIntro
            (NodeVO rootNode, QuestionVO qVO, DisplayLogic logic,
             List<SimpleQuestionPayload> questionPayloads,
             String parentSurveyId,
             AtomicInteger qidCount) {
        if (qVO.getLink() == 0L) {
            questionPayloads.add(buildSimpleQuestionPayloadWithAnswers(rootNode, qVO, logic, qidCount));
        }
        processChildNodes(rootNode, qVO, questionPayloads, parentSurveyId, qidCount);
    }

    public void processChildNodes(NodeVO rootNode,
                                  QuestionVO questionNode,
                                  List<SimpleQuestionPayload> questionPayloads,
                                  String parentSurveyId,
                                  AtomicInteger qidCount) {
        int ansCount = 1;
        for (PossibleAnswerVO answer : questionNode.getChildNodes()) {
            if (answer.getDeleted() != 0) {
                continue;
            }
            if (!answer.getChildNodes().isEmpty()) {
                for (QuestionVO childQuestionVO : answer.getChildNodes()) {
                    if (childQuestionVO.getDeleted() != 0) {
                        continue;
                    }

                    if (childQuestionVO.getLink() == 0L) {
                        //if (shouldExcludeNode(filter,childQuestionVO.getIdNode())) {
                        //	System.out.println("4-EXCLUDING!!" + questionNode.getIdNode() + questionNode.getName());
                        //    continue;
                        //}
                        if (!idNodeQIDMapIntro.containsKey(childQuestionVO.getIdNode())) {
                            String qidStrCount = "QID" + qidCount.incrementAndGet();
                            idNodeQIDMapIntro.put(childQuestionVO.getIdNode(), qidStrCount);
                        }
                        String childQidCount = idNodeQIDMapIntro.get(Long.valueOf(answer.getParentId()));
                        String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;

                        List<Logic> logics = new ArrayList<>();
                        if (Constant.Q_FREQUENCY.equals(questionNode.getType())) {
                            handleDisplayLogicFreq(answer, childQuestionVO, childQidCount, logics);
                        } else {
                            logics.add(new Logic("Question", childQidCount, "no", choiceLocator, "Selected",
                                    childQidCount, choiceLocator, "Expression", childQuestionVO.getName()));
                        }

                        createManualQuestionIntro(rootNode, childQuestionVO,
                                new DisplayLogic("BooleanExpression", false, new Condition(buildLogicMap(logics), "If")), questionPayloads, parentSurveyId, qidCount);
                    } else if (childQuestionVO.getLink() != 0L) {
                        processLinks(rootNode, questionNode, questionPayloads, parentSurveyId, qidCount, ansCount, answer, childQuestionVO);
                    }
                }
            }
            ansCount++;
        }
    }

    public void processLinks(NodeVO rootNode,
                             QuestionVO parentQuestionNode,
                             List<SimpleQuestionPayload> questionPayloads,
                             String parentSurveyId,
                             AtomicInteger qidCount,
                             int ansCount,
                             PossibleAnswerVO childAnswerNode,
                             QuestionVO childQuestionVO) {
        List<String> listOfIdNodes = null;
        //if (filter != null) {
        listOfIdNodes = moduleService.getFilterStudyAgent(childQuestionVO.getLink());
        //}
        if (listOfIdNodes == null) {
            System.err.println("5-EXCLUDING MODULE MJOR ERROR!!" + childQuestionVO.getIdNode() + childQuestionVO.getName());
            return;
        }
        NodeVO linkModule = nodeService.getNode(childQuestionVO.getLink());
        String childQidCount = idNodeQIDMapIntro.get(Long.valueOf(childAnswerNode.getParentId()));
        String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;

        List<Logic> logics = new ArrayList<>();
        if (Constant.Q_FREQUENCY.equals(parentQuestionNode.getType())) {
            handleDisplayLogicFreq(childAnswerNode, childQuestionVO, childQidCount, logics);
        } else {
            logics.add(new Logic("Question", childQidCount, "no", choiceLocator,
                    "Selected", childQidCount, choiceLocator, "Expression",
                    childQuestionVO.getName()));
        }

        if ("F".equals(linkModule.getNodeclass())) {
            System.err.println("THIS SHOULD NEVER BE TRUE MAJOR ERROR!!" + linkModule.getIdNode() + linkModule.getName());
            processFragments(rootNode, questionPayloads, parentSurveyId, qidCount, (FragmentVO) linkModule, logics);
        } else {
            if (qualtricsConfig.isExpandModules()) {
                expandModule(rootNode, questionPayloads, parentSurveyId, qidCount, ansCount, childQuestionVO, linkModule, childAnswerNode);
            } else {
                if (flowResult == null) {
                    Response getFlowResponse = iqsfClient.getFlow(parentSurveyId);
                    flowResult = ((GetFlowResponse) getFlowResponse.getEntity()).getResult();
                }
                if (!flowResult.getFlows().contains(new Flow(String.valueOf(linkModule.getIdNode())))) {
                    publishLinksAsNewModules(parentSurveyId, childQuestionVO, listOfIdNodes, linkModule, childQidCount, choiceLocator);
                }
            }
        }
    }

    private void expandModule(NodeVO rootNode, List<SimpleQuestionPayload> questionPayloads,
                              String parentSurveyId, AtomicInteger qidCount,
                              int ansCount,
                              QuestionVO childQuestionVO, NodeVO linkModule, PossibleAnswerVO answer) {
        linkModule.getChildNodes().forEach(node -> {
            if (node instanceof QuestionVO) {
                QuestionVO question = (QuestionVO) node;
                if (question.getLink() > 0) {
                    question.getChildNodes().forEach(childAns -> processLinks(rootNode, question, questionPayloads, parentSurveyId, qidCount, ansCount, childAns, childQuestionVO));
                } else {
                    question.getChildNodes().forEach(childAns -> {
                        String childQidCount = idNodeQIDMapIntro.get(Long.valueOf(answer.getParentId()));
                        String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;
                        List<Logic> logics = new ArrayList<>();
                        if (Constant.Q_FREQUENCY.equals(question.getType())) {
                            handleDisplayLogicFreq(childAns, childQuestionVO, childQidCount, logics);
                        } else {
                            logics.add(new Logic("Question", childQidCount, "no", choiceLocator, "Selected",
                                    childQidCount, choiceLocator, "Expression", childQuestionVO.getName()));
                        }
                        createManualQuestionIntro(rootNode, question,
                                new DisplayLogic("BooleanExpression", false, new Condition(buildLogicMap(logics), "If")),
                                questionPayloads,
                                parentSurveyId,
                                qidCount);
                    });
                }
            }
        });
    }

    private void publishLinksAsNewModules(String parentSurveyId, QuestionVO childQuestionVO, List<String> listOfIdNodes, NodeVO linkModule, String childQidCount, String choiceLocator) {
        String linkModSurveyId = publishJobModules((ModuleVO) linkModule, listOfIdNodes);
        String url = iqsfClient.buildRedirectUrl(linkModSurveyId) + "?IDAnswer=${q://QID1/ChoiceTextEntryValue/1}";
        List<Logic> introLogics = new ArrayList<>();
        Logic introLogic = new Logic("Question",
                childQidCount, "no",
                choiceLocator, "Selected",
                childQidCount,
                choiceLocator, "Expression", childQuestionVO.getName());
        introLogics.add(introLogic);
        List<Condition> conditions = new ArrayList<>();
        Condition condition = new Condition(introLogics, "If");
        conditions.add(condition);
        BranchLogic branchLogic = new BranchLogic(conditions, "BooleanExpression");
        List<Flow> flows = new ArrayList<>();
        int branchFlow = flowResult.getFlows().size() + 1;
        Flow flow = new Flow(null, "EndSurvey", "FL_" + (branchFlow + 1),
                null, null, "Advanced", new Options("true",
                "Redirect", url), null);
        flows.add(flow);
        flowResult.getFlows().add(new Flow(null,
                "Branch",
                "FL_" + (branchFlow), branchLogic, flows, null, null,
                String.valueOf(linkModule.getIdNode())));
        Flow mainFlow = new Flow(null, "Root", flowResult.getFlowId(),
                null, flowResult.getFlows(), null, null,
                null);
        iqsfClient.updateFlow(parentSurveyId, mainFlow);
    }

    private void processFragments(NodeVO rootNode, List<SimpleQuestionPayload> questionPayloads, String parentSurveyId, AtomicInteger qidCount, FragmentVO linkModule, List<Logic> logics) {
        for (QuestionVO linkQuestion : linkModule.getChildNodes()) {
            createManualQuestionIntro(rootNode, linkQuestion,
                    new DisplayLogic("BooleanExpression", false,
                            new Condition(buildLogicMap(logics), "If")), questionPayloads,
                    parentSurveyId, qidCount);
        }
    }

    private SimpleQuestionPayload buildSimpleQuestionPayloadWithAnswers(NodeVO node, QuestionVO qVO, DisplayLogic logic, AtomicInteger qidCount) {
        if (!idNodeQIDMapIntro.containsKey(qVO.getIdNode())) {
            String qidStrCount = "QID" + qidCount.incrementAndGet();
            idNodeQIDMapIntro.put(qVO.getIdNode(), qidStrCount);
        }
        String numberKey = node.getName().substring(0, 4) + "_" + qVO.getNumber();
        String questionTextKey = numberKey + " - ";
        String hiddenQuestionTextKey = Constant.SPAN_START_DISPLAY_NONE + questionTextKey + Constant.SPAN_END;
        String questionTxt = (qualtricsConfig.isHideNodeKeys() ? hiddenQuestionTextKey : questionTextKey) + qVO.getName();
        List<Language> languages = hasTranslations(numberKey) ? buildLanguages(node.getName(), qVO) : new ArrayList<>();
        SimpleQuestionPayload payload = null;
        if (logic == null) {
            payload = new SimpleQuestionPayload(questionTxt, qVO.getNumber(), "MC", QuestionSelector.get(qVO.getType()), "TX",
                    new Configuration("UseText"), qVO.getName(), buildChoices(qVO, node.getName(), null),
                    buildChoiceOrder(qVO), new Validation(new Setting("ON", "ON", "None")), languages, logic);
        } else {
            payload = new SimpleQuestionPayload(questionTxt, qVO.getNumber(), "MC", QuestionSelector.get(qVO.getType()), "TX",
                    new Configuration("UseText"), qVO.getName(), buildChoices(qVO, node.getName(), null),
                    buildChoiceOrder(qVO), new Validation(new Setting("ON", "ON", "None")), languages, logic);
        }
        return payload;
    }

    private void createQSFTranslationModule(NodeVO node, QuestionVO qVO, DisplayLogic logic,
                                            List<SimpleQuestionPayload> questionPayloads, AtomicInteger qidCount, List<String> filteredIdNodes) {
        if (qVO.getLink() == 0L) {
            if (!"M_IntroModule".equals(node.getType()) && filteredIdNodes != null
                    && !filteredIdNodes.contains(String.valueOf(qVO.getIdNode()))) {
                log.warn("Q_idNode={} not study agent, excluded", qVO.getIdNode());
                return;
            }
            if (!idNodeQIDMapIntro.containsKey(qVO.getIdNode())) {
                String qidStrCount = "QID" + qidCount.incrementAndGet();
                idNodeQIDMapIntro.put(qVO.getIdNode(), qidStrCount);
            }
            String questionTextKey = node.getName().substring(0, 4) + "_" + qVO.getNumber() + " - ";
            String hiddenQuestionTextKey = Constant.SPAN_START_DISPLAY_NONE + questionTextKey + Constant.SPAN_END;
            String questionTxt = (qualtricsConfig.isHideNodeKeys() ? hiddenQuestionTextKey : questionTextKey) + qVO.getName();
            SimpleQuestionPayload payload = null;
            if (logic == null) {
                payload = new SimpleQuestionPayload(questionTxt, qVO.getNumber(), "MC", QuestionSelector.get(qVO.getType()), "TX",
                        new Configuration("UseText"), qVO.getName(), buildChoices(qVO, node.getName(), null),
                        buildChoiceOrder(qVO), new Validation(new Setting("ON", "ON", "None")), new ArrayList<>(), logic);
            } else {
                payload = new SimpleQuestionPayload(questionTxt, qVO.getNumber(), "MC", QuestionSelector.get(qVO.getType()), "TX",
                        new Configuration("UseText"), qVO.getName(), buildChoices(qVO, node.getName(), null),
                        buildChoiceOrder(qVO), new Validation(new Setting("ON", "ON", "None")), new ArrayList<>(), logic);
            }
            questionPayloads.add(payload);

        } else if (qVO.getLink() != 0L) {
            NodeVO linkModule = nodeService.getNode(qVO.getLink());
            if ("F".equals(linkModule.getNodeclass())) {
                if (!uniqueTranslationModules.containsKey(qVO.getLink())) {
                    log.debug("linkedModule fragment name={}, idNode={}, hasChildNode=",
                            linkModule.getName(), linkModule.getIdNode(), !CollectionUtils.isEmpty(((FragmentVO) linkModule).getChildNodes()));
                    List<String> newFilteredIdNodes = moduleService.getFilterStudyAgent(linkModule.getIdNode());
                    uniqueTranslationModules.put(qVO.getLink(), linkModule.getName().substring(0, 4));
                    for (QuestionVO linkQuestion : ((FragmentVO) linkModule).getChildNodes()) {
                        createQSFTranslationModule(linkModule, linkQuestion, null, questionPayloads, qidCount, newFilteredIdNodes);
                    }
                }
            } else {
                log.warn("Not a fragment. link={}", linkModule.getIdNode());
            }
        }
        int ansCount = 1;
        for (PossibleAnswerVO answer : qVO.getChildNodes()) {
            if (answer.getDeleted() != 0) {
                continue;
            }
            if (!answer.getChildNodes().isEmpty()) {
                for (QuestionVO childQuestionVO : answer.getChildNodes()) {
                    if (childQuestionVO.getDeleted() != 0) {
                        continue;
                    }

                    if (childQuestionVO.getLink() == 0L) {
                        if (!idNodeQIDMapIntro.containsKey(childQuestionVO.getIdNode())) {
                            String qidStrCount = "QID" + qidCount.incrementAndGet();
                            idNodeQIDMapIntro.put(childQuestionVO.getIdNode(), qidStrCount);
                        }
                        String childQidCount = idNodeQIDMapIntro.get(Long.valueOf(answer.getParentId()));
                        String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;

                        List<Logic> logics = new ArrayList<>();
                        if (Constant.Q_FREQUENCY.equals(qVO.getType())) {
                            handleDisplayLogicFreq(answer, childQuestionVO, childQidCount, logics);
                        } else {
                            logics.add(new Logic("Question", childQidCount, "no", choiceLocator, "Selected",
                                    childQidCount, choiceLocator, "Expression", childQuestionVO.getName()));
                        }
                        createQSFTranslationModule(node, childQuestionVO,
                                new DisplayLogic("BooleanExpression", false, new Condition(buildLogicMap(logics), "If")),
                                questionPayloads, qidCount, filteredIdNodes);
                    } else if (childQuestionVO.getLink() != 0L) {
                        NodeVO linkModule = nodeService.getNode(childQuestionVO.getLink());
                        if (!uniqueTranslationModules.containsKey(childQuestionVO.getLink())) {
                            List<String> newFilteredIdNodes = moduleService.getFilterStudyAgent(linkModule.getIdNode());
                            uniqueTranslationModules.put(childQuestionVO.getLink(), linkModule.getName().substring(0, 4));
                            String childQidCount = idNodeQIDMapIntro.get(Long.valueOf(answer.getParentId()));
                            String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;

                            List<Logic> logics = new ArrayList<>();
                            if (Constant.Q_FREQUENCY.equals(qVO.getType())) {
                                handleDisplayLogicFreq(answer, childQuestionVO, childQidCount, logics);
                            } else {
                                logics.add(new Logic("Question", childQidCount, "no", choiceLocator,
                                        "Selected", childQidCount, choiceLocator, "Expression",
                                        childQuestionVO.getName()));
                            }

                            if ("F".equals(linkModule.getNodeclass())) {
                                log.debug("linkedModule fragment name={}, idNode={}, hasChildNode=",
                                        linkModule.getName(), linkModule.getIdNode(), !CollectionUtils.isEmpty(((FragmentVO) linkModule).getChildNodes()));
                                for (QuestionVO linkQuestion : ((FragmentVO) linkModule).getChildNodes()) {
                                    createQSFTranslationModule(linkModule, linkQuestion,
                                            new DisplayLogic("BooleanExpression", false,
                                                    new Condition(buildLogicMap(logics), "If")), questionPayloads, qidCount, newFilteredIdNodes);
                                }
                            } else if ("M_Module".equals(linkModule.getType())) {
                                log.debug("linkedModule non-fragment name={}, idNode={}, hasChildNodes={}",
                                        linkModule.getName(), linkModule.getIdNode(), !CollectionUtils.isEmpty(((ModuleVO) linkModule).getChildNodes()));
                                for (QuestionVO linkQuestion : ((ModuleVO) linkModule).getChildNodes()) {
                                    createQSFTranslationModule(linkModule, linkQuestion,
                                            new DisplayLogic("BooleanExpression", false, new Condition(buildLogicMap(logics), "If")),
                                            questionPayloads, qidCount, newFilteredIdNodes);
                                }
                            }
                        }
                    }
                }
            }
            ansCount++;
        }
    }


    private Map<Integer,Logic> buildLogicMap(List<Logic> list){
        Map<Integer,Logic> map = new HashMap<>();
        AtomicInteger count = new AtomicInteger(0);
        list.stream().forEach(logic -> {
            map.put(count.getAndIncrement(),logic);
        });
        return map;
    }

    public String manualBuildQSF(ModuleVO moduleVO, String surveyId,List<String> filter) {
        if (qualtricsConfig.isIncludeTranslations() && !StringUtils.isBlank(qualtricsConfig.getTranslationsPath())) {
            loadTranslations();
        }

        AtomicInteger qidCount = new AtomicInteger(0);
        idNodeQIDMap = new HashMap<>();
        String blockId = "";
        if (surveyId == null) {
            SurveyCreateResult surveyCreateResult = createSurvey(moduleVO, surveyId, null);
            surveyId = surveyCreateResult.getSurveyId();
            blockId = surveyCreateResult.getDefaultBlockId();
        }
        List<SimpleQuestionPayload> questionPayloads = new ArrayList<>();
        for (QuestionVO qVO : moduleVO.getChildNodes()) {
            //if (shouldExcludeNode(filter,qVO.getIdNode())) {
            //	System.out.println("6-EXCLUDING!!" + qVO.getIdNode() + qVO.getName());
            //    continue;
            //}
            createManualQuestion(moduleVO, qVO, null, questionPayloads, qidCount, filter);
        }
        int i = 0;
        int size = questionPayloads.size();
        for (SimpleQuestionPayload payload : questionPayloads) {
            iqsfClient.createQuestion(surveyId, payload, null);
            System.out.println(i + " of " + size);
            i++;
        }

        final Response surveyOptions = iqsfClient.getSurveyOptions(surveyId);
        SurveyOptionResponse options = (SurveyOptionResponse) surveyOptions.getEntity();
        options.getResult().setBackButton("true");

        List<String> availableLanguages = getAvailableLanguages();
        if (availableLanguages != null) {
            options.getResult().setAvailableLanguages(new AvailableLanguage(availableLanguages));
        }

        iqsfClient.updateSurveyOptions(surveyId, options.getResult());

        GetBlockElementResult getBlockElementResult = getBlock(surveyId, blockId);
        createPageBreaks(getBlockElementResult);
        iqsfClient.updateBlock(surveyId, blockId, getBlockElementResult);
        return surveyId;
    }

    private SurveyCreateResult createSurvey(ModuleVO moduleVO, String surveyId, String surveyName) {
        Response response = iqsfClient.createSurvey(new SurveyCreateRequest(
                StringUtils.isNotBlank(surveyName) ? surveyName : moduleVO.getName().replaceAll("\\s+", ""),
                "EN",
                "CORE"
        ));
        Object entity = response.getEntity();
        if (entity instanceof SurveyCreateResponse) {
            SurveyCreateResponse surveyCreateResponse = (SurveyCreateResponse) entity;
            iqsfService.save(surveyCreateResponse.getResult().getSurveyId(), moduleVO.getIdNode(), null);
            return surveyCreateResponse.getResult();
        }
        return null;
    }

    private GetBlockElementResult getBlock(String surveyId, String blockId) {
        Response response = iqsfClient.getBlock(surveyId, blockId);
        Object entity = response.getEntity();
        if (entity instanceof GetBlockElementResponse) {
            GetBlockElementResponse getBlockElementResponse = (GetBlockElementResponse) entity;
            return getBlockElementResponse.getResult();
        }
        return null;
    }

    public File moduleToApplicationQSF(ModuleVO module) throws IOException {
        AtomicInteger qidCount = new AtomicInteger(0);
        String path = "/opt/data";
        File directory = new File(path + "/qsf");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = new Date();
        File file = new File(
                path + "/qsf/" + module.getName().replaceAll("\\s+", "") + "_" + dateFormat.format(date) + ".qsf");
        if (!file.exists()) {
            file.createNewFile();
        }

        ApplicationQSF applicationQSF = new ApplicationQSF();
        final String surveyId = "SV_3W2wSyxLDan2ZPD";
        applicationQSF.setSurveyEntry(new SurveyEntry(surveyId, "SimpleSurvey", null, "UR_es2Gulf6I7xN6p7", "curtin",
                null, "EN", "RS_cMiHcMFlPoWrvyl", "Inactive", "0000-00-00 00:00:00", "0000-00-00 00:00:00",
                "2019-11-22 02:22:39", "UR_es2Gulf6I7xN6p7", "2019-11-22 02:25:12", "0000-00-00 00:00:00",
                "0000-00-00 00:00:00", null));

        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(applicationQSF.toString().substring(0, applicationQSF.toString().length() - 1));
        bw.write(",");
        // List<SurveyElement> surveyElements = new ArrayList<>();
        List<Flow> flows = new ArrayList<>();
        flows.add(new Flow("BL_agDgwnIMsiB1KJL", "Block", "FL_2"));
        SurveyElement flowElement = new SurveyElement(surveyId, QSFElementTypes.FLOW.getAbbr(),
                QSFElementTypes.FLOW.getDesc(), null, null,
                buildPayload(new SubPayload(flows, new Properties(2), "FL_1", "Root")));

        SurveyElement surveyOptions = new SurveyElement(surveyId, QSFElementTypes.OPTIONS.getAbbr(),
                QSFElementTypes.OPTIONS.getDesc(), null, null,
                buildPayload(new SurveyOptionPayload("true", "true", "PublicSurvey", "false", "Yes", "true", "None",
                        "DefaultMessage", "", "", "None", "+1 week", "", "\u2190", "\u2192", "curtin", "MQ", "curtin1",
                        1)));

        SurveyElement scoringElement = new SurveyElement(surveyId, QSFElementTypes.SCORING.getAbbr(),
                QSFElementTypes.SCORING.getDesc(), null, null,
                buildPayload(new ScoringPayload(new ArrayList<>(), new ArrayList<>(), null, 0, 0, null, null)));

        SurveyElement projElement = new SurveyElement(surveyId, QSFElementTypes.PROJECT.getAbbr(),
                QSFElementTypes.PROJECT.getDesc(), null, "1.1.0", buildPayload(new ProjectPayload("CORE", "1.1.0")));

        SurveyElement statElement = new SurveyElement(surveyId, QSFElementTypes.STATISTICS.getAbbr(),
                QSFElementTypes.STATISTICS.getDesc(), null, null,
                buildPayload(new StatisticsPayload(true, "Survey Statistics")));

        SurveyElement defaultResponseElement = new SurveyElement(surveyId, QSFElementTypes.RESPONSESET.getAbbr(),
                "RS_cMiHcMFlPoWrvyl", QSFElementTypes.RESPONSESET.getDesc(), null, null);

        String questions = buildQuestions(module, surveyId, qidCount);
        // new questions to be added to block elements - link modules
        SurveyElement blockElement = new SurveyElement(surveyId, QSFElementTypes.BLOCK.getAbbr(),
                QSFElementTypes.BLOCK.getDesc(), null, null,
                buildPayload(
                        new Default("Default", "Default Question Block", "BL_agDgwnIMsiB1KJL",
                                buildBlockElements(qidCount.intValue(), module.getName())),
                        new Trash("Trash", "Trash \\/ Unused Questions", "BL_4I380AxUd3s3gt7")));
        bw.write("\"SurveyElements\":[");
        bw.write(blockElement.toString());
        bw.write(",");
        bw.write(flowElement.toString());
        bw.write(",");
        bw.write(surveyOptions.toString());
        bw.write(",");
        bw.write(scoringElement.toString());
        bw.write(",");
        bw.write(projElement.toString());
        bw.write(",");
        bw.write(statElement.toString());
        SurveyElement questionCountElement = new SurveyElement(surveyId, QSFElementTypes.QUESTIONCOUNT.getAbbr(),
                QSFElementTypes.QUESTIONCOUNT.getDesc(), String.valueOf(qidCount.intValue()), null, null);
        bw.write(",");
        bw.write(questionCountElement.toString());
        bw.write(",");
        bw.write(defaultResponseElement.toString());
        bw.write(",");
        bw.write(questions.substring(0, questions.length() - 1));

        bw.write("]}");
        bw.close();

        return file;
    }

    private String buildQuestions(ModuleVO module, final String surveyId, AtomicInteger qidCount) {
        StringBuilder sb = new StringBuilder();
        for (QuestionVO qVO : module.getChildNodes()) {
            createQuestion(module, surveyId, qVO, sb, null, qidCount);
        }
        return sb.toString().replace("-999999", String.valueOf(qidCount.intValue() + 1));
    }

    private void createQuestion(NodeVO node, final String surveyId, QuestionVO qVO, StringBuilder sb,
                                DisplayLogic logic, AtomicInteger qidCount) {
        if (qVO.getLink() == 0L) {
            String questionTxt = node.getName().substring(0, 4) + "_" + qVO.getNumber() + " - " + qVO.getName();
            String qidStrCount = "QID" + qidCount.incrementAndGet();
            Object payload = null;
            if (logic == null) {
                payload = buildPayload(new QuestionPayload(questionTxt, qVO.getNumber(), "MC", QuestionSelector.get(qVO.getType()), "TX",
                        new Configuration("UseText"), qVO.getName(), buildChoices(qVO, node.getName(), null),
                        buildChoiceOrder(qVO), new Validation(new Setting("ON", "ON", "None")), new ArrayList<>(), -999999,
                        1, qidStrCount, logic));
            } else {
                payload = buildPayload(new QuestionPayload(questionTxt, qVO.getNumber(), "MC", QuestionSelector.get(qVO.getType()), "TX",
                        new Configuration("UseText"), qVO.getName(), buildChoices(qVO, node.getName(), null),
                        buildChoiceOrder(qVO), new Validation(new Setting("ON", "ON", "None")), new ArrayList<>(), -999999,
                        1, qidStrCount, logic));
            }
            SurveyElement question = new SurveyElement(surveyId, QSFElementTypes.QUESTION.getAbbr(), qidStrCount,
                    questionTxt, null, payload);
            sb.append(question.toString());
            sb.append(",");
        }
        int ansCount = 1;
        for (PossibleAnswerVO answer : qVO.getChildNodes()) {
            if (!answer.getChildNodes().isEmpty()) {
                for (QuestionVO childQuestionVO : answer.getChildNodes()) {
                    if (childQuestionVO.getLink() == 0L) {
                        String childQidCount = "QID" + qidCount.intValue();
                        String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;

                        List<Logic> logics = new ArrayList<>();
                        if(Constant.Q_FREQUENCY.equals(qVO.getType())){
                            handleDisplayLogicFreq(answer, childQuestionVO, childQidCount, logics);
                        }else {
                            logics.add(new Logic("Question", childQidCount, "no", choiceLocator, "Selected",
                                    childQidCount, choiceLocator, "Expression", childQuestionVO.getName()));
                        }

                        createQuestion(node, surveyId, childQuestionVO, sb,
                                new DisplayLogic("BooleanExpression", false, new Condition(
                                        buildLogicMap(logics),
                                        "If")), qidCount);
                    } else if (childQuestionVO.getLink() != 0L) {
                        NodeVO linkModule = nodeService.getNode(childQuestionVO.getLink());
                        String childQidCount = "QID" + qidCount.intValue();
                        String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;

                        List<Logic> logics = new ArrayList<>();
                        if(Constant.Q_FREQUENCY.equals(qVO.getType())){
                            handleDisplayLogicFreq(answer, childQuestionVO, childQidCount, logics);
                        }else {
                            logics.add(new Logic("Question", childQidCount, "no", choiceLocator,
                                    "Selected", childQidCount, choiceLocator, "Expression",
                                    childQuestionVO.getName()));
                        }

                        if ("F".equals(linkModule.getNodeclass())) {
                            for (QuestionVO linkQuestion : ((FragmentVO) linkModule).getChildNodes()) {
                                createQuestion(node, surveyId, linkQuestion, sb,
                                        new DisplayLogic("BooleanExpression", false,new Condition(buildLogicMap(logics), "If")), qidCount);
                            }
                        } else {
                            for (QuestionVO linkQuestion : ((ModuleVO) linkModule).getChildNodes()) {
                                createQuestion(linkModule, surveyId, linkQuestion, sb,
                                        new DisplayLogic("BooleanExpression", false, new Condition(buildLogicMap(logics), "If")), qidCount);
                            }
                        }

                    }
                }
            }
            ansCount++;
        }
    }

    private void createManualQuestion
            (NodeVO node, QuestionVO qVO, DisplayLogic logic,
             List<SimpleQuestionPayload> questionPayloads, AtomicInteger qidCount, List<String> filter) {
        if (qVO.getLink() == 0L) {
        	if(shouldExcludeNode(filter,qVO.getIdNode())){
        		System.out.println("1-EXCLUDING!!" + qVO.getIdNode() + qVO.getName());
                return;
            }
            if (!idNodeQIDMap.containsKey(qVO.getIdNode())) {
                String qidStrCount = "QID" + qidCount.incrementAndGet();
                idNodeQIDMap.put(qVO.getIdNode(), qidStrCount);
            }
            String numberKey = node.getName().substring(0, 4) + "_" + qVO.getNumber();
            String questionTextKey = numberKey + " - ";
            String hiddenQuestionTextKey = Constant.SPAN_START_DISPLAY_NONE + questionTextKey + Constant.SPAN_END;
            String questionTxt = (qualtricsConfig.isHideNodeKeys() ? hiddenQuestionTextKey : questionTextKey) + qVO.getName();

            List<Language> languages = hasTranslations(numberKey) ? buildLanguages(node.getName(), qVO) : new ArrayList<>();
            SimpleQuestionPayload payload = null;
            if (logic == null) {
                payload = new SimpleQuestionPayload(questionTxt, qVO.getNumber(), "MC", QuestionSelector.get(qVO.getType()), "TX",
                        new Configuration("UseText"), qVO.getName(), buildChoices(qVO, node.getName(), null),
                        buildChoiceOrder(qVO), new Validation(new Setting("ON", "ON", "None")), languages, logic);
            } else {
                payload = new SimpleQuestionPayload(questionTxt, qVO.getNumber(), "MC", QuestionSelector.get(qVO.getType()), "TX",
                        new Configuration("UseText"), qVO.getName(), buildChoices(qVO, node.getName(), null),
                        buildChoiceOrder(qVO), new Validation(new Setting("ON", "ON", "None")), languages, logic);
            }
            questionPayloads.add(payload);
        } else if (qVO.getLink() != 0L) {
            List<String> filterIdNodes = null;
            if(filter != null){
                filterIdNodes = moduleService.getFilterStudyAgent(qVO.getLink());
            }
            if(filterIdNodes != null) {
                NodeVO linkModule = nodeService.getNode(qVO.getLink());
                if ("F".equals(linkModule.getNodeclass())) {
                    for (QuestionVO linkQuestion : ((FragmentVO) linkModule).getChildNodes()) {
                        //if (filter != null && !filter.contains(linkQuestion.getIdNode())) {
                        //    continue;
                        //}
                        if(shouldExcludeNode(filterIdNodes,linkQuestion.getIdNode())){
                        	System.out.println("2-EXCLUDING!!" + linkQuestion.getIdNode() + linkQuestion.getName());
                        	continue;
                        }

                        createManualQuestion(linkModule, linkQuestion, null, questionPayloads, qidCount, filterIdNodes);
                    }
                } else {
                    System.out.println("Something not right here!!");
                }
            } else {

            }
        }
        int ansCount = 1;
        for (PossibleAnswerVO answer : qVO.getChildNodes()) {
            //if (filter != null && !filter.contains(answer.getIdNode())) {
            //    continue;
            //}
            if (!answer.getChildNodes().isEmpty()) {
                for (QuestionVO childQuestionVO : answer.getChildNodes()) {
                    //if (filter != null && !filter.contains(childQuestionVO.getIdNode())) {
                    //    continue;
                    //}

                    if (childQuestionVO.getLink() == 0L) {
                    	if(shouldExcludeNode(filter,childQuestionVO.getIdNode())){
                    		System.out.println("10-EXCLUDING!!" + childQuestionVO.getIdNode() + childQuestionVO.getName());
                    		continue;
                        }
                        if (!idNodeQIDMap.containsKey(childQuestionVO.getIdNode())) {
                            String qidStrCount = "QID" + qidCount.incrementAndGet();
                            idNodeQIDMap.put(childQuestionVO.getIdNode(), qidStrCount);
                        }
                        String childQidCount = idNodeQIDMap.get(Long.valueOf(answer.getParentId()));
                        String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;

                        List<Logic> logics = new ArrayList<>();
                        if(Constant.Q_FREQUENCY.equals(qVO.getType())){
                            handleDisplayLogicFreq(answer, childQuestionVO, childQidCount, logics);
                        }else {
                            logics.add(new Logic("Question", childQidCount, "no", choiceLocator, "Selected",
                                    childQidCount, choiceLocator, "Expression", childQuestionVO.getName()));
                        }


                        createManualQuestion(node, childQuestionVO,
                                new DisplayLogic("BooleanExpression", false, new Condition(
                                        buildLogicMap(logics),
                                        "If")), questionPayloads, qidCount, filter);
                    } else if (childQuestionVO.getLink() != 0L) {
                    	System.out.println(childQuestionVO.getName());
                        NodeVO linkModule = nodeService.getNode(childQuestionVO.getLink());
                        String childQidCount = idNodeQIDMap.get(Long.valueOf(answer.getParentId()));
                        String choiceLocator = "q://" + childQidCount + "/SelectableChoice/" + ansCount;
                        List<String> filterIdNodes = null;
                        if(filter != null){
                            filterIdNodes = moduleService.getFilterStudyAgent(childQuestionVO.getLink());
                        }
                        if ("F".equals(linkModule.getNodeclass())) {
                            for (QuestionVO linkQuestion : ((FragmentVO) linkModule).getChildNodes()) {
                                List<Logic> logics = new ArrayList<>();
                                if(Constant.Q_FREQUENCY.equals(qVO.getType())){
                                    handleDisplayLogicFreq(answer, childQuestionVO, childQidCount, logics);
                                }else {
                                    logics.add(new Logic("Question", childQidCount, "no", choiceLocator,
                                            "Selected", childQidCount, choiceLocator, "Expression",
                                            childQuestionVO.getName()));
                                }

                                createManualQuestion(linkModule, linkQuestion,
                                        new DisplayLogic("BooleanExpression", false,new Condition(buildLogicMap(logics), "If")), questionPayloads, qidCount, filterIdNodes);
                            }
                        } else {
                            for (QuestionVO linkQuestion : ((ModuleVO) linkModule).getChildNodes()) {
                                //if (filter != null && !filter.contains(linkQuestion.getIdNode())) {
                                //    continue;
                               // }
                                List<Logic> logics = new ArrayList<>();
                                if(Constant.Q_FREQUENCY.equals(qVO.getType())){
                                    handleDisplayLogicFreq(answer, childQuestionVO, childQidCount, logics);
                                }else {
                                    logics.add(new Logic("Question", childQidCount, "no", choiceLocator,
                                            "Selected", childQidCount, choiceLocator, "Expression",
                                            childQuestionVO.getName()));
                                }
                                createManualQuestion(linkModule, linkQuestion,
                                        new DisplayLogic("BooleanExpression", false,
                                                new Condition(buildLogicMap(logics), "If")), questionPayloads, qidCount,
                                        filter);
                            }
                        }

                    }
                }
            }
            ansCount++;
        }
    }

    private void handleDisplayLogicFreq(PossibleAnswerVO answer, QuestionVO childQuestionVO, String childQidCount, List<Logic> logics) {
        String numbersOnly = answer.getName().replaceAll("[^\\d]", " ");
        String numbersOnlyTrimmed = numbersOnly.trim();
        String commaDelimitedNumbers = numbersOnlyTrimmed.replace( " ", COMMA);
        if(commaDelimitedNumbers.contains(COMMA)){
            String[] separatedNumbers = commaDelimitedNumbers.split(COMMA);
            if(separatedNumbers.length == 2){
                int from = Integer.valueOf(separatedNumbers[0]);
                int to = Integer.valueOf(separatedNumbers[1]);
                int size = to - from;
                for(int i = 1;i <= size;i++){
                    String freqChoiceLocator = "q://" + childQidCount + "/SelectableChoice/" + i;
                    logics.add(new Logic("Question", childQidCount, "no", freqChoiceLocator, "Selected",
                            childQidCount, freqChoiceLocator, "Expression", childQuestionVO.getName(),"Or"));
                }
            }
        }
    }

    private String[] buildChoiceOrder(QuestionVO qVO) {
        if (Constant.Q_FREQUENCY.equals(qVO.getType())){
            String[] choiceOrder = handlePossibleAnswersFrequencyOrder(qVO);
            return choiceOrder;
        }else {
            String[] choiceOrder = handlePossibleAnswersOrder(qVO);
            return choiceOrder;
        }
    }

    private String[] handlePossibleAnswersFrequencyOrder(QuestionVO qVO) {
        if(!qVO.getChildNodes().isEmpty()){
            PossibleAnswerVO answerVO = qVO.getChildNodes().get(0);
            String numbersOnly = answerVO.getName().replaceAll("[^\\d]", " ");
            String numbersOnlyTrimmed = numbersOnly.trim();
            String commaDelimitedNumbers = numbersOnlyTrimmed.replace( " ", COMMA);
            if(commaDelimitedNumbers.contains(COMMA)){
                String[] separatedNumbers = commaDelimitedNumbers.split(COMMA);
                if(separatedNumbers.length == 2){
                    int from = Integer.valueOf(separatedNumbers[0]);
                    int to = Integer.valueOf(separatedNumbers[1]);
                    int size = to-from;
                    String[] choiceOrder = new String[size];
                    int count = 1;
                    for (int i = 0; i < size; i++) {
                        choiceOrder[i] = String.valueOf(count);
                        count++;
                    }
                    return choiceOrder;
                }
            }
        }
        return new String[]{};
    }

    private String[] handlePossibleAnswersOrder(QuestionVO qVO) {
        String[] choiceOrder = new String[qVO.getChildNodes().size()];
        int count = 1;
        for (int i = 0; i < qVO.getChildNodes().size(); i++) {
            choiceOrder[i] = String.valueOf(count);
            count++;
        }
        return choiceOrder;
    }

    // choices should be object s of object and not array
    private List<Choice> buildChoices(QuestionVO qVO, String name, String lang) {
        if (Constant.Q_FREQUENCY.equals(qVO.getType())){
            return handleFrequency(qVO, name, lang);
        }
        if (Constant.Q_SIMPLE.equals(qVO.getType()) || Constant.Q_MULTIPLE.equals(qVO.getType()) || Constant.Q_SINGLE.equals(qVO.getType())){
            return handlePossibleAnswers(qVO, name, lang);
        }
        return new ArrayList<>();
    }

    private List<Choice> handleFrequency(QuestionVO qVO, String name, String lang) {
        List<Choice> choiceList = new ArrayList<>();
        if(!qVO.getChildNodes().isEmpty()){
            PossibleAnswerVO answerVO = qVO.getChildNodes().get(0);
            String numbersOnly = answerVO.getName().replaceAll("[^\\d]", " ");
            String numbersOnlyTrimmed = numbersOnly.trim();
            String commaDelimitedNumbers = numbersOnlyTrimmed.replace( " ", COMMA);
            if(commaDelimitedNumbers.contains(COMMA)){
                String[] separatedNumbers = commaDelimitedNumbers.split(COMMA);
                if(separatedNumbers.length == 2){
                    int from = Integer.valueOf(separatedNumbers[0]);
                    int to = Integer.valueOf(separatedNumbers[1]);
                    for(int i = from;i <= to;i++){
                        PossibleAnswerVO answer = new PossibleAnswerVO();
                        answer.setNumber(answerVO.getNumber());
                        answer.setName(String.valueOf(i));
                        String choiceKey = name.substring(0, 4) + "_" + answerVO.getNumber();
                        String translation = StringUtils.isBlank(lang) ? null : numberTranslationsMap.get(choiceKey).get(lang);
                        choiceList.add(ChoiceFactory.create(answer, name, qualtricsConfig.isHideNodeKeys(), translation));
                    }
                }
            }
        }
        return choiceList;
    }


    private List<Choice> handlePossibleAnswers(QuestionVO qVO, String name, String lang) {
        List<Choice> choiceList = new ArrayList<>();
        for (PossibleAnswerVO answerVO : qVO.getChildNodes()) {
            String choiceKey = name.substring(0, 4) + "_" + answerVO.getNumber();
            String translation = StringUtils.isBlank(lang) ? null : numberTranslationsMap.get(choiceKey).get(lang);
            choiceList.add(ChoiceFactory.create(answerVO, name, qualtricsConfig.isHideNodeKeys(), translation));
        }
        return choiceList;
    }

    private List<BlockElement> buildBlockElements(int qidCount, String name) {
        List<BlockElement> blockElements = new ArrayList<>();
        int count = 0;
        while (count < qidCount) {
            blockElements.add(new BlockElement("Question", "QID" + (++count)));
            blockElements.add(new BlockElement("Page Break", null));
        }
        return blockElements;
    }

    private Object buildPayload(Payload... payloads) {
        if (payloads.length <= 1) {
            return payloads[0];
        }
        List<Payload> payloadList = new ArrayList<>();
        for (Payload payload : payloads) {
            payloadList.add(payload);
        }
        return payloadList;
    }

    private List<Language> buildLanguages(String nodeName, QuestionVO qVO) {
        //each number will be checked against the translation map to get the translations
        String numberKey = nodeName.substring(0, 4) + "_" + qVO.getNumber();
        String questionTextKey = numberKey + " - ";
        Map<String, String> translations = numberTranslationsMap.get(numberKey);
        List<Language> languages = new ArrayList<>();
        translations.forEach((lang, value) -> {
            String hiddenQuestionTextKey = Constant.SPAN_START_DISPLAY_NONE + questionTextKey + Constant.SPAN_END;
            String questionTxt = (qualtricsConfig.isHideNodeKeys() ? hiddenQuestionTextKey : questionTextKey) + value;
            Language language = new Language(lang, questionTxt, buildChoices(qVO, nodeName, lang));
            languages.add(language);
        });
        log.debug("languages={}", languages);
        return languages;
    }

    public boolean isStudyAgentJsonExist(Long idNode) throws IOException {
        String path = "/opt/data";
        File file = new File(path + "/modules/" + idNode + ".json");
        return file.exists();
    }

    public void createStudyAgentJson(String idNode, NodeVO vo, boolean override) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String filePath = createJsonFilePath(idNode);
        File expectedFile = new File(filePath);
        if (expectedFile.exists() && !override) {
            log.info("expected file - " + filePath + " already exist.");
            return;
        } else {
            log.info("[Start] creating file - " + filePath);
            mapper.writeValue(expectedFile, vo);
            log.info("[End] creating file - " + filePath);
        }
    }

    public String[] getStudyAgentCSV(String idNode) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(getCSVFile(idNode)));
        List<String[]> list = reader.readAll();
        reader.close();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    private File getCSVFile(String idNode) {
        String path = "/opt/data";
        File file = new File(path + "/modules/" + idNode + ".csv");
        return file;
    }

    private File getJsonFile(String idNode) {
        String path = "/opt/data";
        File file = new File(path + "/modules/" + idNode + ".json");
        return file;
    }

    public void createStudyAgentCSV(String idNode, List<String> list, boolean override) throws IOException {
        String filePath = createCSVFilePath(idNode);
        File expectedFile = new File(filePath);
        if (expectedFile.exists() && !override) {
            log.info("expected file - " + filePath + " already exist.");
        } else {
            if (list != null && !list.isEmpty()) {
                list = list.stream().sorted().collect(Collectors.toList());
                writeNewCSVFile(list, filePath, expectedFile);
            }
        }
    }

    private void writeNewCSVFile(List<String> list, String filePath, File expectedFile) throws IOException {
        FileWriter writer = new FileWriter(expectedFile);
        log.info("[Start] creating file - " + filePath);
        String commaDelimitedString = String.join(",", list);
        writer.write(commaDelimitedString);
        writer.close();
        log.info("[End] creating file - " + filePath);
    }

    public boolean doesIdNodeExistInArray(String[] arrayToSearch, String idNode) {
        return binarySearch(arrayToSearch, idNode) != -1;
    }

    private int binarySearch(String[] arrayToSearch, String value) {
        int low = 0;
        int high = arrayToSearch.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (arrayToSearch[mid].compareTo(value) < 0) {
                low = mid + 1;
            } else if (arrayToSearch[mid].compareTo(value) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
    }

    private String createCSVFilePath(String idNode) {
        String path = "/opt/data";
        String filePath = path + "/modules/" + idNode + ".csv";
        new File(path + "/modules/").mkdir();
        return filePath;
    }

    private String createJsonFilePath(String idNode) {
        String path = "/opt/data";
        String filePath = path + "/modules/" + idNode + ".json";
        new File(path + "/modules/").mkdir();
        return filePath;
    }

    public void purgeStudyAgentFiles() {
        String path = "/opt/data";
        String filePath = path + "/modules/";
        File dir = new File(filePath);
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            if ("csv".equals(FilenameUtils.getExtension(file.getName()))) {
                file.delete();
                log.info(file.getAbsolutePath() + " has beed deleted.");
            }
        }
    }

    public void deleteStudyAgentJson(String idNode) {
        try {
            SystemPropertyVO autoCreateJson = systemPropertyService.getByName(Constant.AUTO_CREATE_STUDY_AGENT_JSON);
            if (autoCreateJson != null && "true".equals(autoCreateJson.getValue().toLowerCase().trim())) {
                if (doesStudyAgentJsonExist(idNode)) {
                    String path = "/opt/data";
                    String filePath = path + "/modules/" + idNode + ".json";
                    File expectedFile = new File(filePath);
                    if (expectedFile.exists()) {
                        boolean deleted = expectedFile.delete();
                        if (deleted) {
                            log.info(expectedFile + " has beed deleted.");
                        } else {
                            log.error("Unable to delete " + expectedFile);
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            log.error("Error on delete study agent json " + idNode, ex);
        }
    }

    public boolean doesStudyAgentJsonExist(String idNode) throws IOException {
        String filePath = createJsonFilePath(idNode);
        File expectedFile = new File(filePath);
        if (expectedFile.exists()) {
            log.info("expected file - " + filePath + " already exist.");
            return true;
        } else {
            return false;
        }
    }

    public boolean doesStudyAgentCSVExist(String idNode) throws IOException {
        String filePath = createCSVFilePath(idNode);
        File expectedFile = new File(filePath);
        if (expectedFile.exists()) {
            log.info("expected file - " + filePath + " already exist.");
            return true;
        } else {
            return false;
        }
    }

    public NodeVO searchNode(NodeVO nodeVO, Long idNode) throws IOException {
        NodeVO result = null;
        if (nodeVO instanceof ModuleVO) {
            ModuleVO mod = (ModuleVO) nodeVO;
            if (nodeVO.getIdNode() == idNode) {
                return nodeVO;
            } else {
                List<QuestionVO> childNodes = mod.getChildNodes();
                for (QuestionVO qVO : childNodes) {
                    result = searchNode(qVO, idNode);
                    if (result != null) {
                        break;
                    }
                }
            }
        }
        if (nodeVO instanceof FragmentVO) {
            FragmentVO frag = (FragmentVO) nodeVO;
            if (nodeVO.getIdNode() == idNode) {
                return nodeVO;
            } else {
                List<QuestionVO> childNodes = frag.getChildNodes();
                for (QuestionVO qVO : childNodes) {
                    result = searchNode(qVO, idNode);
                    if (result != null) {
                        break;
                    }
                }
            }
        }
        if (nodeVO instanceof QuestionVO) {
            QuestionVO qVO = (QuestionVO) nodeVO;
            if (nodeVO.getIdNode() == idNode) {
                return nodeVO;
            } else {
                List<PossibleAnswerVO> childNodes = qVO.getChildNodes();
                for (PossibleAnswerVO aVO : childNodes) {
                    result = searchNode(aVO, idNode);
                    if (result != null) {
                        break;
                    }
                }
            }
        }
        if (nodeVO instanceof PossibleAnswerVO) {
            PossibleAnswerVO aVO = (PossibleAnswerVO) nodeVO;
            if (nodeVO.getIdNode() == idNode) {
                return nodeVO;
            } else {
                List<QuestionVO> childNodes = aVO.getChildNodes();
                for (QuestionVO qVO : childNodes) {
                    result = searchNode(qVO, idNode);
                    if (result != null) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    public void createStudyAgentForUpdatedNode(long idNode, String name) {
        SystemPropertyVO autoCreateJson = systemPropertyService.getByName(Constant.AUTO_CREATE_STUDY_AGENT_JSON);
        if (autoCreateJson != null && "true".equals(autoCreateJson.getValue().toLowerCase().trim())) {
            NodeVO nodeVO = moduleService.getModuleFilterStudyAgent(idNode);
            if (nodeVO instanceof ModuleVO) {
                ModuleVO moduleFilterStudyAgent = (ModuleVO) nodeVO;
                List<String> listOfIdNodes = new ArrayList<>();
                systemPropertyService.listAllQId(listOfIdNodes, moduleFilterStudyAgent);
                try {
                    createStudyAgentJson(String.valueOf(idNode), moduleFilterStudyAgent, true);
                    createStudyAgentCSV(String.valueOf(idNode), listOfIdNodes, true);
                } catch (Exception e) {
                    log.error("Error creating study agent module json for " + name + "-" + idNode, e);
                }
            } else {
                return;
            }
        }
    }

    public Map<Long, String> getIdNodeQIDMap() {
        return idNodeQIDMap;
    }

    public void setIdNodeQIDMap(Map<Long, String> idNodeQIDMap) {
        this.idNodeQIDMap = idNodeQIDMap;
    }

    public Map<Long, String> getIdNodeQIDMapIntro() {
        return idNodeQIDMapIntro;
    }

    public void setIdNodeQIDMapIntro(Map<Long, String> idNodeQIDMapIntro) {
        this.idNodeQIDMapIntro = idNodeQIDMapIntro;
    }
}
