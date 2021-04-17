package com.example.cloudread.controller;

import com.example.cloudread.JAXBmodel.FundamentalPieceDTOList;
import com.example.cloudread.JAXBmodel.ResearchPieceDTOList;
import com.example.cloudread.controller.api.FundamentalPieceController;
import com.example.cloudread.controller.api.ResearchPieceController;
import com.example.cloudread.service.api.ResearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/research")
public class ResearchDTOController {

    private final ResearchService researchService;

    public ResearchDTOController(ResearchService researchService) {
        this.researchService = researchService;
    }

    @GetMapping("/file")
    public String getResearchFromFile(Model model){
        ResearchPieceDTOList onFile = researchService.parseResearchXMLFile(ResearchPieceController.RESEARCH_XMLFILE);
        model.addAttribute("researchPieces", onFile);
        return "research/researchListOnFile";
    }

    @GetMapping("/DB")
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

    @GetMapping("/search")
    public String getResearchFromDB_search(@RequestParam("keyWord") String keyWord, Model model){
        String searchPath;

        if (keyWord.isBlank()){
            searchPath = ResearchPieceController.CloudWriteResearchPath;
        } else
            searchPath = ResearchPieceController.CloudWriteResearchPath + keyWord + "/search";

        ResearchPieceDTOList onDB = researchService.parseResearchURL(
                searchPath, ResearchPieceController.RESEARCH_XMLFILE);

        if (onDB == null){
            model.addAttribute("message", "Database offline");
            return "index";
        }

        model.addAttribute("researchPieces", onDB);
        return "research/researchList";
    }
}