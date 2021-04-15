package com.example.cloudread.service;

import com.example.cloudread.JAXBmodel.*;
import lombok.extern.slf4j.Slf4j;
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

    private final XMLService xmlService;

    public ResearchService(XMLService xmlService) {
        this.xmlService = xmlService;
    }

    public ResearchPieceDTOList parseResearchURL(String urlPath, String filename) {

        ResearchPieceDTOList researchPieceDTOList = new ResearchPieceDTOList();
        ResearchPieceDTO researchPieceDTO = null;
        XMLEvent nextEvent;

        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

            // save request to XML first then parse XML
            if (xmlService.downloadXML(urlPath, filename)) {
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
                    if (endElement.getName().getLocalPart().equals(XMLCitationDTOList)){
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
                    if (endElement.getName().getLocalPart().equals(XMLStandfirst)){
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
                    if (endElement.getName().getLocalPart().equals(XMLKeyResultDTOList)){
                        return keyResultDTOList;
                    }
                }
            }
        } catch (XMLStreamException xmlStreamException) {
            log.debug("KeyResult XML stream error: " + xmlStreamException.getMessage());
        }

        return null;
    }
}
