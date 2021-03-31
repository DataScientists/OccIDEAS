package org.occideas.qsf.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.occideas.qsf.payload.AvailableLanguage;

import java.io.IOException;
import java.util.ArrayList;

public class AvailableLanguageSerializer extends JsonSerializer<AvailableLanguage> {

    @Override
    public void serialize(AvailableLanguage availableLanguage, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("EN", new ArrayList<>());
        for (String lang : availableLanguage.getLanguages()) {
            jsonGenerator.writeObjectField(lang, new ArrayList<>());
        }
        jsonGenerator.writeEndObject();
    }
}
