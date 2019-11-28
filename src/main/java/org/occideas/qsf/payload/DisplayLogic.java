package org.occideas.qsf.payload;

import org.occideas.qsf.BaseQSF;

public class DisplayLogic extends BaseQSF implements Payload{

    private String type;
    private boolean inPage;
    private Condition condition;

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
