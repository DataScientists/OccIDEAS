package org.occideas.qsf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.occideas.qsf.payload.SimpleQuestionPayload;
import org.occideas.qsf.request.SurveyCreateRequest;
import org.occideas.qsf.response.SurveyCreateResponse;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;

@Component
public class QSFClient implements IQSFClient {

    private Logger log = LogManager.getLogger(this.getClass());

    private static final String API_TOKEN = "TwWDAeQCPgsGmXdmSPdlwF1zvUu5txoSzzHGLRmV";
    private static final String MULTI_FORM = "multipart/form-data";
    private static final String APPLICATION_JSON = "application/json";
    private static final String QSF_PATH = "https://au1.qualtrics.com";

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
        headers.add("X-API-TOKEN", API_TOKEN);
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
                .target(QSF_PATH)
                .path("API/v3/surveys")
                .request()
                .accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .headers(headers)
                .post(Entity.entity(multiPart, multiPart.getMediaType()))
                .readEntity(SurveyCreateResponse.class);

        return handleResponse(response);
    }

    @Override
    public Response createSurvey(SurveyCreateRequest surveyRequest) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", API_TOKEN);
        headers.add("Content-type", APPLICATION_JSON);

        try {
            String request = JSON_MAPPER.writeValueAsString(surveyRequest);
            SurveyCreateResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(QSF_PATH)
                    .path("API/v3/survey-definitions")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .post(Entity.entity(request, MediaType.APPLICATION_JSON))
                    .readEntity(SurveyCreateResponse.class);

            return handleResponse(response);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(),e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }

    @Override
    public Response createQuestion(String surveyId, SimpleQuestionPayload questionPayload, String blockId) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
        headers.add("X-API-TOKEN", API_TOKEN);
        headers.add("Content-type", APPLICATION_JSON);

        try {
//            String request = JSON_MAPPER.writeValueAsString(questionPayload);
            SurveyCreateResponse response = ClientBuilder.newBuilder()
                    .withConfig(clientConfig)
                    .build()
                    .target(QSF_PATH)
                    .path("API/v3/survey-definitions")
                    .path(surveyId)
                    .path("questions")
                    .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .post(Entity.entity(questionPayload.toString(), MediaType.APPLICATION_JSON))
                    .readEntity(SurveyCreateResponse.class);

            return handleResponse(response);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
        }
    }


    private Response handleResponse(SurveyCreateResponse response) {
        if ("200 - OK".equals(response.getMeta().getHttpStatus())) {
            log.info(response);
            return Response.ok(response).build();
        } else {
            log.error(response.getMeta().getHttpStatus() + "->error:" + response.getMeta().getError().getErrorMessage());
            return Response.status(Response.Status.BAD_REQUEST).type("text/plain").entity(response.getMeta().getHttpStatus()
                    + "->error:" + response.getMeta().getError().getErrorMessage()).build();
        }
    }

//    public static void main(String[] args){
//        QSFClient qsfClient = new QSFClient();
//        List< Choice > choicesList = new ArrayList<>();
//        choicesList.add(new Choice("Choice created using Survey API!"));
//        choicesList.add(new Choice("green"));
//        choicesList.add(new Choice("blue"));
//        qsfClient.createQuestion("SV_batkH4S8HXAiiix",new SimpleQuestionPayload("test question 2", "Q1",
//                "MC", "SAVR", "TX", new Configuration("UseText"),
//                "What is this", choicesList, new String[]{"1","2","3"},
//                new Validation(new Setting("OFF","ON","None")),
//                new ArrayList<>(),null),null);
//    }

}
