package com.bankslips.api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.dto.DTO;
import com.bankslips.api.entity.AbstractEntity;
import com.bankslips.api.entity.BankslipEntity;
import com.bankslips.api.enums.StatusEnum;
import com.bankslips.api.parse.ParseEntityDTO;
import com.bankslips.api.repository.BankslipstRepository;
import com.bankslips.api.util.CalcsUtil;

/**
 * Banskslip Service Class Implementation
 * 
 * @author Cristiano Firmino
 *
 */
@Service
public class BankslipsServiceImpl implements IService {

	private static final Logger log = LoggerFactory.getLogger(BankslipsServiceImpl.class);

	@Autowired
	private BankslipstRepository repository;

	@Autowired
	ParseEntityDTO parse;

	@Override
	public Optional<DTO> persist(DTO dto) {
		log.info("Persisting a bankslip: {}", dto);
		BankslipEntity toEntity = (BankslipEntity) this.parse.parseDTOToEntityToPersist(dto);
		AbstractEntity entity = this.repository.save(toEntity);
		DTO toDTO = this.parse.parseEntityToDTOToShow(entity);
		return Optional.ofNullable(toDTO);
	}

	@Override
	public Optional<DTO> findById(String id) {
		log.info("Finding a bankslip by ID {}", id);
		Optional<BankslipEntity> entity = this.repository.findById(id);

		if (!entity.isPresent()) {
			log.info("Bankslip ID not found {}", id);
			return Optional.empty();
		}

		DTO dto = this.parse.parseEntityToDTOToShow(entity.get());

		if (entity.get().getStatus().toString() != StatusEnum.PENDING.toString()) {
			return Optional.ofNullable(dto);
		}

		return calcFine(dto);
	}

	@Override
	public boolean cancel(String id) {
		log.info("Canceling a bankslip by ID {}", id);
		Optional<BankslipEntity> entity = this.repository.findById(id);

		if (!entity.isPresent()) {
			return false;
		}

		entity.get().setStatus(StatusEnum.CANCELED);
		Optional<BankslipEntity> entitySaved = Optional.ofNullable(this.repository.save(entity.get()));

		return entitySaved.isPresent();
	}

	@Override
	public List<DTO> findAll() {
		log.info("Searching all bankslips {}");
		List<DTO> allDTOs = new ArrayList<>();

		this.repository.findAll().forEach(entity -> {
			allDTOs.add(this.parse.parseEntityToDTOToShow(entity));
		});

		return allDTOs;
	}

	@Override
	public boolean pay(Optional<DTO> optionalDTO, Date date) {
		BankslipDTO bankslipDTO = (BankslipDTO) optionalDTO.get();
		bankslipDTO.setPaymentDate(date);
		BankslipEntity entity = (BankslipEntity) this.parse.parseDTOToEntityWithPayToPersist(bankslipDTO);
		entity.setStatus(StatusEnum.PAID);
		Optional<BankslipEntity> entitySaved = Optional.ofNullable(this.repository.save(entity));

		return entitySaved.isPresent();
	}

	private Optional<DTO> calcFine(DTO dto) {
		BankslipDTO bankslipDTO = (BankslipDTO) dto;
		Optional<BigDecimal> fine = CalcsUtil.calcFine(bankslipDTO.getDueDate(), bankslipDTO.getTotalInCents());

		if (fine.isPresent())
			bankslipDTO.setFine(fine.get());

		return Optional.ofNullable(bankslipDTO);
	}

}
