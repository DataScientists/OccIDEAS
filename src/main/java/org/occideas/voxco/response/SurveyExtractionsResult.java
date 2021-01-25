package org.occideas.voxco.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyExtractionsResult {

    @JsonProperty(value = "Extractions")
    private List<ExtractionResult> extractions;

    public List<ExtractionResult> getExtractions() {
        return extractions;
    }

    public void setExtractions(List<ExtractionResult> extractions) {
        this.extractions = extractions;
    }
}
