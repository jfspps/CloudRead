package com.example.cloudread.controller.api;


import com.example.cloudread.restapi.RESTAPIConfig;
import com.example.cloudread.service.api.ResearchService;
import com.example.cloudread.service.api.XML_JSONService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class ResearchPieceController {

    private final XML_JSONService xmlJSONService;
    private final ResearchService researchService;

    // see CloudWrite for routing
    private final String CloudWriteResearchPath;

    public static final String RESEARCH_XMLFILE = "./xmlFeeds/research.xml";
    public static final String RESEARCH_JSONFILE = "./xmlFeeds/research.json";

    public ResearchPieceController(XML_JSONService xmlJSONService, ResearchService researchService, RESTAPIConfig restapiConfig) {
        this.xmlJSONService = xmlJSONService;
        this.researchService = researchService;
        CloudWriteResearchPath = restapiConfig.getUrl() + restapiConfig.getResearch_path();
    }

    @GetMapping(value = "/listResearch/xml")
    public String getIndexXML() {
        return "List of research articles XML refreshed: " +
                xmlJSONService.downloadXML(CloudWriteResearchPath, RESEARCH_XMLFILE);
    }

    @GetMapping(value = "/listResearch/json")
    public String getIndexJSON() {
        return "List of research articles JSON refreshed: " +
                xmlJSONService.downloadJSON(CloudWriteResearchPath, RESEARCH_JSONFILE);
    }

    @GetMapping(value = "/listResearch/buildPieceList")
    public String getbuildFundamentalList() {
        return "Building research pieces list: " +
                researchService.parseResearchXMLFile(RESEARCH_XMLFILE).getResearchPiece().size();
    }
}
