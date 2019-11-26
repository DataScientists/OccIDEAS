package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

import java.util.List;

public class ScoringPayload extends BaseQSF implements Payload {

    @JsonProperty(value = "ScoringCategories")
    private List<ScoringCategories> scoringCategoriesList;
    @JsonProperty(value = "ScoringCategoryGroups")
    private List<ScoringCategoryGroups> scoringCategoryGroupsList;
    @JsonProperty(value = "ScoringSummaryCategory")
    private String scoringSummaryCategory;
    @JsonProperty(value = "ScoringSummaryAfterQuestions")
    private Integer scoringSummaryAfterQuestions = 0;
    @JsonProperty(value = "ScoringSummaryAfterSurvey")
    private Integer scoringSummaryAfterSurvey = 0;
    @JsonProperty(value = "DefaultScoringCategory")
    private String defaultScoringCategory;
    @JsonProperty(value = "AutoScoringCategory")
    private String autoScoringCategory;

    public ScoringPayload() {
    }

    public ScoringPayload(List<ScoringCategories> scoringCategoriesList, List<ScoringCategoryGroups> scoringCategoryGroupsList, String scoringSummaryCategory, Integer scoringSummaryAfterQuestions, Integer scoringSummaryAfterSurvey, String defaultScoringCategory, String autoScoringCategory) {
        this.scoringCategoriesList = scoringCategoriesList;
        this.scoringCategoryGroupsList = scoringCategoryGroupsList;
        this.scoringSummaryCategory = scoringSummaryCategory;
        this.scoringSummaryAfterQuestions = scoringSummaryAfterQuestions;
        this.scoringSummaryAfterSurvey = scoringSummaryAfterSurvey;
        this.defaultScoringCategory = defaultScoringCategory;
        this.autoScoringCategory = autoScoringCategory;
    }

    public List<ScoringCategories> getScoringCategoriesList() {
        return scoringCategoriesList;
    }

    public void setScoringCategoriesList(List<ScoringCategories> scoringCategoriesList) {
        this.scoringCategoriesList = scoringCategoriesList;
    }

    public List<ScoringCategoryGroups> getScoringCategoryGroupsList() {
        return scoringCategoryGroupsList;
    }

    public void setScoringCategoryGroupsList(List<ScoringCategoryGroups> scoringCategoryGroupsList) {
        this.scoringCategoryGroupsList = scoringCategoryGroupsList;
    }

    public String getScoringSummaryCategory() {
        return scoringSummaryCategory;
    }

    public void setScoringSummaryCategory(String scoringSummaryCategory) {
        this.scoringSummaryCategory = scoringSummaryCategory;
    }

    public Integer getScoringSummaryAfterQuestions() {
        return scoringSummaryAfterQuestions;
    }

    public void setScoringSummaryAfterQuestions(Integer scoringSummaryAfterQuestions) {
        this.scoringSummaryAfterQuestions = scoringSummaryAfterQuestions;
    }

    public Integer getScoringSummaryAfterSurvey() {
        return scoringSummaryAfterSurvey;
    }

    public void setScoringSummaryAfterSurvey(Integer scoringSummaryAfterSurvey) {
        this.scoringSummaryAfterSurvey = scoringSummaryAfterSurvey;
    }

    public String getDefaultScoringCategory() {
        return defaultScoringCategory;
    }

    public void setDefaultScoringCategory(String defaultScoringCategory) {
        this.defaultScoringCategory = defaultScoringCategory;
    }

    public String getAutoScoringCategory() {
        return autoScoringCategory;
    }

    public void setAutoScoringCategory(String autoScoringCategory) {
        this.autoScoringCategory = autoScoringCategory;
    }
}
