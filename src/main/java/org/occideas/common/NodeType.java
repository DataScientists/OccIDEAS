package org.occideas.common;

import org.occideas.exceptions.GenericException;
import org.occideas.qsf.QSFQuestionType;
import org.occideas.qsf.payload.Logic;
import org.occideas.qsf.service.QuestionAnswerWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public enum NodeType {

    Q_LINKEDMODULE("Q_linkedmodule", singleSelectionLogic(), QSFQuestionType.MULTIPLE_CHOICE),
    Q_LINKEDAJSM("Q_linkedajsm", singleSelectionLogic(), QSFQuestionType.MULTIPLE_CHOICE),
    Q_FREQUENCY("Q_frequency", singleSelectionLogic(), QSFQuestionType.MULTIPLE_CHOICE),
    Q_SIMPLE("Q_simple", singleSelectionLogic(), QSFQuestionType.MULTIPLE_CHOICE),
    Q_MULTIPLE("Q_multiple", singleSelectionLogic(), QSFQuestionType.MULTIPLE_CHOICE),
    Q_SINGLE("Q_single", singleSelectionLogic(), QSFQuestionType.MULTIPLE_CHOICE),
    P_FREETEXT("P_freetext", null, QSFQuestionType.TEXT_ENTRY),
    P_FREQUENCY_WEEKS("P_frequencyweeks", null, QSFQuestionType.TEXT_ENTRY),
    P_FREQUENCY_HOURS("P_frequencyhours", null, QSFQuestionType.TEXT_ENTRY),
    P_FREQUENCY_HOURS_MINUTES("P_frequencyhoursminute", null, QSFQuestionType.TEXT_ENTRY),
    P_FREQUENCY_SHIFT_HOURS("P_frequencyshifthours", null, QSFQuestionType.TEXT_ENTRY),
    P_FREQUENCY_SECONDS("P_frequencyseconds", null, QSFQuestionType.TEXT_ENTRY),
    P_SIMPLE_TYPE("P_simple", null, QSFQuestionType.MULTIPLE_CHOICE);


    NodeType(String description, Function<QuestionAnswerWrapper, List<Logic>> buildLogic, QSFQuestionType qualtricsType) {
        this.description = description;
        this.buildLogic = buildLogic;
        this.qualtricsType = qualtricsType;
    }

    private String description;
    private Function<QuestionAnswerWrapper, List<Logic>> buildLogic;
    private QSFQuestionType qualtricsType;

    private static Function<QuestionAnswerWrapper, List<Logic>> singleSelectionLogic() {
        return questionAnswerWrapper -> {
            List<Logic> logics = new ArrayList<>();
            String choiceLocator = questionAnswerWrapper.getParent().getChoiceSelectors().get(questionAnswerWrapper.getDependsOn());
            String parentQuestions = String.valueOf(questionAnswerWrapper.getParent().getQuestion().getIdNode());
            logics.add(new Logic("Question",
                    parentQuestions,
                    "no",
                    choiceLocator, "Selected",
                    parentQuestions,
                    choiceLocator, "Expression", questionAnswerWrapper.getQuestion().getName()));
            return logics;
        };
    }

    public static NodeType getBaseOnType(String type) {
        Optional<NodeType> nodeTypeOptional = Arrays.stream(NodeType.values()).filter(t -> t.description.equalsIgnoreCase(type)).findFirst();
        if (nodeTypeOptional.isPresent()) {
            return nodeTypeOptional.get();
        }
        throw new GenericException(type + " type is unsupported");
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
}
