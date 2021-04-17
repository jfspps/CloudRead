package com.example.cloudread.controller;

import com.example.cloudread.JAXBmodel.FundamentalPieceDTO;
import com.example.cloudread.JAXBmodel.FundamentalPieceDTOList;
import com.example.cloudread.JAXBmodel.ResearchPieceDTO;
import com.example.cloudread.JAXBmodel.ResearchPieceDTOList;
import com.example.cloudread.controller.api.FundamentalPieceController;
import com.example.cloudread.controller.api.ResearchPieceController;
import com.example.cloudread.service.api.FundamentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/fundamentals")
public class FundamentalDTOController {

    private final FundamentalService fundamentalService;

    public FundamentalDTOController(FundamentalService fundamentalService) {
        this.fundamentalService = fundamentalService;
    }

    @GetMapping("/file")
    public String getFundamentalsFromFile(Model model){
        FundamentalPieceDTOList onFile = fundamentalService.parseFundamentalXMLFile(FundamentalPieceController.FUNDAMENTAL_XMLFILE);
        model.addAttribute("fundamentalPieces", onFile);
        return "fundamental/fundamentalListOnFile";
    }

    @GetMapping("/DB")
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

    @GetMapping("/search")
    public String getFundamentalsFromDB_search(@RequestParam("keyWord") String keyWord, Model model){
        String searchPath;

        if (keyWord.isBlank()){
            searchPath = FundamentalPieceController.CloudWriteFundamentalsPath;
        } else
            searchPath = FundamentalPieceController.CloudWriteFundamentalsPath + keyWord + "/search";

        FundamentalPieceDTOList onDB = fundamentalService.parseFundamentalURL(
                searchPath, FundamentalPieceController.FUNDAMENTAL_XMLFILE);

        if (onDB == null){
            model.addAttribute("message", "Database offline");
            return "index";
        }

        model.addAttribute("fundamentalPieces", onDB);
        return "fundamental/fundamentalList";
    }

    @GetMapping("/{id}")
    public String getFundamentalPiece(@PathVariable("id") String ID, Model model){
        FundamentalPieceDTOList onFile = fundamentalService.parseFundamentalXMLFile(FundamentalPieceController.FUNDAMENTAL_XMLFILE);

        FundamentalPieceDTO found = onFile.getFundamentalPiece().stream()
                .filter(fundamentalPieceDTO -> fundamentalPieceDTO.getId().equals(Long.valueOf(ID)))
                .findFirst()
                .orElse(null);

        if (found == null){
            model.addAttribute("message", "Fundamental article not on file");
            return "index";
        }

        model.addAttribute("fundamental", found);
        return "fundamental/fundamentalPiece";
    }
}
