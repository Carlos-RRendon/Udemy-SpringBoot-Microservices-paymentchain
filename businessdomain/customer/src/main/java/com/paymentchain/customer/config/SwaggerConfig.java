package com.paymentchain.customer.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    /*@Bean
    public Docket api(){
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.paymentchain.customer.controller"))
                .build();
    }*/

    @Bean
    public GroupedOpenApi api(){
        return GroupedOpenApi.builder()
                .group("customer")
                .packagesToScan("com.paymentchain.customer.controller")
                .build();
    }

}
