package com.example.cloudread.controller.api;

import com.example.cloudread.config.WebClientConfig;
import com.example.cloudread.restapi.RESTAPIConfig;
import com.example.cloudread.service.api.FundamentalService;
import com.example.cloudread.service.api.XML_JSONService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class FundamentalPieceController {

    private final XML_JSONService xmlJSONService;
    private final FundamentalService fundamentalService;

    // see CloudWrite for routing
    private final String CloudWriteFundamentalsPath;

    public static final String FUNDAMENTAL_XMLFILE = "./xmlFeeds/fundamental.xml";
    public static final String FUNDAMENTAL_JSONFILE = "./xmlFeeds/fundamental.json";

    public FundamentalPieceController(XML_JSONService xmlJSONService, FundamentalService fundamentalService, RESTAPIConfig restapiConfig) {
        this.xmlJSONService = xmlJSONService;
        this.fundamentalService = fundamentalService;
        CloudWriteFundamentalsPath = restapiConfig.getUrl() + restapiConfig.getFundamentals_path();
    }

    @GetMapping(value = "listFundamentals/xml")
    public String getIndexXML() {
        return "List of fundamental articles XML refreshed: " +
                xmlJSONService.downloadXML(CloudWriteFundamentalsPath, FUNDAMENTAL_XMLFILE);
    }

    @GetMapping(value = "listFundamentals/json")
    public String getIndexJSON() {
        return "List of fundamental articles JSON refreshed: " +
                xmlJSONService.downloadJSON(CloudWriteFundamentalsPath, FUNDAMENTAL_JSONFILE);
    }

    @GetMapping(value = "listFundamentals/buildPieceList")
    public String getbuildFundamentalList() {
        return "Building fundamental pieces list: " +
                fundamentalService.parseFundamentalXMLFile(FUNDAMENTAL_XMLFILE).getFundamentalPiece().size();
    }
}
