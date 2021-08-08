package org.occideas.config;

import org.occideas.qsf.topic.QualtricsTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("qualtrics")
public class QualtricsConfig {

    private String subscription;
    private String apiToken;
    private QualtricsTopic topic;

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
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

}
