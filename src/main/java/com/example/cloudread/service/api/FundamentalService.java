package com.example.cloudread.service.api;

import com.example.cloudread.JAXBmodel.*;
import com.example.cloudread.config.WebClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
@Slf4j
public class FundamentalService {

    private final XML_JSONService xmlJSONService;

    public static final String XMLFundamentalPiece = "fundamentalPiece";
    public static final String XML_ID = "id";
    public static final String XMLFundamentalTitle = "title";
    public static final String XMLFundamentalKeyword = "keyword";
    public static final String XMLFundamentalPrerequisites = "prerequisites";
    public static final String XMLFundamentalSummary = "summary";

    public static final String XMLConceptDTOList = "conceptDTOList";
    public static final String XMLConceptDTO = "conceptDTO";
    public static final String XMLConceptPurpose = "purpose";
    public static final String XMLConceptDesc = "description";
    public static final String XMLConceptPriority = "priority";

    public FundamentalService(XML_JSONService xmlJSONService) {
        this.xmlJSONService = xmlJSONService;
    }

    public FundamentalPieceDTOList parseFundamentalURL(String urlPath, String filename) {
        // save request to XML first then parse XML
        if (xmlJSONService.downloadXML(urlPath, filename)) {
            return parseFundamentalXMLFile(filename);
        }
        return null;
    }

    public FundamentalPieceDTOList parseFundamentalXMLFile(String filename) {

        FundamentalPieceDTOList fundamentalPieceDTOList = new FundamentalPieceDTOList();
        FundamentalPieceDTO fundamentalPieceDTO = null;
        XMLEvent nextEvent;
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

            File retrieveFile = new File(filename);
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(retrieveFile));

            while (reader.hasNext()) {
                nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case XMLFundamentalPiece:
                            fundamentalPieceDTO = new FundamentalPieceDTO();
                            break;
                        case XML_ID:
                            nextEvent = reader.nextEvent();
                            fundamentalPieceDTO.setId(Long.parseLong(nextEvent.asCharacters().getData()));
                            break;
                        case XMLFundamentalTitle:
                            nextEvent = reader.nextEvent();
                            fundamentalPieceDTO.setTitle(nextEvent.asCharacters().getData());
                            break;
                        case XMLFundamentalKeyword:
                            nextEvent = reader.nextEvent();
                            fundamentalPieceDTO.setKeyword(nextEvent.asCharacters().getData());
                            break;
                        case XMLFundamentalPrerequisites:
                            nextEvent = reader.nextEvent();
                            fundamentalPieceDTO.setPrerequisites(nextEvent.asCharacters().getData());
                            break;
                        case XMLFundamentalSummary:
                            nextEvent = reader.nextEvent();
                            fundamentalPieceDTO.setSummary(nextEvent.asCharacters().getData());
                            break;
                        case XMLConceptDTOList:
                            fundamentalPieceDTO.setConceptDTOList(buildConceptList(reader));
                            break;
                    }
                }
                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals(XMLFundamentalPiece)) {
                        // reached the end of the fundamental piece
                        fundamentalPieceDTOList.getFundamentalPiece().add(fundamentalPieceDTO);
                    }
                }
            }

        } catch (IOException e) {
            log.debug("Download XML IO exception: " + e.getMessage());
        } catch (XMLStreamException xmlStreamException) {
            log.debug("XML stream error: " + xmlStreamException.getMessage());
        }

        log.debug("Fundamental articles processed: " + fundamentalPieceDTOList.getFundamentalPiece().size());
        return fundamentalPieceDTOList;
    }

    private ConceptDTOList buildConceptList(XMLEventReader reader) {
        ConceptDTOList conceptDTOList = new ConceptDTOList();
        ConceptDTO conceptDTO = null;
        XMLEvent nextEvent;

        try {
            while (reader.hasNext()) {
                nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case XMLConceptDTO:
                            conceptDTO = new ConceptDTO();
                            break;
                        case XML_ID:
                            nextEvent = reader.nextEvent();
                            conceptDTO.setId(Long.parseLong(nextEvent.asCharacters().getData()));
                            break;
                        case XMLConceptPurpose:
                            nextEvent = reader.nextEvent();
                            conceptDTO.setPurpose(nextEvent.asCharacters().getData());
                            break;
                        case XMLConceptDesc:
                            nextEvent = reader.nextEvent();
                            conceptDTO.setDescription(nextEvent.asCharacters().getData());
                            break;
                        case XMLConceptPriority:
                            nextEvent = reader.nextEvent();
                            conceptDTO.setPriority(Integer.valueOf(nextEvent.asCharacters().getData()));
                            break;
                    }
                }
                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals(XMLConceptDTO)) {
                        // reached the end of the conceptDTO
                        conceptDTOList.getConceptDTO().add(conceptDTO);
                    }
                    if (endElement.getName().getLocalPart().equals(XMLConceptDTOList)) {
                        // reached end of ConceptList
                        return conceptDTOList;
                    }
                }
            }
        } catch (XMLStreamException xmlStreamException) {
            log.debug("ConceptList XML stream error: " + xmlStreamException.getMessage());
        }

        return null;
    }

    /**
     * Composes a DOCX file, using the article title as a basis for the filename
     * @param fundamentalPieceDTO
     * @param filename
     * @return path and filename
     */
    public String composeFundamentalDOCX(FundamentalPieceDTO fundamentalPieceDTO, String filename){
        String filenamePath = WebClientConfig.DOCX_directory + filename;
        WordprocessingMLPackage wordPackage = null;

        try {
            wordPackage = WordprocessingMLPackage.createPackage();
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            mainDocumentPart.addStyledParagraphOfText("Title", fundamentalPieceDTO.getTitle());

            mainDocumentPart.addStyledParagraphOfText("Heading2", "Prerequisites");
            mainDocumentPart.addParagraphOfText(fundamentalPieceDTO.getPrerequisites());

            // assume that ordering of concepts was handled by CloudWrite
            mainDocumentPart.addStyledParagraphOfText("Heading2", "Concepts");
            for (ConceptDTO conceptDTO : fundamentalPieceDTO.getConceptDTOList().getConceptDTO()) {
                mainDocumentPart.addStyledParagraphOfText("Heading6", conceptDTO.getPurpose());
                mainDocumentPart.addParagraphOfText(conceptDTO.getDescription());
            }

            mainDocumentPart.addStyledParagraphOfText("Heading2", "Summary");
            mainDocumentPart.addParagraphOfText(fundamentalPieceDTO.getSummary());

            File exportFile = new File(filenamePath);
            wordPackage.save(exportFile);
        } catch (InvalidFormatException invalidFormatException){
            log.debug("Error building wordPackage: " + invalidFormatException.getMessage());
        } catch (Docx4JException e) {
            log.debug("Error saving DOCX: " + e.getMessage());
        }

        if (wordPackage != null){
            return filenamePath;
        } else
            return null;
    }
}
