package org.occideas.qsf.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.occideas.qsf.payload.Choice;
import org.occideas.qsf.payload.Language;

import java.io.IOException;
import java.util.List;

public class LanguageSerializer extends JsonSerializer<List<Language>> {

    @Override
    public void serialize(List<Language> languages, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        for (Language language : languages) {
            jsonGenerator.writeFieldName(language.getLanguage());
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("QuestionText", language.getQuestionText());

            jsonGenerator.writeFieldName("Choices");
            jsonGenerator.writeStartObject();
            for( int i=0; i < language.getChoicesList().size();i++){
                Choice choice = language.getChoicesList().get(i);
                int index = i + 1;
                jsonGenerator.writeFieldId(index);
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("Display",choice.getDisplay());
                if(choice.getTextEntry() != null){
                    jsonGenerator.writeStringField("TextEntry", choice.getTextEntry());
                }
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndObject();

            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndObject();
    }
}
