package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BlockElement;
import org.occideas.qsf.payload.Options;

import java.util.List;

public class GetBlockElementResult extends Result{

    @JsonProperty(value = "Type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;
    @JsonProperty(value = "Description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonProperty(value = "ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonProperty(value = "BlockElements")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BlockElement> blockElement;
    @JsonProperty(value = "Options")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Options options;

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

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BlockElement> getBlockElement() {
        return blockElement;
    }

    public void setBlockElement(List<BlockElement> blockElement) {
        this.blockElement = blockElement;
    }
}
