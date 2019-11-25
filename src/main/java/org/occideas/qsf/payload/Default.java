package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;
import org.occideas.qsf.BlockElement;
import org.occideas.qsf.payload.Payload;

import java.util.List;

public class Default extends BaseQSF implements Payload {

    @JsonProperty(value = "Type")
    private String type = "Default";
    @JsonProperty(value = "Description")
    private String description;
    @JsonProperty(value = "ID")
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "BlockElements")
    private List<BlockElement> blockElements;

    public Default() {
    }

    public Default(String type, String description, String id, List<BlockElement> blockElements) {
        this.type = type;
        this.description = description;
        this.id = id;
        this.blockElements = blockElements;
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

    public List<BlockElement> getBlockElements() {
        return blockElements;
    }

    public void setBlockElements(List<BlockElement> blockElements) {
        this.blockElements = blockElements;
    }
}
