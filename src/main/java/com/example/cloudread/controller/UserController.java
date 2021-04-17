package com.example.cloudread.controller;

import com.example.cloudread.JAXBmodel.FundamentalPieceDTOList;
import com.example.cloudread.JAXBmodel.ResearchPieceDTOList;
import com.example.cloudread.controller.api.FundamentalPieceController;
import com.example.cloudread.controller.api.ResearchPieceController;
import com.example.cloudread.service.api.FundamentalService;
import com.example.cloudread.service.api.ResearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final FundamentalService fundamentalService;
    private final ResearchService researchService;

    public UserController(FundamentalService fundamentalService, ResearchService researchService) {
        this.fundamentalService = fundamentalService;
        this.researchService = researchService;
    }

    @GetMapping("/")
    public String getWelcomePage(){
        return "index";
    }

    @GetMapping("/fundamentals/file")
    public String getFundamentalsFromFile(Model model){
        FundamentalPieceDTOList onFile = fundamentalService.parseFundamentalXMLFile(FundamentalPieceController.FUNDAMENTAL_XMLFILE);
        model.addAttribute("fundamentalPieces", onFile);
        return "fundamental/fundamentalList";
    }

    @GetMapping("/fundamentals/DB")
    public String getFundamentalsFromDB(Model model){
        FundamentalPieceDTOList onDB = fundamentalService.parseFundamentalURL(
                FundamentalPieceController.CloudWriteFundamentalsPath, FundamentalPieceController.FUNDAMENTAL_XMLFILE);

        if (onDB == null){
            model.addAttribute("message", "Database offline");
            return "index";
        }

        model.addAttribute("fundamentalPieces", onDB);
        return "fundamental/fundamentalList";
    }

    @GetMapping("/research/file")
    public String getResearchFromFile(Model model){
        ResearchPieceDTOList onFile = researchService.parseResearchXMLFile(ResearchPieceController.RESEARCH_XMLFILE);
        model.addAttribute("researchPieces", onFile);
        return "research/researchList";
    }

    @GetMapping("/research/DB")
    public String getResearchFromDB(Model model){
        ResearchPieceDTOList onDB = researchService.parseResearchURL(
                ResearchPieceController.CloudWriteResearchPath,  ResearchPieceController.RESEARCH_XMLFILE);

        if (onDB == null){
            model.addAttribute("message", "Database offline");
            return "index";
        }

        model.addAttribute("researchPieces", onDB);
        return "research/researchList";
    }
}
