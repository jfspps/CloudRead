package com.example.cloudread.restapi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class RESTAPIConfig {

    // these are defaults and can be overridden by restapi.properties, which in
    // turn can be overridden by command line parameters, for example
    // java -jar cloudread-0.0.1-SNAPSHOT.jar --rest.config.url=/oops/
    // or through the IDE environment program arguments
    private String url = null;
    private String fundamentals_path = null;
    private String research_path = null;
}
