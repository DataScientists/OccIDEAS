package org.occideas.qsf;

import org.occideas.qsf.payload.*;
import org.occideas.qsf.request.SurveyCreateRequest;
import org.occideas.qsf.request.SurveyExportRequest;
import org.occideas.qsf.response.GetBlockElementResult;

import javax.ws.rs.core.Response;
import java.io.File;

public interface IQSFClient {

    Response uploadQSF(File file, String surveyName);

    Response createSurvey(SurveyCreateRequest surveyRequest);

    Response getSurveyMetadata(String surveyId);

    Response createQuestion(String surveyId, SimpleQuestionPayload questionPayload, String blockId);

    Response copySurvey(CopySurveyPayload payload, String surveyId, String userId);

    Response getSurveyOptions(String surveyId);

    Response updateSurveyOptions(String surveyId, SurveyOptionPayload optionPayload);

    Response createBlock(String surveyId, Default defaultElement);

    Response updateBlock(String surveyId, String blockId, GetBlockElementResult defaultElement);

    Response getBlock(String surveyId, String blockId);

    Response getFlow(String surveyId);

    Response publishSurvey(String surveyId);

    Response activateSurvey(String surveyId);

    Response deleteSurvey(String surveyId);
    
    Response copySurvey(String surveyId);

    Response listSurvey();

    Response updateFlow(String surveyId, Flow flow);

    Response createExportResponse(String surveyId, SurveyExportRequest request);

    String buildRedirectUrl(String surveyId);

    Response getExportResponseProgress(String surveyId, String progressId);


    File getExportResponseFile(String surveyId, String fileId);

    String getResponse(String responseId, String surveyId);

    Response listDistribution(String surveyId);

    Response listenToSurveyResponse(String surveyId);
}
