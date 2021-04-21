package com.example.cloudread;

import com.example.cloudread.config.RESTAPIConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RESTAPIConfig.class)
public class CloudReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudReadApplication.class, args);
    }

}
