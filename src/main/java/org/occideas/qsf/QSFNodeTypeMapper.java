package org.occideas.qsf;

import org.occideas.entity.Question;
import org.occideas.exceptions.GenericException;
import org.occideas.qsf.payload.Choice;
import org.occideas.qsf.payload.Logic;
import org.occideas.qsf.service.QuestionAnswerWrapper;

import java.util.*;
import java.util.function.Function;

public enum QSFNodeTypeMapper {

    Q_LINKEDMODULE("Q_linkedmodule", singleSelectionLogic(), singleChoiceCreator(), QSFQuestionType.SINGLE_CHOICE),
    Q_LINKEDAJSM("Q_linkedajsm", singleSelectionLogic(), singleChoiceCreator(), QSFQuestionType.SINGLE_CHOICE),
    Q_FREQUENCY("Q_frequency", singleSelectionLogic(), singleChoiceCreator(), QSFQuestionType.SINGLE_CHOICE),
    Q_SIMPLE("Q_simple", singleSelectionLogic(), singleChoiceCreator(), QSFQuestionType.SINGLE_CHOICE),
    Q_MULTIPLE("Q_multiple", singleSelectionLogic(), multiChoiceCreator(), QSFQuestionType.MULTIPLE_CHOICE),
    Q_SINGLE("Q_single", singleSelectionLogic(), singleChoiceCreator(), QSFQuestionType.SINGLE_CHOICE),
    P_FREETEXT("P_freetext", null, singleChoiceCreator(), QSFQuestionType.TEXT_ENTRY),
    P_FREQUENCY_WEEKS("P_frequencyweeks", null, singleNumberChoiceCreator("Weeks"), QSFQuestionType.TEXT_ENTRY_FORM),
    P_FREQUENCY_HOURS("P_frequencyhours", null, singleNumberChoiceCreator("Hours"), QSFQuestionType.TEXT_ENTRY_FORM),
    P_FREQUENCY_HOURS_MINUTES("P_frequencyhoursminute", null, shiftHoursChoiceCreator(), QSFQuestionType.TEXT_ENTRY_FORM),
    P_FREQUENCY_SHIFT_HOURS("P_frequencyshifthours", null, shiftHoursChoiceCreator(), QSFQuestionType.TEXT_ENTRY_FORM),
    P_FREQUENCY_SECONDS("P_frequencyseconds", null, singleNumberChoiceCreator("Seconds"), QSFQuestionType.TEXT_ENTRY_FORM),
    P_SIMPLE_TYPE("P_simple", null, singleChoiceCreator(), QSFQuestionType.SINGLE_CHOICE);


    QSFNodeTypeMapper(String description, Function<QuestionAnswerWrapper, List<Logic>> buildLogic, Function<Question, QSFQuestionChoices> buildChoices, QSFQuestionType qualtricsType) {
        this.description = description;
        this.buildLogic = buildLogic;
        this.buildChoices = buildChoices;
        this.qualtricsType = qualtricsType;
    }

    private final String description;
    private final Function<QuestionAnswerWrapper, List<Logic>> buildLogic;
    private final Function<Question, QSFQuestionChoices> buildChoices;
    private final QSFQuestionType qualtricsType;

    private static Function<Question, QSFQuestionChoices> singleNumberChoiceCreator(String display) {
        return question -> {
            if (Objects.isNull(question.getChildNodes()) || question.getChildNodes().isEmpty()) {
                throw new GenericException("Question has no child nodes " + question.getIdNode());
            }

            Map<String, Choice> choices = new HashMap<>();
            List<String> choiceOrder = new LinkedList<>();
            choices.put("1", new Choice(display, "on", QSFTextValidation.VALID_NUMBER.getCode()));
            choiceOrder.add("1");
            return new QSFQuestionChoices(choices, choiceOrder);
        };
    }

    private static Function<Question, QSFQuestionChoices> shiftHoursChoiceCreator() {
        return question -> {
            Map<String, Choice> choices = new HashMap<>();
            List<String> choiceOrder = new LinkedList<>();
            choices.put("1", new Choice("Hours", "on", QSFTextValidation.VALID_NUMBER.getCode()));
            choiceOrder.add("1");
            choices.put("2", new Choice("Minutes", "on", QSFTextValidation.VALID_NUMBER.getCode()));
            choiceOrder.add("2");
            return new QSFQuestionChoices(choices, choiceOrder);
        };
    }

    private static Function<Question, QSFQuestionChoices> singleChoiceCreator() {
        return question -> {
            Map<String, Choice> choices = new HashMap<>();

            List<String> choiceOrder = new LinkedList<>();
            question.getChildNodes().forEach(answer -> {
                choices.put(String.valueOf(answer.getIdNode()), new Choice(answer.getName()));
                choiceOrder.add(String.valueOf(answer.getIdNode()));
            });
            return new QSFQuestionChoices(choices, choiceOrder);
        };
    }

    private static Function<Question, QSFQuestionChoices> multiChoiceCreator() {
        return question -> {
            Map<String, Choice> choices = new HashMap<>();

            List<String> choiceOrder = new LinkedList<>();
            question.getChildNodes().forEach(answer -> {
                if (QSFExclusiveAnswer.isAnswerExclusive(answer.getName())) {
                    choices.put(String.valueOf(answer.getIdNode()), new Choice(answer.getName(), true));
                } else {
                    choices.put(String.valueOf(answer.getIdNode()), new Choice(answer.getName()));
                }
                choiceOrder.add(String.valueOf(answer.getIdNode()));
            });
            return new QSFQuestionChoices(choices, choiceOrder);
        };
    }

    private static Function<QuestionAnswerWrapper, List<Logic>> singleSelectionLogic() {
        return questionAnswerWrapper -> {
            List<Logic> logics = new ArrayList<>();
            String choiceLocator = questionAnswerWrapper.getParent().getChoiceSelectors().get(questionAnswerWrapper.getDependsOn());
            String parentQuestions = String.valueOf(questionAnswerWrapper.getParent().getQualtricsQID());
            logics.add(new Logic("Question",
                    parentQuestions,
                    "no",
                    choiceLocator,
                    "Selected",
                    parentQuestions,
                    choiceLocator, "Expression", questionAnswerWrapper.getQuestion().getName()));
            return logics;
        };
    }

    public static QSFNodeTypeMapper getBaseOnType(String questionType, String answerType) {
        if (Objects.isNull(answerType) || Q_MULTIPLE.getDescription().equalsIgnoreCase(questionType)) {
            return Q_MULTIPLE;
        }

        Optional<QSFNodeTypeMapper> nodeTypeOptional = Arrays.stream(QSFNodeTypeMapper.values()).filter(t -> t.description.equalsIgnoreCase(answerType)).findFirst();
        if (nodeTypeOptional.isPresent()) {
            return nodeTypeOptional.get();
        }
        throw new GenericException(answerType + " answerType is unsupported");
    }

    public String getDescription() {
        return description;
    }

    public Function<QuestionAnswerWrapper, List<Logic>> getBuildLogic() {
        return buildLogic;
    }

    public QSFQuestionType getQualtricsType() {
        return qualtricsType;
    }

    public Function<Question, QSFQuestionChoices> getBuildChoices() {
        return buildChoices;
    }
}
