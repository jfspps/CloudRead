package com.example.cloudread.controller;

import com.example.JAXBmodel.FundamentalPieceDTOList;
import com.example.cloudread.config.WebClientConfig;
import com.example.cloudread.service.XMLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

    @GetMapping(value = "/buildFunFromXML")
    public String getBuildFun() {
        FundamentalPieceDTOList fundamentalPieceDTOList = xmlService.parseFundamentalXML(FUNDAMENTAL_XMLFILE);

        return "Number of pieces: " + fundamentalPieceDTOList.getFundamentalPieceDTOs().size();
    }
}
