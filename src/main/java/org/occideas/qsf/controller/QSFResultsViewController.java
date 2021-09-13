package org.occideas.qsf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QSFResultsViewController {

    @RequestMapping(value = "results")
    public String results() {
        return "forward:/view-result.html";
    }

}
