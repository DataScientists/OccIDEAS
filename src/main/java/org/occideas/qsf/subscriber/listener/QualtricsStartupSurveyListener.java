package org.occideas.qsf.subscriber.listener;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.config.QualtricsConfig;
import org.occideas.qsf.subscriber.service.QualtricsSurveySubscriberService;
import org.occideas.qsf.subscriber.topic.QualtricsTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class QualtricsStartupSurveyListener implements ApplicationListener<ContextRefreshedEvent> {

    private Logger log = LogManager.getLogger(this);

    @Autowired
    private QualtricsConfig qualtricsConfig;
    @Autowired
    private QualtricsSurveySubscriberService qualtricsSurveySubscriberService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        QualtricsTopic topic = qualtricsConfig.getTopic();
        if (Objects.isNull(topic)) {
            log.warn("topic config is not setup in application.properties");
            return;
        }
        if (StringUtils.isEmpty(topic.getSurvey())) {
            log.warn("survey id is not setup in application.properties, nothing to listen to.");
            return;
        }
        qualtricsSurveySubscriberService.listenToSurvey(topic.getSurvey());
    }
}
