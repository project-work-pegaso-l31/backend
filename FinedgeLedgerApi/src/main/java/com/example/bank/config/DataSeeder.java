package com.example.bank.config;

import com.example.bank.dto.CreateAccountDTO;
import com.example.bank.dto.CreateCustomerDTO;
import com.example.bank.service.AccountService;
import com.example.bank.service.CustomerService;
import com.example.bank.service.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(CustomerService custSvc,
                           AccountService  accSvc,
                           TransactionService txSvc) {

        return args -> {

            /* ---------- Mario Rossi ---------- */
            var mario = custSvc.create(new CreateCustomerDTO(
                    "Mario Rossi", "mario@demo.it", "RSSMRA80A01H501U"));

            var contoMario1 = accSvc.openAccount(new CreateAccountDTO(mario.id()));
            var contoMario2 = accSvc.openAccount(new CreateAccountDTO(mario.id()));

            addTx(txSvc, contoMario1.id(),  800, "Stipendio",  LocalDate.of(2025, 1, 25));
            addTx(txSvc, contoMario1.id(), -120, "Affitto",    LocalDate.of(2025, 2,  1));
            addTx(txSvc, contoMario2.id(), 1500, "Bonus",      LocalDate.of(2025, 3, 10));

            /* ---------- Laura Bianchi ---------- */
            var laura = custSvc.create(new CreateCustomerDTO(
                    "Laura Bianchi", "laura@demo.it", "BNCLRA90B55F205J"));

            var contoLaura = accSvc.openAccount(new CreateAccountDTO(laura.id()));

            addTx(txSvc, contoLaura.id(), 2000, "Stipendio",   LocalDate.of(2025, 1, 27));
            addTx(txSvc, contoLaura.id(), -300, "Spesa",       LocalDate.of(2025, 4, 15));

            /* ---------- Luca Verdi ---------- */
            var luca = custSvc.create(new CreateCustomerDTO(
                    "Luca Verdi", "luca@demo.it", "VRDLGU85C12D612Q"));

            var contoLuca = accSvc.openAccount(new CreateAccountDTO(luca.id()));

            addTx(txSvc, contoLuca.id(), 1000, "Vendita usato",LocalDate.of(2025, 2,  5));
            addTx(txSvc, contoLuca.id(),  -50, "Caffè",        LocalDate.of(2025, 2,  6));

            /* ---------- Giulia Neri ---------- */
            var giulia = custSvc.create(new CreateCustomerDTO(
                    "Giulia Neri", "giulia@demo.it", "NRIGLU92D68E888X"));

            var contoGiulia = accSvc.openAccount(new CreateAccountDTO(giulia.id()));
            addTx(txSvc, contoGiulia.id(), 500, "Regalo",      LocalDate.of(2025, 5, 20));

            /* ---------- Paolo Gallo ---------- */
            var paolo = custSvc.create(new CreateCustomerDTO(
                    "Paolo Gallo", "paolo@demo.it", "GLLPLA78H30F205V"));

            var contoPaolo = accSvc.openAccount(new CreateAccountDTO(paolo.id()));
            addTx(txSvc, contoPaolo.id(), 900, "Rimborso",     LocalDate.of(2025, 6,  8));
        };
    }

    /* ------------------------------------------------------------------ */
    private void addTx(TransactionService svc,
                       UUID accountId,
                       int amount,
                       String descr,
                       LocalDate date) {

        LocalDateTime when = date.atTime(10, 0);

        if (amount >= 0) {
            svc.credit(accountId, BigDecimal.valueOf(amount), descr, when);
        } else {
            svc.debit(accountId,  BigDecimal.valueOf(-amount), descr, when);
        }
    }
}
