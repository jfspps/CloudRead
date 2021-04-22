package com.example.cloudread.config;

import com.example.cloudread.restapi.RESTAPIConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@PropertySource("classpath:restapi.properties")
public class WebClientConfig implements WebMvcConfigurer {
    public static final String BASE_URL = "http://localhost:5000/api";

    public static final String DOCX_directory = "./DOCX/";

    // required for AWS or other systems that require explicit template resolvers
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver =
                new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");

        return templateResolver;
    }

    @Bean
    public RESTAPIConfig restapiConfig(@Value("${rest.config.url}") String url){
        RESTAPIConfig restapiConfig = new RESTAPIConfig();
        restapiConfig.setUrl(url);

        return restapiConfig;
    }
}