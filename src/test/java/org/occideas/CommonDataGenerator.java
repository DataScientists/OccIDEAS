package org.occideas;

import org.occideas.entity.JobModule;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.mapper.ModuleMapperImpl;
import org.occideas.mapper.PossibleAnswerMapperImpl;
import org.occideas.qsf.QSFNodeTypeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CommonDataGenerator {

    private static final AtomicInteger idNode = new AtomicInteger();
    private static String[] letters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    private static ModuleMapperImpl moduleMapper = new ModuleMapperImpl();
    private static PossibleAnswerMapperImpl answerMapper = new PossibleAnswerMapperImpl();


    public static JobModule createModule(int numberOfChildQuestions, int numberOfAnswers) {
        JobModule module = new JobModule();
        long generatedIdNode = CommonDataGenerator.idNode.incrementAndGet();
        module.setName("Sample Intro Module " + generatedIdNode);
        module.setIdNode(generatedIdNode);
        module.setChildNodes(createQuestions(numberOfChildQuestions, numberOfAnswers));
        return module;
    }

    public static Question createQuestionPSimple(String nodeNumber, String type, int numberOfAnswers) {
        Question question = new Question();
        long generatedIdNode = CommonDataGenerator.idNode.incrementAndGet();
        question.setIdNode(generatedIdNode);
        question.setName("Sample Question " + generatedIdNode);
        question.setNumber(nodeNumber);
        question.setType(type);
        question.setChildNodes(createPossibleAnswer(numberOfAnswers, "P_simple"));
        return question;
    }

    public static Question createQuestionPFreetext(String nodeNumber, String type, int numberOfAnswers) {
        Question question = new Question();
        long generatedIdNode = CommonDataGenerator.idNode.incrementAndGet();
        question.setIdNode(generatedIdNode);
        question.setName("Sample Question " + generatedIdNode);
        question.setNumber(nodeNumber);
        question.setType(type);
        question.setChildNodes(createPossibleAnswer(numberOfAnswers, "P_freetext"));
        return question;
    }


    public static List<Question> createQuestions(int numberOfQuestions, int numberOfAnswers) {
        List<Question> questions = new ArrayList<>();
        int letterInd = 0;
        for (int i = 0; i < numberOfQuestions; i++) {
            if (letterInd == letters.length - 1) {
                letterInd = 0;
            }
            questions.add(createQuestionPSimple(i + 1 + letters[letterInd], QSFNodeTypeMapper.Q_MULTIPLE.getDescription(), numberOfAnswers));
        }
        return questions;
    }

    public static PossibleAnswer createPossibleAnswer(String nodeNumber, String type) {
        PossibleAnswer possibleAnswer = new PossibleAnswer();
        long generatedIdNode = CommonDataGenerator.idNode.incrementAndGet();
        possibleAnswer.setIdNode(generatedIdNode);
        possibleAnswer.setName("Sample Answer " + generatedIdNode);
        possibleAnswer.setNumber(nodeNumber);
        possibleAnswer.setType(type);
        return possibleAnswer;
    }

    public static PossibleAnswer createPossibleAnswer(String nodeNumber, int numberOfChildQuestions, int numberOfChildAnswers) {
        PossibleAnswer possibleAnswer = new PossibleAnswer();
        long generatedIdNode = CommonDataGenerator.idNode.incrementAndGet();
        possibleAnswer.setIdNode(generatedIdNode);
        possibleAnswer.setName("Sample Answer " + generatedIdNode);
        possibleAnswer.setNumber(nodeNumber);
        possibleAnswer.setChildNodes(createQuestions(numberOfChildQuestions, numberOfChildAnswers));
        return possibleAnswer;
    }

    public static List<PossibleAnswer> createPossibleAnswer(int numberOfAnswers, String type) {
        List<PossibleAnswer> possibleAnswers = new ArrayList<>();
        int letterInd = 0;
        for (int i = 0; i < numberOfAnswers; i++) {
            if (letterInd == letters.length - 1) {
                letterInd = 0;
            }
            possibleAnswers.add(createPossibleAnswer(i + 1 + letters[letterInd], type));
        }
        return possibleAnswers;
    }

    public static List<PossibleAnswer> createPossibleAnswers(int numberOfChildAnswers, int numberOfChildQuestions) {
        List<PossibleAnswer> possibleAnswers = new ArrayList<>();
        int letterInd = 0;
        for (int i = 0; i < numberOfChildAnswers; i++) {
            if (letterInd == letters.length - 1) {
                letterInd = 0;
            }
            possibleAnswers.add(createPossibleAnswer(i + 1 + letters[letterInd], numberOfChildQuestions, numberOfChildAnswers));
        }
        return possibleAnswers;
    }

    public static void resetIdNodes() {
        idNode.set(0);
    }
}
