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

    // see CloudWrite for routing
    public static final String fundamentalServiceURLSuffix = "/fundamentals/";

    public static final String FUNDAMENTAL_XMLFILE = "./src/main/resources/xmlFeeds/fundamental.xml";
    public static final String FUNDAMENTAL_JSONFILE = "./src/main/resources/xmlFeeds/fundamental.json";

    public FundamentalPieceController(XMLService xmlService) {
        this.xmlService = xmlService;
    }

    @GetMapping(value = "/listFundamentals/xml")
    public String getIndexXML() {
        return "List of fundamental articles XML refreshed: " + xmlService.downloadXML(WebClientConfig.BASE_URL + fundamentalServiceURLSuffix, FUNDAMENTAL_XMLFILE);
    }

    @GetMapping(value = "/listFundamentals/json")
    public String getIndexJSON() {
        return "List of fundamental articles JSON refreshed: " + xmlService.downloadJSON(WebClientConfig.BASE_URL + fundamentalServiceURLSuffix, FUNDAMENTAL_JSONFILE);
    }

    @GetMapping(value = "/listFundamentals/buildPieceList")
    public String getbuildFundamentalList() {
        return "Building fundamental pieces list: " + xmlService.parseFundamentalURL(WebClientConfig.BASE_URL + fundamentalServiceURLSuffix, FUNDAMENTAL_XMLFILE).getFundamentalPiece().size();
    }
}
