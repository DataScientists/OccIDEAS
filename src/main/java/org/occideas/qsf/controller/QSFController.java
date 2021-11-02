package org.occideas.qsf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QSFController {

    @RequestMapping(value = "survey-intro")
    public String results() {
        return "forward:/survey-intro.html";
    }

}
