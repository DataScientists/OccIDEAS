package org.occideas.interview.service.result;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.config.AgentConfig;
import org.occideas.exceptions.GenericException;
import org.occideas.vo.AssessmentResult;
import org.occideas.vo.ResponseSummary;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AssessmentResultsService {

    protected final Logger log = LogManager.getLogger(this.getClass());

    protected enum AgentTypes {
        noise
    }

    @Autowired
    protected AgentConfig agentConfig;

    protected abstract AssessmentResult deriveResult(List<Long> agentIds, Map<String, ResponseSummary> responseSummary);

    public AssessmentResult getResults(List<Long> agentIds, Map<String, ResponseSummary> responseSummary) {
        validateAgentConfigExist();

        return deriveResult(agentIds, responseSummary);
    }

    protected AssessmentResult emptyResults() {
        return new AssessmentResult("This feature is under development.");
    }


    private void validateAgentConfigExist() {
        if (Objects.isNull(agentConfig.getIds()) || agentConfig.getIds().isEmpty()) {
            log.error("application.properties is missing agents props.");
            throw new GenericException("application.properties is missing agents props.");
        }
    }

}
