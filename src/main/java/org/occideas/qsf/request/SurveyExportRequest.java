package org.occideas.qsf.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.occideas.qsf.BaseQSF;

public class SurveyExportRequest extends BaseQSF {

    private String format;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String startDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String endDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer limit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean useLabels;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer seenUnansweredRecode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer multiselectSeenUnansweredRecode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean includeDisplayOrder;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String timeZone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String newlineReplacement;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] questionIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] embeddedDataIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] surveyMetadataIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean compress;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean exportResponsesInProgress;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean breakoutSets;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String filterId;

    public SurveyExportRequest() {
    }


    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getUseLabels() {
        return useLabels;
    }

    public void setUseLabels(Boolean useLabels) {
        this.useLabels = useLabels;
    }

    public Integer getSeenUnansweredRecode() {
        return seenUnansweredRecode;
    }

    public void setSeenUnansweredRecode(Integer seenUnansweredRecode) {
        this.seenUnansweredRecode = seenUnansweredRecode;
    }

    public Integer getMultiselectSeenUnansweredRecode() {
        return multiselectSeenUnansweredRecode;
    }

    public void setMultiselectSeenUnansweredRecode(Integer multiselectSeenUnansweredRecode) {
        this.multiselectSeenUnansweredRecode = multiselectSeenUnansweredRecode;
    }

    public Boolean getIncludeDisplayOrder() {
        return includeDisplayOrder;
    }

    public void setIncludeDisplayOrder(Boolean includeDisplayOrder) {
        this.includeDisplayOrder = includeDisplayOrder;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getNewlineReplacement() {
        return newlineReplacement;
    }

    public void setNewlineReplacement(String newlineReplacement) {
        this.newlineReplacement = newlineReplacement;
    }

    public String[] getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(String[] questionIds) {
        this.questionIds = questionIds;
    }

    public String[] getEmbeddedDataIds() {
        return embeddedDataIds;
    }

    public void setEmbeddedDataIds(String[] embeddedDataIds) {
        this.embeddedDataIds = embeddedDataIds;
    }

    public String[] getSurveyMetadataIds() {
        return surveyMetadataIds;
    }

    public void setSurveyMetadataIds(String[] surveyMetadataIds) {
        this.surveyMetadataIds = surveyMetadataIds;
    }

    public Boolean getCompress() {
        return compress;
    }

    public void setCompress(Boolean compress) {
        this.compress = compress;
    }

    public Boolean getExportResponsesInProgress() {
        return exportResponsesInProgress;
    }

    public void setExportResponsesInProgress(Boolean exportResponsesInProgress) {
        this.exportResponsesInProgress = exportResponsesInProgress;
    }

    public Boolean getBreakoutSets() {
        return breakoutSets;
    }

    public void setBreakoutSets(Boolean breakoutSets) {
        this.breakoutSets = breakoutSets;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }
}
