package org.occideas.qsf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Payload {

    @JsonProperty(value = "Type")
    private String type;
    @JsonProperty(value = "Description")
    private String description;
    @JsonProperty(value = "ID")
    private String id;
    @JsonProperty(value = "BlockElements")
    private List<BlockElement> blockElements;

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
