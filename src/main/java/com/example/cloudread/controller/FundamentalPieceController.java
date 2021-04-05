package com.example.cloudread.controller;

import com.example.JAXBmodel.FundamentalPieceList;
import com.example.cloudread.service.FundamentalPieceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FundamentalPieceController {

    private final FundamentalPieceService fundamentalPieceService;

    public FundamentalPieceController(FundamentalPieceService fundamentalPieceService) {
        this.fundamentalPieceService = fundamentalPieceService;
    }

    @GetMapping(value = "/")
    public String getIndex() {
        log.info("Logged in");

        FundamentalPieceList fundamentalPieceList = fundamentalPieceService.getAllPieces_Adv();

        log.info("Number of pieces: " + fundamentalPieceList.getFundamentalPieces().size());

        return null;
    }
}
