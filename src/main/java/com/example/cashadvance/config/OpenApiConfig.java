package com.example.cashadvance.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CashAdvance API")
                        .version("v1")
                        .description("API for cash advance application")
                        .contact(new Contact().name("Team").email("dev@example.com"))
                );
    }
}
