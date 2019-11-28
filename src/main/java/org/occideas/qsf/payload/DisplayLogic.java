package org.occideas.qsf.payload;

import org.occideas.qsf.BaseQSF;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DisplayLogic extends BaseQSF implements Payload{
	
	@JsonProperty(value = "0")
	private Condition condition;
	@JsonProperty(value = "Type")
    private String type;
	@JsonProperty(value = "inPage")
    private boolean inPage;

    public DisplayLogic() {
    }

    public DisplayLogic(String type, boolean inPage, Condition condition) {
        this.type = type;
        this.inPage = inPage;
        this.condition = condition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isInPage() {
        return inPage;
    }

    public void setInPage(boolean inPage) {
        this.inPage = inPage;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
