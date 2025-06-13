package com.example.bank.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "FinEdge Ledger API",
                version = "0.1.0",
                description = "Gestione clienti, conti correnti e transazioni"
        )
)
public class OpenApiConfig { /* solo meta – niente codice */ }
