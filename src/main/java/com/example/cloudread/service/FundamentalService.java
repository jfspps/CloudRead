package com.example.cloudread.service;

import com.example.cloudread.JAXBmodel.ConceptDTO;
import com.example.cloudread.JAXBmodel.ConceptDTOList;
import com.example.cloudread.JAXBmodel.FundamentalPieceDTO;
import com.example.cloudread.JAXBmodel.FundamentalPieceDTOList;
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
public class FundamentalService {

    private final XMLService xmlService;

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

    public FundamentalService(XMLService xmlService) {
        this.xmlService = xmlService;
    }

    // https://www.baeldung.com/java-stax
    public FundamentalPieceDTOList parseFundamentalURL(String urlPath, String filename) {

        FundamentalPieceDTOList fundamentalPieceDTOList = new FundamentalPieceDTOList();
        FundamentalPieceDTO fundamentalPieceDTO = null;
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
                    if (endElement.getName().getLocalPart().equals(XMLConceptDTOList)){
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
}
