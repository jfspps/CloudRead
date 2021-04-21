package com.example.cloudread.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rest.config")
@Getter
@Setter
@RequiredArgsConstructor
public class RESTAPIConfig {

    private String URL = "http://localhost:5000/api";
}
