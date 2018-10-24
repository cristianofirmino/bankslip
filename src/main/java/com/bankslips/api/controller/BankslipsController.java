package com.bankslips.api.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@Autowired
	CustomResponse<Object> customResponse;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody BankslipDTO bankslipDTO) {

		log.info("Creating a banksplit: {}", bankslipDTO);
		customResponse = validator.validateForCreation(bankslipDTO);

		if (customResponse.getErrors().size() > 0) {
			log.error("Error creating a banksplit: {}", bankslipDTO.toString() + customResponse.getErrors());
			return ResponseEntity.unprocessableEntity().body(customResponse);
		}

		Optional<DTO> dto = service.persist(bankslipDTO);

		if (dto.isPresent()) {
			log.info("Banksplit created successfully.: {}", dto);
			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		}

		customResponse.setError("Could not validate the transaction");
		log.error("Error creating a banksplit: {}", bankslipDTO.toString() + customResponse.getErrors());
		return new ResponseEntity<>(customResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@GetMapping
	public ResponseEntity<?> listAll() {

		List<DTO> all = service.findAll();

		if (all.isEmpty() || all == null) {
			log.info("No results: {}");
			return ResponseEntity.noContent().build();
		}

		return new ResponseEntity<>(all, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") String id) {

		Optional<DTO> dto = this.service.findById(id);

		if (!dto.isPresent()) {
			customResponse.setError("Bankslip not found with the specified id");
			return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);
		}

		log.info("Banksplit was found.: {}", dto);
		return ResponseEntity.ok(dto);
	}

}
