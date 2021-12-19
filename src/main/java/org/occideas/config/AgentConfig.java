package org.occideas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;


@Configuration
@ConfigurationProperties("agents")
public class AgentConfig {

    private Map<String, List<String>> ids;

    public Map<String, List<String>> getIds() {
        return ids;
    }

    public void setIds(Map<String, List<String>> ids) {
        this.ids = ids;
    }
}
