package org.occideas.vo;

import java.util.List;

public class AnzscoSuggestionVO {

    private String code;
    private String label;
    private double confidence;
    private String moduleCode;
    private String moduleName;
    private Long moduleId;
    // Set instead of moduleCode/moduleId/moduleName when the ANZSCO code was too short/coarse
    // to resolve to a single module - the participant picks one of these to resolve it.
    private String disambiguationQuestion;
    private List<AnzscoModuleOptionVO> disambiguationOptions;

    public AnzscoSuggestionVO() {
    }

    public AnzscoSuggestionVO(String code, String label, double confidence) {
        this.code = code;
        this.label = label;
        this.confidence = confidence;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getDisambiguationQuestion() {
        return disambiguationQuestion;
    }

    public void setDisambiguationQuestion(String disambiguationQuestion) {
        this.disambiguationQuestion = disambiguationQuestion;
    }

    public List<AnzscoModuleOptionVO> getDisambiguationOptions() {
        return disambiguationOptions;
    }

    public void setDisambiguationOptions(List<AnzscoModuleOptionVO> disambiguationOptions) {
        this.disambiguationOptions = disambiguationOptions;
    }
}
