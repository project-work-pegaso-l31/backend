package com.example.bank.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "FinEdge Ledger API",
                version = "0.1.0",
                description = "Backend demo in-memory per gestione clienti, conti e movimenti"
        )
)
public class OpenApiConfig { }     // solo metadata
