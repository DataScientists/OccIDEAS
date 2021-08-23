package org.occideas.qsf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.occideas.config.QualtricsConfig;
import org.occideas.qsf.payload.*;
import org.occideas.qsf.request.*;
import org.occideas.qsf.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Component
public class QSFClient implements IQSFClient {

    private Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private QualtricsConfig qualtricsConfig;
    private static final String MULTI_FORM = "multipart/form-data";
    private static final String APPLICATION_JSON = "application/json";

    private ObjectMapper JSON_MAPPER = new ObjectMapper();
    private ClientConfig clientConfig = new ClientConfig();

    public QSFClient() {
        JSON_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JSON_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JSON_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        JSON_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JSON_MAPPER.findAndRegisterModules();

        clientConfig.register(new JacksonJsonProvider(JSON_MAPPER));
    }

    @Override
    public Response uploadQSF(File file, String surveyName) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", MULTI_FORM);

        final FileDataBodyPart filePart = new FileDataBodyPart("file", file);
        filePart.type(new javax.ws.rs.core.MediaType("application", "vnd.qualtrics.survey.qsf"));

        MultiPart multiPart = new FormDataMultiPart()
                .field("name", surveyName)
                .bodyPart(filePart);

        SurveyCreateResponse response = ClientBuilder.newBuilder()
                .withConfig(clientConfig)
                .register(MultiPartFeature.class)
                .build()
                .target(qualtricsConfig.getUrl())
                .path("API/v3/surveys")
                .request()
                .accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .headers(headers)
                .post(Entity.entity(multiPart, multiPart.getMediaType()))
                .readEntity(SurveyCreateResponse.class);

        return handleResponse(response, file.getAbsolutePath(), "uploadQSF");
    }

    @Override
    public Response createSurvey(SurveyCreateRequest surveyRequest) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            String request = JSON_MAPPER.writeValueAsString(surveyRequest);
            SurveyCreateResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/survey-definitions")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .post(Entity.entity(request, MediaType.APPLICATION_JSON))
                    .readEntity(SurveyCreateResponse.class);

            return handleResponse(response, request, "createSurvey");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response createQuestion(String surveyId, SimpleQuestionPayload questionPayload, String blockId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);
        try {
            SurveyCreateResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/survey-definitions")
                    .path(surveyId)
                    .path("questions")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .post(Entity.entity(questionPayload.toString(), MediaType.APPLICATION_JSON))
                    .readEntity(SurveyCreateResponse.class);

            return handleResponse(response, questionPayload.toString(), "createQuestion");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response copySurvey(CopySurveyPayload payload, String surveyId, String userId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);
        headers.add("X-COPY-SOURCE",surveyId);
        headers.add("X-COPY-DESTINATION-OWNER",userId);

        try {
            CopySurveyResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/surveys")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .post(Entity.entity(payload.toString(), MediaType.APPLICATION_JSON))
                    .readEntity(CopySurveyResponse.class);

            return handleResponse(response, payload.toString(), "copySurvey");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response getSurveyOptions(String surveyId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            SurveyOptionResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/survey-definitions")
                    .path(surveyId)
                    .path("options")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .get()
                    .readEntity(SurveyOptionResponse.class);

            return handleResponse(response, surveyId, "getOptions");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response updateSurveyOptions(String surveyId, SurveyOptionPayload optionPayload) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            SurveyOptionResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/survey-definitions")
                    .path(surveyId)
                    .path("options")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .put(Entity.entity(optionPayload.toString(), MediaType.APPLICATION_JSON))
                    .readEntity(SurveyOptionResponse.class);

            return handleResponse(response, optionPayload.toString(), "updateOptions");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response createBlock(String surveyId, Default defaultElement) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
//            String request = JSON_MAPPER.writeValueAsString(defaultElement);
            BlockCreateResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/survey-definitions")
                    .path(surveyId)
                    .path("blocks")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .post(Entity.entity(defaultElement.toString(), MediaType.APPLICATION_JSON))
                    .readEntity(BlockCreateResponse.class);

            return handleResponse(response, defaultElement.toString(), "createBlock");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response updateBlock(String surveyId, String blockId, GetBlockElementResult element) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
//            String request = JSON_MAPPER.writeValueAsString(defaultElement);
            BaseResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/survey-definitions")
                    .path(surveyId)
                    .path("blocks")
                    .path(blockId)
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .put(Entity.entity(element.toString(), MediaType.APPLICATION_JSON))
                    .readEntity(BaseResponse.class);

            return handleResponse(response, element.toString(), "updateBlock");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response getBlock(String surveyId, String blockId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            GetBlockElementResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/survey-definitions")
                    .path(surveyId)
                    .path("blocks")
                    .path(blockId)
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .get()
                    .readEntity(GetBlockElementResponse.class);

            return handleResponse(response, "API/v3/survey-definitions/"+surveyId+"/blocks/"+blockId, "updateBlock");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response getFlow(String surveyId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
//            String request = JSON_MAPPER.writeValueAsString(questionPayload);
            GetFlowResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/survey-definitions")
                    .path(surveyId)
                    .path("flow")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .get(GetFlowResponse.class);

            return handleResponse(response, surveyId + "/flow", "getFlow");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response publishSurvey(String surveyId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            String request = JSON_MAPPER.writeValueAsString(new SurveyPublishRequest("publish version via occideas"
                    , true));
            SurveyPublishResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/survey-definitions")
                    .path(surveyId)
                    .path("versions")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .post(Entity.entity(request, MediaType.APPLICATION_JSON))
                    .readEntity(SurveyPublishResponse.class);

            return handleResponse(response, request, "publishSurvey");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response createExportResponse(String surveyId, SurveyExportRequest surveyExportRequest) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            String request = JSON_MAPPER.writeValueAsString(surveyExportRequest);
            SurveyExportResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/surveys")
                    .path(surveyId)
                    .path("export-responses")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .post(Entity.entity(request, MediaType.APPLICATION_JSON))
                    .readEntity(SurveyExportResponse.class);

            return handleResponse(response, "surveyId:"+surveyId+" request:"+request, "createExportResponse");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response getExportResponseProgress(String surveyId, String progressId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            SurveyExportResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/surveys")
                    .path(surveyId)
                    .path("export-responses")
                    .path(progressId)
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .get()
                    .readEntity(SurveyExportResponse.class);

            return handleResponse(response, "surveyId:"+surveyId+"-progressId:"+progressId, "getExportResponseProgress");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public File getExportResponseFile(String surveyId, String fileId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            InputStream inputStream = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/surveys")
                    .path(surveyId)
                    .path("export-responses")
                    .path(fileId)
                    .path("file")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .get()
                    .readEntity(InputStream.class);
            File downloadfile = new File("/tmp/"+surveyId+".zip");
            byte[] byteArray = IOUtils.toByteArray(inputStream);
            FileOutputStream fos = new FileOutputStream(downloadfile);
            fos.write(byteArray);
            fos.flush();
            fos.close();
            IOUtils.closeQuietly(inputStream);
            log.info("surveyId:" + surveyId + "-fileId:" + fileId);
            return downloadfile;
        } catch (Exception e) {
            log.error("getExportResponseFile:" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String getResponse(String responseId, String surveyId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            String response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/surveys")
                    .path(surveyId)
                    .path("responses")
                    .path(responseId)
                    .request(MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .get()
                    .readEntity(String.class);

            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Response listDistribution(String surveyId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        DistributionListResponse response = ClientBuilder.newBuilder()
                .withConfig(clientConfig)
                .build()
                .target(qualtricsConfig.getUrl())
                .path("API/v3/distributions")
                .queryParam("surveyId", surveyId)
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .headers(headers)
                .get()
                .readEntity(DistributionListResponse.class);

        return handleResponse(response, "/list", "listDistribution");
    }

    @Override
    public Response listenToSurveyResponse(String surveyId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        SurveyListenRequest surveyListenerRequest = new SurveyListenRequest();
        surveyListenerRequest.setEncrypt(false);
        surveyListenerRequest.setPublicationUrl(qualtricsConfig.getTopic().getPublicationUrl());
        surveyListenerRequest.setTopics(qualtricsConfig
                .getTopic().getPrefix() + surveyId);
        log.info("surveyListenerRequest sent: {}", surveyListenerRequest);
        String request = null;
        try {
            request = JSON_MAPPER.writeValueAsString(surveyListenerRequest);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        SurveyListenResponse response = ClientBuilder.newBuilder()
                .withConfig(clientConfig)
                .build()
                .target(qualtricsConfig.getUrl())
                .path("API/v3/eventsubscriptions/")
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .headers(headers)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON))
                .readEntity(SurveyListenResponse.class);


        return handleResponse(response, "/list", "listenSurveyResponse");
    }

    @Override
    public Response activateSurvey(String surveyId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            String request = JSON_MAPPER.writeValueAsString(new SurveyUpdateRequest(true));
            BaseResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/surveys")
                    .path(surveyId)
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .put(Entity.entity(request, MediaType.APPLICATION_JSON))
                    .readEntity(BaseResponse.class);

            return handleResponse(response, request, "activateSurvey");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response deleteSurvey(String surveyId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        BaseResponse response = ClientBuilder.newBuilder()
                .withConfig(clientConfig)
                .build()
                .target(qualtricsConfig.getUrl())
                .path("API/v3/surveys")
                .path(surveyId)
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .headers(headers)
                .delete()
                .readEntity(BaseResponse.class);
        log.info("deleting survey "+surveyId);
        return handleResponse(response, surveyId + "/delete", "deleteSurvey");
    }
    
    @Override
    public Response copySurvey(String surveyId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        BaseResponse response = ClientBuilder.newBuilder()
                .withConfig(clientConfig)
                .build()
                .target(qualtricsConfig.getUrl())
                .path("API/v3/surveys")
                .path(surveyId)
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .headers(headers)
                .delete()
                .readEntity(BaseResponse.class);
        log.info("copying survey "+surveyId);
        return handleResponse(response, surveyId + "/delete", "deleteSurvey");
    }

    @Override
    public Response listSurvey() {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        SurveyListResponse response = ClientBuilder.newBuilder()
                .withConfig(clientConfig)
                .build()
                .target(qualtricsConfig.getUrl())
                .path("API/v3/surveys")
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .headers(headers)
                .get()
                .readEntity(SurveyListResponse.class);

        return handleResponse(response, "/list", "listSurvey");
    }


    @Override
    public Response updateFlow(String surveyId, Flow flow) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", qualtricsConfig.getApiToken());
        headers.add("Content-type", APPLICATION_JSON);

        try {
            String request = JSON_MAPPER.writeValueAsString(flow);
            BaseResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(qualtricsConfig.getUrl())
                    .path("API/v3/survey-definitions")
                    .path(surveyId)
                    .path("flow")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .put(Entity.entity(request, MediaType.APPLICATION_JSON))
                    .readEntity(BaseResponse.class);

            return handleResponse(response, request, "updateFlow");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }


    @Override
    public String buildRedirectUrl(String surveyId) {
        return "http://curtin.au1.qualtrics.com/jfe/form/" + surveyId;
    }


    private  Response handleResponse(BaseResponse response, String requestBody, String function) {
        if ("200 - OK".equals(response.getMeta().getHttpStatus())) {
            log.info(function + ":" + response);
            return Response.ok(response).build();
        } else {
            log.error(function + ":" + response.getMeta().getHttpStatus() + "->error:" + response.getMeta().getError().getErrorMessage() + " --> " + requestBody);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(response.getMeta().getHttpStatus()
                    + "->error:" + response.getMeta().getError().getErrorMessage()).build();
        }
    }

//    public static void main(String[] args) {
////        QSFClient qsfClient = new QSFClient();
////        System.out.println(qsfClient.listSurvey());
////        qsfClient.deleteSurvey("SV_aaTp4pm5eEkrgy1");
//    }

}
