package com.example.cloudread.controller.api;


import com.example.cloudread.config.WebClientConfig;
import com.example.cloudread.service.api.ResearchService;
import com.example.cloudread.service.api.XMLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ResearchPieceController {

    private final XMLService xmlService;
    private final ResearchService researchService;

    // see CloudWrite for routing
    public static final String CloudWriteResearchSuffix = "/research/";

    public static final String RESEARCH_XMLFILE = "./src/main/resources/xmlFeeds/research.xml";
    public static final String RESEARCH_JSONFILE = "./src/main/resources/xmlFeeds/research.json";

    public ResearchPieceController(XMLService xmlService, ResearchService researchService) {
        this.xmlService = xmlService;
        this.researchService = researchService;
    }

    @GetMapping(value = "/listResearch/xml")
    public String getIndexXML() {
        return "List of research articles XML refreshed: " +
                xmlService.downloadXML(WebClientConfig.BASE_URL + CloudWriteResearchSuffix, RESEARCH_XMLFILE);
    }

    @GetMapping(value = "/listResearch/json")
    public String getIndexJSON() {
        return "List of research articles JSON refreshed: " +
                xmlService.downloadJSON(WebClientConfig.BASE_URL + CloudWriteResearchSuffix, RESEARCH_JSONFILE);
    }

    @GetMapping(value = "/listResearch/buildPieceList")
    public String getbuildFundamentalList() {
        return "Building research pieces list: " +
                researchService.parseResearchURL(WebClientConfig.BASE_URL + CloudWriteResearchSuffix, RESEARCH_XMLFILE).getResearchPiece().size();
    }
}
