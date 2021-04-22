package com.example.cloudread.controller;

import com.example.cloudread.restapi.RESTAPIConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class UserController {

    private final RESTAPIConfig restapiConfig;

    public UserController(RESTAPIConfig restapiConfig) {
        this.restapiConfig = restapiConfig;
    }

    @GetMapping("/")
    public String getWelcomePage(){
        log.info("Current REST API url: " + restapiConfig.getUrl());
        return "index";
    }
}
