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

@SpringBootApplication(
		exclude = {  // -- resta per evitare JPA
				org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
				org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
		})
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	/** Inizializza 1 cliente, 1 conto e 2 movimenti */
	@Bean
	CommandLineRunner seed(CustomerService customerSvc,
						   AccountService accountSvc,
						   TransactionService txSvc) {
		return args -> {
			// 1. cliente
			var mario = customerSvc.create(
					new CreateCustomerDTO("Mario Rossi",
							"mario.rossi@example.com",
							"RSSMRA80A01H501U"));

			// 2. conto
			var conto = accountSvc.openAccount(
					new CreateAccountDTO(mario.id(),
							"IT60X0542811101000000123456"));

			// 3. accredito + addebito
			txSvc.credit(conto.id(), new BigDecimal("1500.00"), "stipendio");
			txSvc.debit(conto.id(),  new BigDecimal("200.00"),  "bolletta");
		};
	}
}
