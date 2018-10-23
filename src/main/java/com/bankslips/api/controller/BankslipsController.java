package com.bankslips.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.response.CustomResponse;
import com.bankslips.api.service.BankslipsService;
import com.bankslips.api.validation.CustomValidation;

@RestController
@RequestMapping("/rest/bankslips")
@CrossOrigin(origins = "*")
public class BankslipsController {

	private static final Logger log = LoggerFactory.getLogger(BankslipsController.class);

	@Autowired
	private BankslipsService banksplitsService;

	@Autowired
	private CustomValidation createValidation;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody BankslipDTO bankslipDTO) {

		log.info("Creating a banksplit: {}", bankslipDTO.toString());

		CustomResponse<Object> response = createValidation.validation(bankslipDTO);
		if (response.getErrors().size() > 0) {
			log.error("Error creating a banksplit: {}", bankslipDTO.toString() + response.getErrors());
			return ResponseEntity.unprocessableEntity().body(response);
		}

		return ResponseEntity.ok(bankslipDTO);

	}

}
