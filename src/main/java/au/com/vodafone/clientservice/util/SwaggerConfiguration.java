package au.com.vodafone.clientservice.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfiguration {

    private ApiInfo apiInfo() {
        return new ApiInfo("Client Service API", "Client Service API.", "2", "Terms of service",
                new Contact("Krishna K", "", ""),
                "License of API", "API license URL", Collections.emptyList());
    }
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("au.com.vodafone.clientservice.controller"))
                .build();
    }
}
