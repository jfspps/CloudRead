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
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class ResearchService {

    public static final String XMLResearchPiece = "researchPiece";
    public static final String XML_ID = "id";
    public static final String XMLResearchTitle = "title";
    public static final String XMLResearchKeyword = "keyword";
    public static final String XMLResearchPurpose = "researchPurpose";
    public static final String XMLResearchProgress = "currentProgress";
    public static final String XMLReseachFutureWork = "futureWork";

    public static final String XMLStandfirst = "standfirstDTO";
    public static final String XMLStandfirstRationale = "rationale";
    public static final String XMLStandfirstApproach = "approach";

    public static final String XMLKeyResultDTOList = "keyResultDTOList";
    public static final String XMLKeyResultDTO = "keyResultDTO";
    public static final String XMLKeyResultDesc = "description";
    public static final String XMLKeyResultPriority = "priority";

    public static final String XMLCitationDTOList = "citationDTOList";
    public static final String XMLCitationDTO = "citationDTO";
    public static final String XMLCitationRef = "reference";

    private final XML_JSONService xmlJSONService;

    public ResearchService(XML_JSONService xmlJSONService) {
        this.xmlJSONService = xmlJSONService;
    }

    public ResearchPieceDTOList parseResearchURL(String urlPath, String filename) {
        // save request to XML first then parse XML
        if (xmlJSONService.downloadXML(urlPath, filename)) {
            return parseResearchXMLFile(filename);
        }
        return null;
    }

    public ResearchPieceDTOList parseResearchXMLFile(String filename) {

        ResearchPieceDTOList researchPieceDTOList = new ResearchPieceDTOList();
        ResearchPieceDTO researchPieceDTO = null;
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
                        case XMLResearchPiece:
                            researchPieceDTO = new ResearchPieceDTO();
                            break;
                        case XML_ID:
                            nextEvent = reader.nextEvent();
                            researchPieceDTO.setId(Long.parseLong(nextEvent.asCharacters().getData()));
                            break;
                        case XMLResearchTitle:
                            nextEvent = reader.nextEvent();
                            researchPieceDTO.setTitle(nextEvent.asCharacters().getData());
                            break;
                        case XMLResearchKeyword:
                            nextEvent = reader.nextEvent();
                            researchPieceDTO.setKeyword(nextEvent.asCharacters().getData());
                            break;
                        case XMLResearchPurpose:
                            nextEvent = reader.nextEvent();
                            researchPieceDTO.setResearchPurpose(nextEvent.asCharacters().getData());
                            break;
                        case XMLResearchProgress:
                            nextEvent = reader.nextEvent();
                            researchPieceDTO.setCurrentProgress(nextEvent.asCharacters().getData());
                            break;
                        case XMLReseachFutureWork:
                            nextEvent = reader.nextEvent();
                            researchPieceDTO.setFutureWork(nextEvent.asCharacters().getData());
                            break;
                        case XMLKeyResultDTOList:
                            researchPieceDTO.setKeyResultDTOList(buildKeyResultList(reader));
                            break;
                        case XMLStandfirst:
                            researchPieceDTO.setStandfirstDTO(buildStandfirst(reader));
                            break;
                        case XMLCitationDTOList:
                            researchPieceDTO.setCitationDTOList(buildCitationList(reader));
                            break;
                    }
                }
                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals(XMLResearchPiece)) {
                        // reached the end of the fundamental piece
                        researchPieceDTOList.getResearchPiece().add(researchPieceDTO);
                    }
                }
            }
        } catch (IOException e) {
            log.debug("Download XML IO exception: " + e.getMessage());
        } catch (XMLStreamException xmlStreamException) {
            log.debug("XML stream error: " + xmlStreamException.getMessage());
        }

        log.debug("Research articles processed: " + researchPieceDTOList.getResearchPiece().size());
        return researchPieceDTOList;
    }

    private CitationDTOList buildCitationList(XMLEventReader reader) {
        CitationDTOList citationDTOList = new CitationDTOList();
        CitationDTO citationDTO = null;
        XMLEvent nextEvent;

        try {
            while (reader.hasNext()) {
                nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case XMLCitationDTO:
                            citationDTO = new CitationDTO();
                            break;
                        case XML_ID:
                            nextEvent = reader.nextEvent();
                            citationDTO.setId(Long.parseLong(nextEvent.asCharacters().getData()));
                            break;
                        case XMLCitationRef:
                            nextEvent = reader.nextEvent();
                            citationDTO.setReference(nextEvent.asCharacters().getData());
                            break;
                    }
                }
                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals(XMLCitationDTO)) {
                        citationDTOList.getCitationDTO().add(citationDTO);
                    }
                    if (endElement.getName().getLocalPart().equals(XMLCitationDTOList)) {
                        return citationDTOList;
                    }
                }
            }
        } catch (XMLStreamException xmlStreamException) {
            log.debug("CitationList XML stream error: " + xmlStreamException.getMessage());
        }

        return null;
    }

    private StandfirstDTO buildStandfirst(XMLEventReader reader) {
        StandfirstDTO standfirstDTO = new StandfirstDTO();
        XMLEvent nextEvent;

        try {
            while (reader.hasNext()) {
                nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case XMLStandfirstRationale:
                            nextEvent = reader.nextEvent();
                            standfirstDTO.setRationale(nextEvent.asCharacters().getData());
                            break;
                        case XML_ID:
                            nextEvent = reader.nextEvent();
                            standfirstDTO.setId(Long.parseLong(nextEvent.asCharacters().getData()));
                            break;
                        case XMLStandfirstApproach:
                            nextEvent = reader.nextEvent();
                            standfirstDTO.setApproach(nextEvent.asCharacters().getData());
                            break;
                    }
                }
                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals(XMLStandfirst)) {
                        return standfirstDTO;
                    }
                }
            }
        } catch (XMLStreamException xmlStreamException) {
            log.debug("Standfirst XML stream error: " + xmlStreamException.getMessage());
        }

        return null;
    }

    private KeyResultDTOList buildKeyResultList(XMLEventReader reader) {
        KeyResultDTOList keyResultDTOList = new KeyResultDTOList();
        KeyResultDTO keyResultDTO = null;
        XMLEvent nextEvent;

        try {
            while (reader.hasNext()) {
                nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    switch (startElement.getName().getLocalPart()) {
                        case XMLKeyResultDTO:
                            keyResultDTO = new KeyResultDTO();
                            break;
                        case XML_ID:
                            nextEvent = reader.nextEvent();
                            keyResultDTO.setId(Long.parseLong(nextEvent.asCharacters().getData()));
                            break;
                        case XMLKeyResultDesc:
                            nextEvent = reader.nextEvent();
                            keyResultDTO.setDescription(nextEvent.asCharacters().getData());
                            break;
                        case XMLKeyResultPriority:
                            nextEvent = reader.nextEvent();
                            keyResultDTO.setPriority(Integer.parseInt(nextEvent.asCharacters().getData()));
                            break;
                    }
                }
                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals(XMLKeyResultDTO)) {
                        keyResultDTOList.getKeyResultDTO().add(keyResultDTO);
                    }
                    if (endElement.getName().getLocalPart().equals(XMLKeyResultDTOList)) {
                        return keyResultDTOList;
                    }
                }
            }
        } catch (XMLStreamException xmlStreamException) {
            log.debug("KeyResult XML stream error: " + xmlStreamException.getMessage());
        }

        return null;
    }

    public String composeResearchDOCX(ResearchPieceDTO researchPieceDTO, String filename){
        String filenamePath = WebClientConfig.DOCX_directory + filename;
        WordprocessingMLPackage wordPackage = null;

        try {
            Files.createDirectories(Paths.get(WebClientConfig.DOCX_directory));
            wordPackage = WordprocessingMLPackage.createPackage();
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            mainDocumentPart.addStyledParagraphOfText("Title", researchPieceDTO.getTitle());

            mainDocumentPart.addStyledParagraphOfText("Heading2", "Standfirst");
            mainDocumentPart.addParagraphOfText(researchPieceDTO.getStandfirstDTO().getRationale());
            mainDocumentPart.addParagraphOfText(researchPieceDTO.getStandfirstDTO().getApproach());

            mainDocumentPart.addStyledParagraphOfText("Heading2", "Research purpose");
            mainDocumentPart.addParagraphOfText(researchPieceDTO.getResearchPurpose());

            mainDocumentPart.addStyledParagraphOfText("Heading2", "Current status");
            mainDocumentPart.addParagraphOfText(researchPieceDTO.getCurrentProgress());

            // assume that ordering of results was handled by CloudWrite
            mainDocumentPart.addStyledParagraphOfText("Heading2", "Key Results");
            for (KeyResultDTO keyResultDTO : researchPieceDTO.getKeyResultDTOList().getKeyResultDTO()) {
                mainDocumentPart.addParagraphOfText(keyResultDTO.getDescription());
            }

            mainDocumentPart.addStyledParagraphOfText("Heading2", "Future work");
            mainDocumentPart.addParagraphOfText(researchPieceDTO.getFutureWork());

            mainDocumentPart.addStyledParagraphOfText("Heading2", "Citations");
            for (CitationDTO citationDTO : researchPieceDTO.getCitationDTOList().getCitationDTO()) {
                mainDocumentPart.addParagraphOfText(citationDTO.getReference());
            }

            File exportFile = new File(filenamePath);
            wordPackage.save(exportFile);
        } catch (InvalidFormatException invalidFormatException){
            log.debug("Error building wordPackage: " + invalidFormatException.getMessage());
        } catch (Docx4JException e) {
            log.debug("Error saving DOCX: " + e.getMessage());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        if (wordPackage != null){
            return filenamePath;
        } else
            return null;
    }
}
