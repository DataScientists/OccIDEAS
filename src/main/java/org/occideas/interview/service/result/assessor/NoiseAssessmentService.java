package org.occideas.interview.service.result.assessor;

import org.occideas.interview.service.result.AssessmentResultsService;
import org.occideas.vo.AssessmentResult;
import org.occideas.vo.ResponseSummary;
import org.occideas.vo.RuleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoiseAssessmentService extends AssessmentResultsService {

    @Override
    protected AssessmentResult deriveResult(List<Long> agentIds, Map<String, ResponseSummary> responseSummary) {
        List<String> noiseAgents = agentConfig.getIds().get(AgentTypes.noise.name());
        if (Objects.isNull(noiseAgents) || noiseAgents.isEmpty()) {
            return emptyResults();
        }

        List<RuleVO> firedRules = responseSummary.entrySet().stream()
                .filter((entry) -> !entry.getValue().getFiredRules().isEmpty())
                .map((entry) -> entry.getValue().getFiredRules())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<RuleVO> noiseRules = firedRules.stream()
                .filter(rule -> noiseAgents.contains(String.valueOf(rule.getAgent().getIdAgent())))
                .collect(Collectors.toList());

        if (noiseRules.isEmpty()) {
            return emptyResults();
        }

        return emptyResults();
    }
}
