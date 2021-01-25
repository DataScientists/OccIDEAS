package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Extraction {

    @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "SurveyId")
    private Long surveyId;

    @JsonProperty(value = "Language")
    private String language = "en";

    @JsonProperty(value = "Description")
    private String description;

    @JsonProperty(value = "DestinationFileName")
    private String destinationFileName;

    @JsonProperty(value = "ExtractFormat")
    private String extractFormat = "CSV";

    @JsonProperty(value = "Filter")
    private Filter filter;

    @JsonProperty(value = "IncludeOpenEnds")
    private boolean includeOpenEnds = true;

    @JsonProperty(value = "IncludeConnectionHistory")
    private Boolean includeConnectionHistory;

    @JsonProperty(value = "IncludeLabels")
    private boolean includeLabels = true;

    @JsonProperty(value = "StripHtmlFromLabels")
    private boolean stripHtmlFromLabels = true;

    @JsonProperty(value = "FieldDelimiter")
    private String fieldDelimiter = "Comma";

    @JsonProperty(value = "Encoding")
    private String encoding = "UTF8";

    @JsonProperty(value = "EncloseValuesInDoubleQuotes")
    private Boolean encloseValuesInDoubleQuotes;

    @JsonProperty(value = "IncludeHeader")
    private boolean includeHeader = true;

    @JsonProperty(value = "UseChoiceLabels")
    private boolean useChoiceLabels;

    @JsonProperty(value = "MergeOpenEnds")
    private Boolean mergeOpenEnds;

    @JsonProperty(value = "DichotomizedMultiple")
    private Boolean dichotomizedMultiple;

    @JsonProperty(value = "DichotomizedEmptyWhenNoAnswer")
    private Boolean dichotomizedEmptyWhenNoAnswer;

    @JsonProperty(value = "UseNegativeIntegersForEmptyAnswers")
    private Boolean useNegativeIntegersForEmptyAnswers;

    @JsonProperty(value = "DapresyDataFormat")
    private Boolean dapresyDataFormat;

    @JsonProperty(value = "LoopsInQuestionnaireOrder")
    private Boolean loopsInQuestionnaireOrder;

    @JsonProperty(value = "Variables")
    private List<String> variables;

    public Extraction() {
    }

    public Extraction(String name, Long surveyId) {
        this.name = name;
        this.surveyId = surveyId;
        this.destinationFileName = name.replaceAll(" ", "_").toLowerCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestinationFileName() {
        return destinationFileName;
    }

    public void setDestinationFileName(String destinationFileName) {
        this.destinationFileName = destinationFileName;
    }

    public String getExtractFormat() {
        return extractFormat;
    }

    public void setExtractFormat(String extractFormat) {
        this.extractFormat = extractFormat;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public boolean isIncludeOpenEnds() {
        return includeOpenEnds;
    }

    public void setIncludeOpenEnds(boolean includeOpenEnds) {
        this.includeOpenEnds = includeOpenEnds;
    }

    public Boolean getIncludeConnectionHistory() {
        return includeConnectionHistory;
    }

    public void setIncludeConnectionHistory(Boolean includeConnectionHistory) {
        this.includeConnectionHistory = includeConnectionHistory;
    }

    public boolean isIncludeLabels() {
        return includeLabels;
    }

    public void setIncludeLabels(boolean includeLabels) {
        this.includeLabels = includeLabels;
    }

    public boolean isStripHtmlFromLabels() {
        return stripHtmlFromLabels;
    }

    public void setStripHtmlFromLabels(boolean stripHtmlFromLabels) {
        this.stripHtmlFromLabels = stripHtmlFromLabels;
    }

    public String getFieldDelimiter() {
        return fieldDelimiter;
    }

    public void setFieldDelimiter(String fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Boolean getEncloseValuesInDoubleQuotes() {
        return encloseValuesInDoubleQuotes;
    }

    public void setEncloseValuesInDoubleQuotes(Boolean encloseValuesInDoubleQuotes) {
        this.encloseValuesInDoubleQuotes = encloseValuesInDoubleQuotes;
    }

    public boolean isIncludeHeader() {
        return includeHeader;
    }

    public void setIncludeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
    }

    public boolean isUseChoiceLabels() {
        return useChoiceLabels;
    }

    public void setUseChoiceLabels(boolean useChoiceLabels) {
        this.useChoiceLabels = useChoiceLabels;
    }

    public Boolean getMergeOpenEnds() {
        return mergeOpenEnds;
    }

    public void setMergeOpenEnds(Boolean mergeOpenEnds) {
        this.mergeOpenEnds = mergeOpenEnds;
    }

    public Boolean getDichotomizedMultiple() {
        return dichotomizedMultiple;
    }

    public void setDichotomizedMultiple(Boolean dichotomizedMultiple) {
        this.dichotomizedMultiple = dichotomizedMultiple;
    }

    public Boolean getDichotomizedEmptyWhenNoAnswer() {
        return dichotomizedEmptyWhenNoAnswer;
    }

    public void setDichotomizedEmptyWhenNoAnswer(Boolean dichotomizedEmptyWhenNoAnswer) {
        this.dichotomizedEmptyWhenNoAnswer = dichotomizedEmptyWhenNoAnswer;
    }

    public Boolean getUseNegativeIntegersForEmptyAnswers() {
        return useNegativeIntegersForEmptyAnswers;
    }

    public void setUseNegativeIntegersForEmptyAnswers(Boolean useNegativeIntegersForEmptyAnswers) {
        this.useNegativeIntegersForEmptyAnswers = useNegativeIntegersForEmptyAnswers;
    }

    public Boolean getDapresyDataFormat() {
        return dapresyDataFormat;
    }

    public void setDapresyDataFormat(Boolean dapresyDataFormat) {
        this.dapresyDataFormat = dapresyDataFormat;
    }

    public Boolean getLoopsInQuestionnaireOrder() {
        return loopsInQuestionnaireOrder;
    }

    public void setLoopsInQuestionnaireOrder(Boolean loopsInQuestionnaireOrder) {
        this.loopsInQuestionnaireOrder = loopsInQuestionnaireOrder;
    }

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }
}

