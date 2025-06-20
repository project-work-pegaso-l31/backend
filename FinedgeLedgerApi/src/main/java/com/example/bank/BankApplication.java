package com.example.bank;

import com.example.bank.dto.CreateAccountDTO;
import com.example.bank.dto.CreateCustomerDTO;
import com.example.bank.service.AccountService;
import com.example.bank.service.CustomerService;
import com.example.bank.service.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	/**
	 * Seeder: inserisce un cliente, un conto e due transazioni demo
	 * ad ogni avvio (dati in‐memory).
	 */
	@Bean
	CommandLineRunner seed(CustomerService   customerSvc,
						   AccountService    accountSvc,
						   TransactionService txSvc) {

		return args -> {
			/* 1. Cliente demo ------------------------------------------------ */
			var mario = customerSvc.create(
					new CreateCustomerDTO(
							"Mario Rossi",
							"mario@mail.it",
							"RSSMRA80A01H501U"
					));

			/* 2. Conto demo (IBAN generato automaticamente) ----------------- */
			var conto = accountSvc.openAccount(
					new CreateAccountDTO(mario.id()));

			/* 3. Movimenti demo --------------------------------------------- */
			txSvc.credit(conto.id(), new BigDecimal("1000.00"), "Primo deposito");
			txSvc.debit (conto.id(), new BigDecimal("150.00"),  "Pagamento bolletta");
		};
	}
}
