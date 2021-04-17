package com.example.cloudread.controller;

import com.example.cloudread.JAXBmodel.FundamentalPieceDTO;
import com.example.cloudread.JAXBmodel.ResearchPieceDTO;
import com.example.cloudread.JAXBmodel.ResearchPieceDTOList;
import com.example.cloudread.controller.api.ResearchPieceController;
import com.example.cloudread.service.api.ResearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/research")
@Slf4j
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

    @GetMapping("/{id}/retrieve")
    public String getResearchPiece(@PathVariable("id") String ID, Model model){
        ResearchPieceDTO found = getResearchPieceDTO(ID, model);
        if (found == null) return "index";

        model.addAttribute("research", found);
        return "research/researchPiece";
    }

    @GetMapping("/{id}/save")
    public String getSaveResearchToDOCX(@PathVariable("id") String ID, Model model){
        ResearchPieceDTO found = getResearchPieceDTO(ID, model);

        // todo: run this on a separate thread
        if (found != null){
            // remove whitespace from the title and use it as part of the filename
            log.info("DOCX saved: " + researchService.composeResearchDOCX(found, found.getTitle().replaceAll("\\s+","") + "_DOCX.docx"));
        }

        model.addAttribute("research", found);
        return "research/researchPiece";
    }

    private ResearchPieceDTO getResearchPieceDTO(String ID, Model model) {
        ResearchPieceDTOList onFile = researchService.parseResearchXMLFile(ResearchPieceController.RESEARCH_XMLFILE);

        ResearchPieceDTO found = onFile.getResearchPiece().stream()
                .filter(researchPieceDTO -> researchPieceDTO.getId().equals(Long.valueOf(ID)))
                .findFirst()
                .orElse(null);

        if (found == null){
            model.addAttribute("message", "Research article not on file");
            return null;
        }
        return found;
    }

}
