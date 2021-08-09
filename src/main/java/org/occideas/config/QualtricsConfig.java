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

}
