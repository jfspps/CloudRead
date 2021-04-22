package com.example.cloudread.controller;

import com.example.cloudread.JAXBmodel.FundamentalPieceDTO;
import com.example.cloudread.JAXBmodel.FundamentalPieceDTOList;
import com.example.cloudread.controller.api.FundamentalPieceController;
import com.example.cloudread.exceptions.NotFoundException;
import com.example.cloudread.restapi.RESTAPIConfig;
import com.example.cloudread.service.api.FundamentalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/fundamentals")
@Slf4j
public class FundamentalDTOController {

    private final FundamentalService fundamentalService;

    // see CloudWrite for routing
    private final String CloudWriteFundamentalsPath;

    public FundamentalDTOController(FundamentalService fundamentalService, RESTAPIConfig restapiConfig) {
        this.fundamentalService = fundamentalService;
        CloudWriteFundamentalsPath = restapiConfig.getUrl() + restapiConfig.getFundamentals_path();
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
                CloudWriteFundamentalsPath, FundamentalPieceController.FUNDAMENTAL_XMLFILE);

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
            searchPath = CloudWriteFundamentalsPath;
        } else
            searchPath = CloudWriteFundamentalsPath + keyWord + "/search";

        FundamentalPieceDTOList onDB = fundamentalService.parseFundamentalURL(
                searchPath, FundamentalPieceController.FUNDAMENTAL_XMLFILE);

        if (onDB == null){
            model.addAttribute("message", "Database offline");
            return "index";
        }

        model.addAttribute("fundamentalPieces", onDB);
        return "fundamental/fundamentalList";
    }

    @GetMapping("/{id}/retrieve")
    public String getFundamentalPiece(@PathVariable("id") String ID, Model model){
        FundamentalPieceDTO found = getFundamentalPieceDTO(ID);
        if (found == null) {
            model.addAttribute("message", "Fundamental article record not found");
            return "index";
        }

        model.addAttribute("fundamental", found);
        return "fundamental/fundamentalPiece";
    }

    @GetMapping("/{id}/save")
    public void getSaveFundamentalToDOCX(HttpServletResponse response, @PathVariable("id") String ID) {
        FundamentalPieceDTO found = getFundamentalPieceDTO(ID);

        if (found != null){
            // remove whitespace from the title and use it as part of the filename
            String filename = found.getTitle().replaceAll("\\s+","") + "_DOCX.docx";
            String fileNameAndPath = fundamentalService.composeFundamentalDOCX(found, filename);

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

    private FundamentalPieceDTO getFundamentalPieceDTO(String ID) {
        FundamentalPieceDTOList onFile = fundamentalService.parseFundamentalXMLFile(FundamentalPieceController.FUNDAMENTAL_XMLFILE);

        return onFile.getFundamentalPiece().stream()
                .filter(fundamentalPieceDTO -> fundamentalPieceDTO.getId().equals(Long.valueOf(ID)))
                .findFirst()
                .orElse(null);
    }
}
