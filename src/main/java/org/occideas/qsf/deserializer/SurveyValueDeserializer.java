package org.occideas.qsf.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.occideas.qsf.response.SurveyValue;

import java.io.IOException;

public class SurveyValueDeserializer extends JsonDeserializer<SurveyValue> {

    @Override
    public SurveyValue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return new SurveyValue();
    }
}
