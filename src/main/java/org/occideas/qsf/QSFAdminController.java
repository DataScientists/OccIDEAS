package org.occideas.qsf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.qsf.subscriber.service.QualtricsSurveySubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${public.api.base-path}/qualtrics/admin")
public class QSFAdminController {

    private Logger log = LogManager.getLogger(this);

    @Autowired
    private QualtricsSurveySubscriberService qualtricsSurveySubscriberService;

    @PostMapping("/subscribe/refresh")
    public ResponseEntity refresh() {
        try {
            qualtricsSurveySubscriberService.subscribeActiveDistributions();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
