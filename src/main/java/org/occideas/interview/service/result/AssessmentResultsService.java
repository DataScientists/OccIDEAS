package org.occideas.interview.service.result;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.config.AgentConfig;
import org.occideas.entity.Rule;
import org.occideas.exceptions.GenericException;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.vo.AssessmentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public abstract class AssessmentResultsService<T> {

    public static final String N_A = "N/A";
    protected final Logger log = LogManager.getLogger(this.getClass());


    protected enum AgentTypes {
        noise
    }

    @Autowired
    protected AgentConfig agentConfig;
    @Autowired
    protected InterviewDao interviewDao;

    protected abstract AssessmentResult<T> deriveResult(long interviewId,
                                                        List<Long> agentIds,
                                                        List<Rule> firedRules,
                                                        BigDecimal workshift);

    public AssessmentResult<T> getResults(long interviewId, List<Long> agentIds, List<Rule> firedRules, BigDecimal workshift) {
        validateWorkshiftExist(workshift);
        validateAgentConfigExist();
        return deriveResult(interviewId, agentIds, firedRules, workshift);
    }

    protected AssessmentResult<T> emptyResults() {
        return new AssessmentResult<T>(N_A, N_A, N_A, N_A, N_A);
    }

    private void validateWorkshiftExist(BigDecimal workshift) {
        if (Objects.isNull(workshift)) {
            String message = "application.properties qualtrics.node.lastShiftHours could be missing.";
            log.error(message);
            throw new GenericException(message);
        }
    }

    private void validateAgentConfigExist() {
        if (Objects.isNull(agentConfig.getIds()) || agentConfig.getIds().isEmpty()) {
            String message = "application.properties is missing agents props.";
            log.error(message);
            throw new GenericException(message);
        }
    }

}
