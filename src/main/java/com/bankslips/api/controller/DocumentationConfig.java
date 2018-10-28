package com.bankslips.api.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Documentation Configuration
 * @author Cristiano Firmino
 *
 */
@Configuration
@EnableSwagger2
public class DocumentationConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.bankslips.api.controller"))
				.paths(PathSelectors.any()).build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Banksplit API")
				.description("Bankslip API documentation.").version("1.0")
				.contact(new Contact("Cristiano Firmino Rodrigues", 
						"https://www.linkedin.com/in/cristiano-f-3b283b44/",
						"cristianofirmino@gmail.com")).build();
	}

}
