
package org.occideas.qsf.response;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "responses"
})
public class SurveyResponses {

    @JsonProperty("responses")
    private List<Response> responses = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("responses")
    public List<Response> getResponses() {
        return responses;
    }

    @JsonProperty("responses")
    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "SurveyResponses{" +
                "responses=" + responses +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
