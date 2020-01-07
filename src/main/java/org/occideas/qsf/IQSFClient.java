package org.occideas.qsf;

import org.occideas.qsf.payload.Flow;
import org.occideas.qsf.payload.SimpleQuestionPayload;
import org.occideas.qsf.request.SurveyCreateRequest;

import javax.ws.rs.core.Response;
import java.io.File;

public interface IQSFClient {

    Response uploadQSF(File file, String surveyName);

    Response createSurvey(SurveyCreateRequest surveyRequest);

    Response createQuestion(String surveyId, SimpleQuestionPayload questionPayload, String blockId);

    Response getFlow(String surveyId);

    Response publishSurvey(String surveyId);

    Response activateSurvey(String surveyId);

    Response deleteSurvey(String surveyId);

    Response listSurvey();

    Response updateFlow(String surveyId, Flow flow);

    String buildRedirectUrl(String surveyId);
}
