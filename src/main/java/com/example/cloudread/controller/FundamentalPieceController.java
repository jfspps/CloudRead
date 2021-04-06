package com.example.cloudread.controller;

import com.example.cloudread.config.WebClientConfig;
import com.example.cloudread.service.XMLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FundamentalPieceController {

    private final XMLService xmlService;
    public static final String fundamentalServiceURLSuffix = "/fundamentals/";

    public FundamentalPieceController(XMLService xmlService) {
        this.xmlService = xmlService;
    }

    @GetMapping(value = "/listFundamentals")
    public String getIndex() {
        String xmlFeed = xmlService.downloadXML(WebClientConfig.BASE_URL + fundamentalServiceURLSuffix);

        log.info("Test URL: " + xmlFeed);

        return null;
    }
}
