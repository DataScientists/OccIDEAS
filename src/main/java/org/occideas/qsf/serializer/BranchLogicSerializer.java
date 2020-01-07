package org.occideas.qsf.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.occideas.qsf.payload.BranchLogic;
import org.occideas.qsf.payload.Condition;
import org.occideas.qsf.payload.Logic;

import java.io.IOException;

public class BranchLogicSerializer extends JsonSerializer<BranchLogic> {


    @Override
    public void serialize(BranchLogic branchLogic, JsonGenerator jsonGenerator,
                          SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        for (int x = 0; x < branchLogic.getLogic().size(); x++) {
            Condition condition = branchLogic.getLogic().get(x);
            jsonGenerator.writeFieldId(x);
            jsonGenerator.writeStartObject();
            for(int y=0;y < condition.getLogics().size(); y++) {
                Logic logic = condition.getLogics().get(y);
                jsonGenerator.writeFieldId(y);
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

            }
            jsonGenerator.writeStringField("Type",condition.getType());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeStringField("Type", branchLogic.getType());
        jsonGenerator.writeEndObject();
}
}
