package com.example.cloudread.controller;

import com.example.cloudread.config.WebClientConfig;
import com.example.cloudread.service.XMLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@Slf4j
public class FundamentalPieceController {

    private final XMLService xmlService;

    public static final String fundamentalServiceURLSuffix = "/fundamentals/";
    public static final String FUNDAMENTAL_XMLFILE = "./src/main/resources/xmlFeeds/fundamental.xml";
    public static final String expositionServiceURLSuffix = "/expositions/";
    public static final String EXPOSITION_XMLFILE = "exposition.xml";

    public FundamentalPieceController(XMLService xmlService) {
        this.xmlService = xmlService;
    }

    @GetMapping(value = "/listFundamentals")
    public String getIndex() {
        return "listFundamentals " +  xmlService.downloadXML(WebClientConfig.BASE_URL + fundamentalServiceURLSuffix, FUNDAMENTAL_XMLFILE);
    }
}
