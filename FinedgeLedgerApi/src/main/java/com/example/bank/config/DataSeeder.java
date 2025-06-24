package com.example.bank.config;

import com.example.bank.dto.CreateAccountDTO;
import com.example.bank.dto.CreateCustomerDTO;
import com.example.bank.service.AccountService;
import com.example.bank.service.CustomerService;
import com.example.bank.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final CustomerService customerSvc;
    private final AccountService accountSvc;
    private final TransactionService txSvc;
    private final ObjectMapper om = new ObjectMapper();

    @Bean
    ApplicationRunner seed() {
        return args -> {
            try (InputStream in = getClass().getResourceAsStream("/seed-data.json")) {
                JsonNode root = om.readTree(in).get("customers");

                for (JsonNode cNode : root) {
                    var cust = customerSvc.create(new CreateCustomerDTO(
                            cNode.get("fullName").asText(),
                            cNode.get("email").asText(),
                            cNode.get("fiscalCode").asText()));

                    for (JsonNode aNode : cNode.withArray("accounts")) {

                        /* apri conto ⇒ IBAN generato automaticamente */
                        var acc = accountSvc.openAccount(
                                new CreateAccountDTO(cust.id()));

                        /* movimenti iniziali */
                        for (JsonNode m : aNode.withArray("movements")) {
                            BigDecimal val  = new BigDecimal(m.get("amount").asText());
                            String desc     = m.get("desc").asText();
                            LocalDateTime at= LocalDateTime.parse(m.get("at").asText());

                            if (val.signum() >= 0)
                                txSvc.credit(acc.id(), val, desc, at);
                            else
                                txSvc.debit(acc.id(), val.abs(), desc, at);
                        }
                    }
                }
            }
        };
    }
}
