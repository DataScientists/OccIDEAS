package org.occideas.qsf.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.occideas.qsf.payload.Logic;

import java.io.IOException;
import java.util.Map;

public class LogicSerializer extends JsonSerializer<Map<Integer, Logic>> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(Map<Integer, Logic> logics, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        logics.forEach((key,logic) ->{
            try {
                jsonGenerator.writeFieldId(key);
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("LogicType", logic.getLogicType());
                jsonGenerator.writeStringField("QuestionID", logic.getQuestionId());
                jsonGenerator.writeStringField("QuestionIsInLoop", logic.getQuestionIsInLoop());
                jsonGenerator.writeStringField("ChoiceLocator", logic.getChoiceLocator());
                jsonGenerator.writeStringField("Operator", logic.getOperator());
                jsonGenerator.writeStringField("QuestionIDFromLocator", logic.getQuestionIdFromLocator());
                jsonGenerator.writeStringField("LeftOperand", logic.getLeftOperand());
                jsonGenerator.writeStringField("Type", logic.getType());
                jsonGenerator.writeStringField("Description", logic.getDescription());
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndObject();
    }


}
