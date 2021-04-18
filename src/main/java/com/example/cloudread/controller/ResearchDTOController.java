package com.example.cloudread.controller;

import com.example.cloudread.JAXBmodel.FundamentalPieceDTO;
import com.example.cloudread.JAXBmodel.ResearchPieceDTO;
import com.example.cloudread.JAXBmodel.ResearchPieceDTOList;
import com.example.cloudread.controller.api.ResearchPieceController;
import com.example.cloudread.exceptions.NotFoundException;
import com.example.cloudread.service.api.ResearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

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
        ResearchPieceDTO found = getResearchPieceDTO(ID);
        if (found == null) {
            model.addAttribute("message", "Research article record not found");
            return "index";
        }

        model.addAttribute("research", found);
        return "research/researchPiece";
    }

    @GetMapping("/{id}/save")
    public void getSaveResearchToDOCX(HttpServletResponse response, @PathVariable("id") String ID) {
        ResearchPieceDTO found = getResearchPieceDTO(ID);

        if (found != null){
            // remove whitespace from the title and use it as part of the filename
            String filename = found.getTitle().replaceAll("\\s+","") + "_DOCX.docx";
            String fileNameAndPath = researchService.composeResearchDOCX(found, filename);

            // build DOCX into the HTTP response
            response.setContentType("application/octet-stream");
            String headerParam = "attachment; filename=" + fileNameAndPath;
            response.setHeader("Content-Disposition", headerParam);

            File file = new File(fileNameAndPath);

            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
                IOUtils.copy(stream, response.getOutputStream());
            } catch (IOException ioException){
                log.debug("Error sending DOCX to stream: " + ioException.getMessage());
            }
        } else
            throw new NotFoundException("Article not found");
    }

    private ResearchPieceDTO getResearchPieceDTO(String ID) {
        ResearchPieceDTOList onFile = researchService.parseResearchXMLFile(ResearchPieceController.RESEARCH_XMLFILE);

        return onFile.getResearchPiece().stream()
                .filter(researchPieceDTO -> researchPieceDTO.getId().equals(Long.valueOf(ID)))
                .findFirst()
                .orElse(null);
    }

}
