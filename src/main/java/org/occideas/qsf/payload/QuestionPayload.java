package org.occideas.qsf.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.occideas.qsf.BaseQSF;

import java.util.List;

public class QuestionPayload extends BaseQSF implements Payload {

    @JsonProperty(value = "QuestionText")
    private String questionText;
    @JsonProperty(value = "DataExportTag")
    private String dataExportTag;
    @JsonProperty(value = "QuestionType")
    private String questionType;
    @JsonProperty(value = "Selector")
    private String selector;
    @JsonProperty(value = "SubSelector")
    private String subSelector;
    @JsonProperty(value = "Configuration")
    private Configuration configuration;
    @JsonProperty(value = "QuestionDescription")
    private String questionDescription;
    @JsonProperty(value = "Choices")
    private List<Choice> choicesList;
    @JsonProperty(value = "ChoiceOrder")
    private String[] choiceOrderList;
    @JsonProperty(value = "Validation")
    private Validation validation;
    @JsonProperty(value = "Language")
    private List<Language> languageList;
    @JsonProperty(value = "NextChoiceId")
    private int nextChoiceId;
    @JsonProperty(value = "NextAnswerId")
    private int nextAnswerId;
    @JsonProperty(value = "QuestionID")
    private String questionId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "DisplayLogic")
    private DisplayLogic logic;

    public QuestionPayload() {
    }

    public QuestionPayload(String questionText, String dataExportTag, String questionType, String selector, String subSelector, Configuration configuration, String questionDescription, List<Choice> choicesList, String[] choiceOrderList, Validation validation, List<Language> languageList, int nextChoiceId, int nextAnswerId, String questionId,DisplayLogic logic) {
        this.questionText = questionText;
        this.dataExportTag = dataExportTag;
        this.questionType = questionType;
        this.selector = selector;
        this.subSelector = subSelector;
        this.configuration = configuration;
        this.questionDescription = questionDescription;
        this.choicesList = choicesList;
        this.choiceOrderList = choiceOrderList;
        this.validation = validation;
        this.languageList = languageList;
        this.nextChoiceId = nextChoiceId;
        this.nextAnswerId = nextAnswerId;
        this.questionId = questionId;
        this.logic = logic;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getDataExportTag() {
        return dataExportTag;
    }

    public void setDataExportTag(String dataExportTag) {
        this.dataExportTag = dataExportTag;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getSubSelector() {
        return subSelector;
    }

    public void setSubSelector(String subSelector) {
        this.subSelector = subSelector;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public List<Choice> getChoicesList() {
        return choicesList;
    }

    public void setChoicesList(List<Choice> choicesList) {
        this.choicesList = choicesList;
    }

    public String[] getChoiceOrderList() {
        return choiceOrderList;
    }

    public void setChoiceOrderList(String[] choiceOrderList) {
        this.choiceOrderList = choiceOrderList;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public List<Language> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<Language> languageList) {
        this.languageList = languageList;
    }

    public int getNextChoiceId() {
        return nextChoiceId;
    }

    public void setNextChoiceId(int nextChoiceId) {
        this.nextChoiceId = nextChoiceId;
    }

    public int getNextAnswerId() {
        return nextAnswerId;
    }

    public void setNextAnswerId(int nextAnswerId) {
        this.nextAnswerId = nextAnswerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

	public DisplayLogic getLogic() {
		return logic;
	}

	public void setLogic(DisplayLogic logic) {
		this.logic = logic;
	}
    
}
