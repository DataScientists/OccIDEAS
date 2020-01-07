package org.occideas.qsf.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.occideas.qsf.payload.Condition;
import org.occideas.qsf.payload.Logic;

import java.io.IOException;
import java.util.List;

public class ConditionLogicSerializer extends JsonSerializer<List<Condition>> {

    @Override
    public void serialize(List<Condition> conditions, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();

        for(int i=0;i < conditions.size();i++){
            Condition condition = conditions.get(i);
            int index = i;
            jsonGenerator.writeFieldId(index);
            jsonGenerator.writeStartObject();
            for(int x=0;x < condition.getLogics().size();x++){
                jsonGenerator.writeFieldId(x);
                jsonGenerator.writeStartObject();
                Logic logic = condition.getLogics().get(x);
                jsonGenerator.writeStringField("ChoiceLocator",logic.getChoiceLocator());
                jsonGenerator.writeStringField("Description",logic.getDescription());
                jsonGenerator.writeStringField("LeftOperand",logic.getLeftOperand());
                jsonGenerator.writeStringField("LogicType",logic.getLogicType());
                jsonGenerator.writeStringField("Operator",logic.getOperator());
                jsonGenerator.writeStringField("QuestionID",logic.getQuestionId());
                jsonGenerator.writeStringField("QuestionIDFromLocator",logic.getQuestionIdFromLocator());
                jsonGenerator.writeStringField("QuestionIsInLoop",logic.getQuestionIsInLoop());
                jsonGenerator.writeStringField("Type",logic.getType());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeStringField("Type",condition.getType());
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndObject();
    }
}
