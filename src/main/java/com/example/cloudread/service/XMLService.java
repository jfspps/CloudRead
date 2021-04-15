package com.example.cloudread.service;

import com.example.cloudread.JAXBmodel.FundamentalPieceDTO;
import com.example.cloudread.JAXBmodel.FundamentalPieceDTOList;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@Slf4j
public class XMLService {

    public static final String XMLFundamentalList = "FundamentalPieceDTOList";
    public static final String XMLFundamentalPiece = "fundamentalPiece";
    public static final String XML_ID = "id";
    public static final String XMLFundamentalTitle = "title";
    public static final String XMLFundamentalKeyword = "keyword";
    public static final String XMLFundamentalPrerequisites = "prerequisites";
    public static final String XMLFundamentalSummary = "summary";

    public static final String XMLConceptDTOList = "conceptDTOList";
    public static final String XMLConceptDTOs = "conceptDTOs";
    public static final String XMLConceptPurpose = "purpose";
    public static final String XMLConceptDesc = "description";
    public static final String XMLConceptPriority = "priority";


    public boolean downloadXML(String urlPath, String filename) {
        try {
            URL url = new URL(urlPath);                                                 // URL exception possible, handled first
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    // IOException possible
            connection.setRequestProperty("Accept", "application/xml");
            int response = connection.getResponseCode();                                // IOException possible
            log.debug("downloadXML response code: " + response);

            InputStream inputStream = connection.getInputStream();                      // IOException possible
            File targetFile = new File(filename);
            OutputStream outStream = new FileOutputStream(targetFile);

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outStream);
            log.info("XML file saved");

            return true;

        } catch (MalformedURLException e) {
            log.debug("Download XML invalid URL: " + e.getMessage());
        } catch (IOException e) {
            log.debug("Download XML IO exception: " + e.getMessage());
        }

        return false;
    }

    public boolean downloadJSON(String urlPath, String filename) {
        try {
            URL url = new URL(urlPath);                                                 // URL exception possible, handled first
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    // IOException possible
            connection.setRequestProperty("Accept", "application/json");
            int response = connection.getResponseCode();                                // IOException possible
            log.debug("downloadJSON response code: " + response);

            InputStream inputStream = connection.getInputStream();                      // IOException possible
            File targetFile = new File(filename);
            OutputStream outStream = new FileOutputStream(targetFile);

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outStream);
            log.info("JSON file saved");

            return true;

        } catch (MalformedURLException e) {
            log.debug("Download JSON invalid URL: " + e.getMessage());
        } catch (IOException e) {
            log.debug("Download JSON IO exception: " + e.getMessage());
        }

        return false;
    }

    // https://www.baeldung.com/java-stax
    public FundamentalPieceDTOList parseFundamentalURL(String urlPath, String filename) {

        FundamentalPieceDTOList fundamentalPieceDTOList = new FundamentalPieceDTOList();
        FundamentalPieceDTO fundamentalPieceDTO = null;

        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

            // save request to XML first then parse XML
            if (downloadXML(urlPath, filename)) {
                File retrieveFile = new File(filename);
                XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(retrieveFile));

                while (reader.hasNext()) {
                    XMLEvent nextEvent = reader.nextEvent();
                    if (nextEvent.isStartElement()) {
                        StartElement startElement = nextEvent.asStartElement();
                        switch (startElement.getName().getLocalPart()) {
                            case XMLFundamentalPiece:
                                fundamentalPieceDTO = new FundamentalPieceDTO();
                                break;
                            case XML_ID:
                                nextEvent = reader.nextEvent();
                                fundamentalPieceDTO.setId(Long.valueOf(nextEvent.asCharacters().getData()));
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
}
