package com.bankslips.api.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankslips.api.dto.DTO;
import com.bankslips.api.entity.AbstractEntity;
import com.bankslips.api.parse.ParseEntityDTO;
import com.bankslips.api.repository.Repository;

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
	private Repository repository;
	
	@Autowired
	ParseEntityDTO parse;

	@Override
	public Optional<DTO> persist(DTO dto) {
		log.info("Persisting a bankslip: {}", dto);
		AbstractEntity toEntity = parse.parseDTOToEntity(dto);
		AbstractEntity entity = this.repository.save(toEntity);
		DTO toDTO = parse.parseEntityToDTO(entity);		
		return Optional.ofNullable(toDTO);
	}

	@Override
	public Optional<DTO> findById(String id) {
		log.info("Finding a bankslip by ID {}", id);
		Optional<AbstractEntity> entity = this.repository.findById(id);
		DTO dto = parse.parseEntityToDTO(entity.get());
		return Optional.ofNullable(dto);
	}

	@Override
	public void delete(String id) {
		log.info("Deleting a bankslip by ID {}", id);
		this.repository.deleteById(id);

	}

}
