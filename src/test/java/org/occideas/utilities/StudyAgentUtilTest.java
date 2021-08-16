package org.occideas.utilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.occideas.CommonDataGenerator;
import org.occideas.config.QualtricsConfig;
import org.occideas.module.service.ModuleService;
import org.occideas.node.service.INodeService;
import org.occideas.qsf.payload.SimpleQuestionPayload;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudyAgentUtilTest {

    @Mock
    QualtricsConfig qualtricsConfig;
    @Mock
    ModuleService moduleService;
    @Mock
    INodeService nodeService;

    @InjectMocks
    StudyAgentUtil studyAgentUtil;

    @AfterEach
    public void cleanup() {
        CommonDataGenerator.resetIdNodes();
    }

    @Test
    void givenNodeAndQuestionWithoutLinks_whenCreateManualQuestionIntro_shouldPopulateQuestionPayloads() {
        ModuleVO introModule = CommonDataGenerator.createModuleVO(2, 2);
        List<SimpleQuestionPayload> questionPayloads = new ArrayList<>();
        AtomicInteger qidCount = new AtomicInteger(0);
        String surveyId = "survey1";
        Map<Long, String> idNodeQIDMap = new HashMap<>();
        studyAgentUtil.setIdNodeQIDMapIntro(idNodeQIDMap);

        studyAgentUtil.createManualQuestionIntro(introModule,
                introModule.getChildNodes().get(0),
                null,
                questionPayloads,
                surveyId,
                qidCount);

        assertNotNull(questionPayloads);
        assertFalse(questionPayloads.isEmpty());
    }

    @Test
    void givenNodeAndQuestionWithLinksAndExpandedIsTrue_whenProcessLinks_shouldPopulateQuestionPayloadsExpanded() {
        when(qualtricsConfig.isExpandModules()).thenReturn(true);
        ModuleVO introModule = CommonDataGenerator.createModuleVO(2, 2);
        List<SimpleQuestionPayload> questionPayloads = new ArrayList<>();
        AtomicInteger qidCount = new AtomicInteger(0);
        String surveyId = "survey1";
        Map<Long, String> idNodeQIDMap = new HashMap<>();
        studyAgentUtil.setIdNodeQIDMapIntro(idNodeQIDMap);
        long linkId = 100L;
        QuestionVO question = introModule.getChildNodes().get(0);
        List<PossibleAnswerVO> childAnswers = CommonDataGenerator.createPossibleAnswerVOs(1, 1);
        childAnswers.forEach(child -> child.setParentId(String.valueOf(question.getIdNode())));
        question.setChildNodes(childAnswers);
        QuestionVO parentQuestion = childAnswers.get(0).getChildNodes().get(0);
        parentQuestion.setLink(linkId);
        List<String> listOfIdNodes = Arrays.asList("101L", "102L");
        when(moduleService.getFilterStudyAgent(linkId)).thenReturn(listOfIdNodes);
        ModuleVO linkModule = CommonDataGenerator.createModuleVO(2, 2);
        linkModule.getChildNodes().get(0).setIdNode(101L);
        linkModule.getChildNodes().get(0).setIdNode(102L);
        when(nodeService.getNode(linkId)).thenReturn(linkModule);

        introModule.getChildNodes().forEach(childQuesion -> {
            studyAgentUtil.createManualQuestionIntro(introModule,
                    childQuesion,
                    null,
                    questionPayloads,
                    surveyId,
                    qidCount);
        });


        assertNotNull(questionPayloads);
        assertFalse(questionPayloads.isEmpty());
        SimpleQuestionPayload firstQuestionPayloadFromLink = questionPayloads.get(3);
        assertNotNull(firstQuestionPayloadFromLink.getLogic());
        assertEquals("q://QID1/SelectableChoice/1", firstQuestionPayloadFromLink.getLogic().getCondition().getLogic().get(0).getChoiceLocator());
        SimpleQuestionPayload secondQuestionPayloadFromLink = questionPayloads.get(4);
        assertNotNull(secondQuestionPayloadFromLink.getLogic());
        assertEquals("q://QID1/SelectableChoice/1", secondQuestionPayloadFromLink.getLogic().getCondition().getLogic().get(0).getChoiceLocator());
        assertEquals(6, questionPayloads.size());
    }

}