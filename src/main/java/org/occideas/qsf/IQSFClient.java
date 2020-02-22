package org.occideas.qsf;

import org.occideas.qsf.payload.Default;
import org.occideas.qsf.payload.Flow;
import org.occideas.qsf.payload.SimpleQuestionPayload;
import org.occideas.qsf.payload.SurveyOptionPayload;
import org.occideas.qsf.request.SurveyCreateRequest;
import org.occideas.qsf.response.GetBlockElementResult;

import javax.ws.rs.core.Response;
import java.io.File;

public interface IQSFClient {

    Response uploadQSF(File file, String surveyName);

    Response createSurvey(SurveyCreateRequest surveyRequest);

    Response createQuestion(String surveyId, SimpleQuestionPayload questionPayload, String blockId);

    Response getSurveyOptions(String surveyId);

    Response updateSurveyOptions(String surveyId, SurveyOptionPayload optionPayload);

    Response createBlock(String surveyId, Default defaultElement);

    Response updateBlock(String surveyId, String blockId, GetBlockElementResult defaultElement);

    Response getBlock(String surveyId, String blockId);

    Response getFlow(String surveyId);

    Response publishSurvey(String surveyId);

    Response activateSurvey(String surveyId);

    Response deleteSurvey(String surveyId);

    Response listSurvey();

    Response updateFlow(String surveyId, Flow flow);

    String buildRedirectUrl(String surveyId);
}
