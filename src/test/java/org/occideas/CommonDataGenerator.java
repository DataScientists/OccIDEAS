package org.occideas;

import org.occideas.vo.ModuleVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CommonDataGenerator {

    private static final AtomicInteger idNode = new AtomicInteger();
    private static String[] letters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static ModuleVO createModuleVO(int numberOfChildQuestions, int numberOfAnswers) {
        ModuleVO moduleVO = new ModuleVO();
        long generatedIdNode = CommonDataGenerator.idNode.incrementAndGet();
        moduleVO.setName("Sample Intro Module " + generatedIdNode);
        moduleVO.setIdNode(generatedIdNode);
        moduleVO.setChildNodes(createQuestionVOs(numberOfChildQuestions, numberOfAnswers));
        return moduleVO;
    }

    public static QuestionVO createQuestionVO(String nodeNumber, int numberOfAnswers) {
        QuestionVO questionVO = new QuestionVO();
        long generatedIdNode = CommonDataGenerator.idNode.incrementAndGet();
        questionVO.setIdNode(generatedIdNode);
        questionVO.setName("Sample Question " + generatedIdNode);
        questionVO.setNumber(nodeNumber);
        questionVO.setChildNodes(createPossibleAnswerVOs(numberOfAnswers));
        return questionVO;
    }

    public static List<QuestionVO> createQuestionVOs(int numberOfQuestions, int numberOfAnswers) {
        List<QuestionVO> questions = new ArrayList<>();
        int letterInd = 0;
        for (int i = 0; i < numberOfQuestions; i++) {
            if (letterInd == letters.length - 1) {
                letterInd = 0;
            }
            questions.add(createQuestionVO(i + 1 + letters[letterInd], numberOfAnswers));
        }
        return questions;
    }

    public static PossibleAnswerVO createPossibleAnswerVO(String nodeNumber) {
        PossibleAnswerVO possibleAnswerVO = new PossibleAnswerVO();
        long generatedIdNode = CommonDataGenerator.idNode.incrementAndGet();
        possibleAnswerVO.setIdNode(generatedIdNode);
        possibleAnswerVO.setName("Sample Answer " + generatedIdNode);
        possibleAnswerVO.setNumber(nodeNumber);
        return possibleAnswerVO;
    }

    public static PossibleAnswerVO createPossibleAnswerVO(String nodeNumber, int numberOfChildQuestions, int numberOfChildAnswers) {
        PossibleAnswerVO possibleAnswerVO = new PossibleAnswerVO();
        long generatedIdNode = CommonDataGenerator.idNode.incrementAndGet();
        possibleAnswerVO.setIdNode(generatedIdNode);
        possibleAnswerVO.setName("Sample Answer " + generatedIdNode);
        possibleAnswerVO.setNumber(nodeNumber);
        possibleAnswerVO.setChildNodes(createQuestionVOs(numberOfChildQuestions, numberOfChildAnswers));
        return possibleAnswerVO;
    }

    public static List<PossibleAnswerVO> createPossibleAnswerVOs(int numberOfAnswers) {
        List<PossibleAnswerVO> possibleAnswers = new ArrayList<>();
        int letterInd = 0;
        for (int i = 0; i < numberOfAnswers; i++) {
            if (letterInd == letters.length - 1) {
                letterInd = 0;
            }
            possibleAnswers.add(createPossibleAnswerVO(i + 1 + letters[letterInd]));
        }
        return possibleAnswers;
    }

    public static List<PossibleAnswerVO> createPossibleAnswerVOs(int numberOfChildAnswers, int numberOfChildQuestions) {
        List<PossibleAnswerVO> possibleAnswers = new ArrayList<>();
        int letterInd = 0;
        for (int i = 0; i < numberOfChildAnswers; i++) {
            if (letterInd == letters.length - 1) {
                letterInd = 0;
            }
            possibleAnswers.add(createPossibleAnswerVO(i + 1 + letters[letterInd], numberOfChildQuestions, numberOfChildAnswers));
        }
        return possibleAnswers;
    }

    public static void resetIdNodes() {
        idNode.set(0);
    }
}
