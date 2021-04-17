package com.example.cloudread.controller.api;


import com.example.cloudread.config.WebClientConfig;
import com.example.cloudread.service.api.ResearchService;
import com.example.cloudread.service.api.XML_JSONService;
import lombok.extern.slf4j.Slf4j;
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
    public static final String CloudWriteResearchPath = WebClientConfig.BASE_URL + "/research/";

    public static final String RESEARCH_XMLFILE = "./src/main/resources/xmlFeeds/research.xml";
    public static final String RESEARCH_JSONFILE = "./src/main/resources/xmlFeeds/research.json";

    public ResearchPieceController(XML_JSONService xmlJSONService, ResearchService researchService) {
        this.xmlJSONService = xmlJSONService;
        this.researchService = researchService;
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
