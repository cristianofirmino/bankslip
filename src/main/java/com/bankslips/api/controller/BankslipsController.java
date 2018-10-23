package com.bankslips.api.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.dto.DTO;
import com.bankslips.api.response.CustomResponse;
import com.bankslips.api.service.IService;
import com.bankslips.api.validation.CustomValidator;

@RestController
@RequestMapping("/rest/bankslips")
@CrossOrigin(origins = "*")
public class BankslipsController {

	private static final Logger log = LoggerFactory.getLogger(BankslipsController.class);

	@Autowired
	private IService service;

	@Autowired
	private CustomValidator validator;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody DTO dto) {

		log.info("Creating a banksplit: {}", dto);
		CustomResponse<Object> customResponse = validator.validateForCreation(dto);

		if (customResponse.getErrors().size() > 0) {
			log.error("Error creating a banksplit: {}", dto.toString() + customResponse.getErrors());
			return ResponseEntity.unprocessableEntity().body(customResponse);
		}

		Optional<DTO> oDTO = service.persist(dto);

		if (oDTO.isPresent()) {
			log.info("Banksplit created successfully.: {}", oDTO);
			return new ResponseEntity<>(oDTO, HttpStatus.CREATED);
		}

		customResponse.setError("Could not validate the transaction");
		log.error("Error creating a banksplit: {}", dto.toString() + customResponse.getErrors());
		return new ResponseEntity<>(customResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
