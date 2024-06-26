package org.occideas.config;

import org.occideas.qsf.subscriber.topic.QualtricsTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("qualtrics")
public class QualtricsConfig {

    private String url;
    private String apiToken;
    private QualtricsTopic topic;
    private boolean isConfigured;
    private boolean hideNodeKeys;
    private boolean includeTranslations;
    private boolean expandModules;
    private String translationsPath;
    private boolean ipsosEnableLabel;
    private String redirectUrl;
    private NodeSurveyConfig node;
    private SurveyLinkConfig survey;
    private boolean enableListener;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public QualtricsTopic getTopic() {
        return topic;
    }

    public void setTopic(QualtricsTopic topic) {
        this.topic = topic;
    }

    public boolean isHideNodeKeys() {
        return hideNodeKeys;
    }

    public void setHideNodeKeys(boolean hideNodeKeys) {
        this.hideNodeKeys = hideNodeKeys;
    }

    public boolean isIncludeTranslations() {
        return includeTranslations;
    }

    public void setIncludeTranslations(boolean includeTranslations) {
        this.includeTranslations = includeTranslations;
    }

    public boolean isExpandModules() {
        return expandModules;
    }

    public void setExpandModules(boolean expandModules) {
        this.expandModules = expandModules;
    }

    public String getTranslationsPath() {
        return translationsPath;
    }

    public void setTranslationsPath(String translationsPath) {
        this.translationsPath = translationsPath;
    }

    public boolean isIpsosEnableLabel() {
        return ipsosEnableLabel;
    }

    public void setIpsosEnableLabel(boolean ipsosEnableLabel) {
        this.ipsosEnableLabel = ipsosEnableLabel;
    }

    public NodeSurveyConfig getNode() {
        return node;
    }

    public void setNode(NodeSurveyConfig node) {
        this.node = node;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public SurveyLinkConfig getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyLinkConfig survey) {
        this.survey = survey;
    }

    public boolean isEnableListener() {
        return enableListener;
    }

    public void setEnableListener(boolean enableListener) {
        this.enableListener = enableListener;
    }

	public boolean isConfigured() {
		return isConfigured;
	}

	public void setConfigured(boolean isConfigured) {
		this.isConfigured = isConfigured;
	}
}
