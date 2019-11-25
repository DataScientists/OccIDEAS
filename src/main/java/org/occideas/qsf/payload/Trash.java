package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;
import org.occideas.qsf.payload.Payload;

public class Trash extends BaseQSF implements Payload {

    @JsonProperty(value = "Type")
    private String type = "Trash";
    @JsonProperty(value = "Description")
    private String description;
    @JsonProperty(value = "ID")
    private String id;

    public Trash() {
    }
    
    public Trash(String type, String description, String id) {
        this.type = type;
        this.description = description;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
