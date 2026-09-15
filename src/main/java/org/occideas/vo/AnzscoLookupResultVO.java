package org.occideas.vo;

import java.util.List;

public class AnzscoLookupResultVO {

    private String jobTitle;
    private String jobDescription;
    private List<AnzscoSuggestionVO> suggestions;

    public AnzscoLookupResultVO() {
    }

    public AnzscoLookupResultVO(String jobTitle, String jobDescription, List<AnzscoSuggestionVO> suggestions) {
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.suggestions = suggestions;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public List<AnzscoSuggestionVO> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<AnzscoSuggestionVO> suggestions) {
        this.suggestions = suggestions;
    }
}
