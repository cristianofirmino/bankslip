package com.bankslips.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Main Class Application for Bankslip API
 * 
 * @author Cristiano Firmino
 *
 */
@SpringBootApplication
@Configuration
@ComponentScan(basePackages = { " com.bankslips.api.* " })
public class BankslipsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankslipsApplication.class, args);
	}

}
