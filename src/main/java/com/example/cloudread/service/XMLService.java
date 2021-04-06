package com.example.cloudread.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@Slf4j
public class XMLService {

    public String downloadXML(String urlPath){
        StringBuilder xmlResult = new StringBuilder();

        try {
            URL url = new URL(urlPath);                                                 // URL exception possible, handled first
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    // IOException possible
            int response = connection.getResponseCode();                                // IOException possible
            log.debug("downloadXML response code: " + response);

            // these calls could be chained together
            InputStream inputStream = connection.getInputStream();                      // IOException possible
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            int charsRead;
            char[] inputBuffer = new char[500];     // for capacities, see https://stackoverflow.com/questions/4638974/what-is-the-buffer-size-in-bufferedreader
            while (true){
                // read bufferedReader values into inputBuffer
                charsRead = bufferedReader.read(inputBuffer);
                if (charsRead < 0){
                    // read() returns -1 at end of stream
                    break;
                }
                if (charsRead > 0){
                    xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                }
            }
            bufferedReader.close();
            return xmlResult.toString();

        } catch (MalformedURLException e){
            log.debug("downloadXML invalid URL: " + e.getMessage());
        } catch (IOException e){
            log.debug("downloadXML IO exception: " + e.getMessage());
        } catch (SecurityException e){
            log.debug("downloadXML security exception: " + e.getMessage());
        }

        return null;
    }
}
