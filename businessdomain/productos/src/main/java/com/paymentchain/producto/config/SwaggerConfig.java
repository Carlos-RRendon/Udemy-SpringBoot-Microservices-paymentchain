package com.paymentchain.producto.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api(){
        return GroupedOpenApi.builder()
                .group("product")
                .packagesToScan("com.paymentchain.producto.controller")
                .build();
    }
}
