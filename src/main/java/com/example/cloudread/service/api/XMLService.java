package com.example.cloudread.service.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@Slf4j
public class XMLService {

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
}
