package org.occideas.qsf.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.occideas.qsf.payload.Choice;

import java.io.IOException;
import java.util.List;

public class ChoicesSerializer extends JsonSerializer<List<Choice>> {

    @Override
    public void serialize(List<Choice> choices, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        for(int i=0;i < choices.size();i++){
            Choice choice = choices.get(i);
            int index = i + 1;
            jsonGenerator.writeFieldId(index);
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("Display",choice.getDisplay());
            if(choice.getTextEntry() != null){
                jsonGenerator.writeStringField("TextEntry",choice.getTextEntry());
            }
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndObject();
    }
}
