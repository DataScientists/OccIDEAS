package org.occideas.qsf.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.occideas.qsf.response.SurveyValue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SurveyValueDeserializerTest {

    @Test
    public void givenSurveyValueJSON_whenDeserialize_shouldReturnSurveyValue() throws JsonProcessingException {
        String sampleJson = getSampleJson();

        ObjectMapper objectMapper = new ObjectMapper();
        SurveyValue actual = objectMapper.readValue(sampleJson, SurveyValue.class);

        assertEquals("sample@gmail.com", actual.getRecipientEmail());
    }

    private String getSampleJson() {
        return "{\n" +
                "      \"startDate\": \"2021-08-09T02:19:43Z\",\n" +
                "      \"endDate\": \"2021-08-09T02:20:59Z\",\n" +
                "      \"status\": 0,\n" +
                "      \"ipAddress\": \"219.74.213.197\",\n" +
                "      \"progress\": 100,\n" +
                "      \"duration\": 75,\n" +
                "      \"finished\": 1,\n" +
                "      \"recordedDate\": \"2021-08-09T02:21:00.263Z\",\n" +
                "      \"recipientLastName\": \"lastname\",\n" +
                "      \"recipientFirstName\": \"firstname\",\n" +
                "      \"recipientEmail\": \"sample@gmail.com\",\n" +
                "      \"locationLatitude\": \"1.30650329590\",\n" +
                "      \"locationLongitude\": \"103.889892578\",\n" +
                "      \"distributionChannel\": \"email\",\n" +
                "      \"userLanguage\": \"EN\",\n" +
                "      \"QID1\": 2,\n" +
                "      \"QID1_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID11\": 2,\n" +
                "      \"QID11_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID22\": 2,\n" +
                "      \"QID22_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID25\": 2,\n" +
                "      \"QID25_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID32\": 2,\n" +
                "      \"QID32_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID34\": 2,\n" +
                "      \"QID34_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID45\": 2,\n" +
                "      \"QID45_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID49\": 2,\n" +
                "      \"QID49_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID52\": 2,\n" +
                "      \"QID52_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID55\": 2,\n" +
                "      \"QID55_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID60\": 2,\n" +
                "      \"QID60_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID67\": 2,\n" +
                "      \"QID67_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID106\": 2,\n" +
                "      \"QID106_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID125\": 2,\n" +
                "      \"QID125_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID129\": 2,\n" +
                "      \"QID129_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID144\": 2,\n" +
                "      \"QID144_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID162\": 2,\n" +
                "      \"QID162_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID164\": 2,\n" +
                "      \"QID164_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID166\": 2,\n" +
                "      \"QID166_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID169\": 2,\n" +
                "      \"QID169_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ],\n" +
                "      \"QID171\": 2,\n" +
                "      \"QID171_DO\": [\n" +
                "        \"1\",\n" +
                "        \"2\",\n" +
                "        \"3\"\n" +
                "      ]\n" +
                "    }";
    }
}