package com.excilys.cdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping("/404")
    public String redirectTo404() {
        return "404";
    }

    @RequestMapping("/403")
    public String redirectTo403() {
        return "403";
    }

    @RequestMapping("/500")
    public String redirectTo500() {
        return "500";
    }

}
