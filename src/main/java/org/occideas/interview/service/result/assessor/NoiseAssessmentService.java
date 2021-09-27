package org.occideas.interview.service.result.assessor;

import org.apache.commons.lang3.math.NumberUtils;
import org.occideas.entity.*;
import org.occideas.interview.service.result.AssessmentResultsService;
import org.occideas.interview.service.result.view.NoiseView;
import org.occideas.interviewanswer.dao.InterviewAnswerDao;
import org.occideas.interviewquestion.dao.InterviewQuestionDao;
import org.occideas.vo.AssessmentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoiseAssessmentService extends AssessmentResultsService<NoiseView> {

    @Autowired
    private InterviewAnswerDao interviewAnswerDao;
    @Autowired
    private InterviewQuestionDao interviewQuestionDao;

    @Override
    protected AssessmentResult<NoiseView> deriveResult(long interviewId, List<Long> agentIds, List<Rule> firedRules, BigDecimal shiftHours) {

        List<String> noiseAgents = agentConfig.getIds().get(AgentTypes.noise.name());
        if (Objects.isNull(noiseAgents) || noiseAgents.isEmpty() || Objects.isNull(firedRules) || firedRules.isEmpty()) {
            return emptyResults();
        }

        List<Rule> noiseRules = firedRules.stream()
                .filter(rule -> noiseAgents.contains(String.valueOf(rule.getAgent().getIdAgent())))
                .collect(Collectors.toList());

        if (noiseRules.isEmpty()) {
            return emptyResults();
        }

        BigDecimal totalPartialExposure = BigDecimal.ZERO;
        BigDecimal maxBackgroundPartialExposure = BigDecimal.ZERO;
        BigDecimal maxBackgroundHours = BigDecimal.ZERO;

        List<NoiseView> noiseRow = new ArrayList<>();

        List<InterviewAnswer> answerHistory = interviewAnswerDao.findByInterviewId(interviewId);
        List<InterviewQuestion> questionHistory = interviewQuestionDao.findByInterviewId(interviewId);
        BigDecimal totalFrequency = getTotalFrequency(noiseRules, questionHistory);

        boolean useRatio = totalFrequency.compareTo(shiftHours) > 0;
        BigDecimal ratio = getRatioValue(shiftHours, totalFrequency, useRatio);

        BigDecimal level = BigDecimal.ZERO;
        BigDecimal peakNoise = BigDecimal.ZERO;
        for (Rule rule : noiseRules) {
            if ("BACKGROUND".equalsIgnoreCase(rule.getType())) {
                BigDecimal hoursBg = deriveBackgroundHours(shiftHours, totalFrequency);
                if (hoursBg.compareTo(BigDecimal.ZERO) < 0) {
                    hoursBg = BigDecimal.ZERO;
                }
                if (Objects.nonNull(rule.getRuleAdditionalfields()) && !rule.getRuleAdditionalfields().isEmpty()) {
                    level = new BigDecimal(Integer.valueOf(rule.getRuleAdditionalfields().get(0).getValue()));
                }
                BigDecimal partialExposure = derivePartialExposure(level, hoursBg).setScale(15, RoundingMode.CEILING);
                NoiseView noiseView = new NoiseView("backgroundNoise",
                        rule.getConditions().get(0).getNumber(),
                        level.toPlainString() + "B",
                        hoursBg.toPlainString(),
                        partialExposure.toPlainString(),
                        rule.getConditions().get(0).getIdNode(),
                        rule.getConditions().get(0).getName(),
                        getModuleNameOfNode(rule.getConditions().get(0).getTopNodeId()),
                        rule.getConditions().get(0).getTopNodeId()
                );
                noiseRow.add(noiseView);

                if (partialExposure.compareTo(maxBackgroundPartialExposure) > 0) {
                    maxBackgroundPartialExposure = partialExposure;
                    maxBackgroundHours = hoursBg;
                }
            } else {
                BigDecimal hours;
                BigDecimal frequencyhours = BigDecimal.ZERO;
                if (Objects.nonNull(rule.getConditions()) && !rule.getConditions().isEmpty()) {
                    PossibleAnswer parentAnswer = rule.getConditions().get(0);
                    List<Question> childQuestions = parentAnswer.getChildNodes();
                    if (Objects.nonNull(childQuestions) && !childQuestions.isEmpty()) {
                        List<PossibleAnswer> answers = childQuestions.get(0).getChildNodes();
                        if (Objects.nonNull(answers) && !answers.isEmpty()) {
                            PossibleAnswer frequencyAnswer = answers.get(0);
                            Optional<InterviewAnswer> answerOption = answerHistory.stream().filter(answer -> answer.getAnswerId() == frequencyAnswer.getIdNode()).findFirst();
                            if (answerOption.isPresent() && NumberUtils.isNumber(answerOption.get().getAnswerFreetext())) {
                                frequencyhours = frequencyhours.add(new BigDecimal(answerOption.get().getAnswerFreetext()));
                                if ("P_frequencyseconds".equalsIgnoreCase(answerOption.get().getType())) {
                                    frequencyhours = frequencyhours.divide(new BigDecimal(3600), 15, RoundingMode.CEILING); //convert seconds to hours
                                }
                            }
                        }
                    }

                }
                if (useRatio) {
                    hours = frequencyhours.divide(ratio, 15, RoundingMode.CEILING);
                } else {
                    hours = frequencyhours.setScale(15, RoundingMode.CEILING);
                }

                if (hours.compareTo(BigDecimal.ZERO) < 0) {
                    hours = new BigDecimal(0.0);
                }

                if (rule.getRuleAdditionalfields() != null && !rule.getRuleAdditionalfields().isEmpty()) {
                    level = new BigDecimal(Integer.valueOf(rule.getRuleAdditionalfields().get(0).getValue()));
                }
                BigDecimal partialExposure = derivePartialExposure(level, hours).setScale(15, RoundingMode.CEILING);

                String modHours;
                if (useRatio) {
                    modHours = "*" + hours.toPlainString() + "*";
                } else {
                    modHours = hours.toPlainString();
                }

                NoiseView noiseView = new NoiseView("",
                        rule.getConditions().get(0).getNumber(),
                        level.toPlainString(),
                        modHours,
                        partialExposure.toPlainString(),
                        rule.getConditions().get(0).getIdNode(),
                        rule.getConditions().get(0).getName(),
                        getModuleNameOfNode(rule.getConditions().get(0).getTopNodeId()),
                        rule.getConditions().get(0).getTopNodeId()
                );
                noiseRow.add(noiseView);

                totalPartialExposure = totalPartialExposure.add(partialExposure).setScale(15, RoundingMode.CEILING);
            }
            if (peakNoise.compareTo(level) < 0) {
                if (totalPartialExposure.compareTo(BigDecimal.ZERO) > 0) {
                    peakNoise = new BigDecimal(level.toPlainString());
                }
            }
        }

        totalPartialExposure = totalPartialExposure.add(maxBackgroundPartialExposure);
        totalFrequency = totalFrequency.add(maxBackgroundHours);

//        BigDecimal autoExposureLevel = deriveAutoExposure(totalPartialExposure);

        AssessmentResult<NoiseView> noiseViewAssessmentResult =
                new AssessmentResult<>(shiftHours.toPlainString(), totalPartialExposure.toPlainString(),
                        null,
                        peakNoise.setScale(15, RoundingMode.CEILING).toPlainString(),
                        totalFrequency.setScale(15, RoundingMode.CEILING).toPlainString());
        noiseViewAssessmentResult.setResults(noiseRow);

        return noiseViewAssessmentResult;
    }

    protected BigDecimal deriveBackgroundHours(BigDecimal shiftHours, BigDecimal totalFrequency) {
        return shiftHours.subtract(totalFrequency).setScale(15, RoundingMode.CEILING);
    }

    protected BigDecimal deriveAutoExposure(BigDecimal totalPartialExposure) {
        if (totalPartialExposure.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal formula = new BigDecimal(Math.log10(totalPartialExposure.divide(new BigDecimal(3.2)
                        .multiply(new BigDecimal(Math.pow(10, -9)).setScale(15, RoundingMode.CEILING))
                        .setScale(15, RoundingMode.CEILING))
                .setScale(15, RoundingMode.CEILING).doubleValue())).setScale(15, RoundingMode.CEILING);
        return new BigDecimal(10)
                .multiply(formula)
                .setScale(15, RoundingMode.CEILING)
                .setScale(15, RoundingMode.CEILING);
    }

    private String getModuleNameOfNode(long topNodeId) {
        List<InterviewQuestion> byLinkId = interviewQuestionDao.findByLinkId(topNodeId);
        if (!byLinkId.isEmpty()) {
            return byLinkId.get(0).getName().substring(0, 4);
        }
        return "";
    }

    protected BigDecimal derivePartialExposure(BigDecimal level, BigDecimal hoursBg) {
        BigDecimal partialFormula1 = new BigDecimal(4).multiply(hoursBg);
        BigDecimal partialFormula2 = new BigDecimal(Math.pow(10, (level.subtract(new BigDecimal(100)).divide(new BigDecimal(10)))
                .doubleValue())).setScale(15, RoundingMode.CEILING);
        return partialFormula1.multiply(
                        partialFormula2)
                .setScale(15, RoundingMode.CEILING);
    }

    private BigDecimal getRatioValue(BigDecimal shiftHours, BigDecimal totalFrequency, boolean useRatio) {
        BigDecimal ratio = new BigDecimal(1.0).setScale(15, RoundingMode.CEILING);
        if (useRatio) {
            ratio = totalFrequency.divide(shiftHours, 15, RoundingMode.CEILING);
        }
        return ratio;
    }

    private BigDecimal getTotalFrequency(List<Rule> noiseRules, List<InterviewQuestion> questionHistory) {
        List<Rule> nonBackgroundRules = noiseRules.stream()
                .filter(noiseRule -> !"BACKGROUND".equalsIgnoreCase(noiseRule.getType()))
                .collect(Collectors.toList());
        BigDecimal totalFrequency = BigDecimal.ZERO;
        for (Rule noiseRule : nonBackgroundRules) {
            PossibleAnswer parentNode = noiseRule.getConditions().get(0);
            Optional<InterviewQuestion> childQuestion = questionHistory.stream()
                    .filter(question -> question.getQuestionId() == parentNode.getChildNodes().get(0).getIdNode())
                    .filter(question -> "Q_frequency".equalsIgnoreCase(question.getType()))
                    .findFirst();
            if (childQuestion.isPresent()) {
                InterviewAnswer interviewAnswer = childQuestion.get().getAnswers().get(0);
                if (!"P_frequencyseconds".equalsIgnoreCase(interviewAnswer.getType())) {
                    if (NumberUtils.isNumber(interviewAnswer.getAnswerFreetext())) {
                        totalFrequency = totalFrequency.add(new BigDecimal(interviewAnswer.getAnswerFreetext()));
                    }
                }
            }
        }
        return totalFrequency;
    }


}
