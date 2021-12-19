package org.occideas.style.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.exceptions.GenericException;
import org.occideas.style.service.StyleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/style")
public class StyleRestController {

    private final Logger log = LogManager.getLogger(this.getClass());

    private final StyleService styleService;

    public StyleRestController(StyleService styleService) {
        this.styleService = styleService;
    }

    @GetMapping(value = "/list")
    public Map<String, String> getStyles() {
        Map<String, String> styles;
        try {
            styles = styleService.getStyles();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new GenericException(e.getMessage());
        }
        return styles;
    }

}
