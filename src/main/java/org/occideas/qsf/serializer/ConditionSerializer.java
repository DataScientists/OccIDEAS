package org.occideas.qsf.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.occideas.qsf.payload.Condition;
import org.occideas.qsf.payload.Logic;

import java.io.IOException;
import java.util.Map;

public class ConditionSerializer extends JsonSerializer<Condition> {

    @Override
    public void serialize(Condition condition, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        printLogic(jsonGenerator,condition.getLogic());
        jsonGenerator.writeStringField("Type", condition.getType());
        jsonGenerator.writeEndObject();
    }

    private void printLogic(JsonGenerator jsonGenerator, Map<Integer, Logic> logics) throws IOException {
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
                if(!StringUtils.isEmpty(logic.getConjunction())) {
                    jsonGenerator.writeStringField("Conjuction", logic.getConjunction());
                }
                jsonGenerator.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
