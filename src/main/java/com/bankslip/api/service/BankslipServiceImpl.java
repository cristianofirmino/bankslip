package com.bankslip.api.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankslip.api.entity.Bankslip;
import com.bankslip.api.repository.BanksplitRepository;

/**
 * Banskslip Service Class Implementation
 * 
 * @author Cristiano Firmino
 *
 */
@Service
public class BankslipServiceImpl implements BankslipService {

	private static final Logger log = LoggerFactory.getLogger(BankslipServiceImpl.class);

	@Autowired
	private BanksplitRepository banksplitRepository;

	@Override
	public Optional<Bankslip> persist(Bankslip bankslip) {
		log.info("Persisting a bankslip: {}", bankslip);
		return Optional.ofNullable(this.banksplitRepository.save(bankslip));
	}

	@Override
	public Optional<Bankslip> findById(String id) {
		log.info("Finding a bankslip by ID {}", id);
		return this.banksplitRepository.findById(id);
	}

	@Override
	public void delete(String id) {
		log.info("Deleting a bankslip by ID {}", id);
		this.banksplitRepository.deleteById(id);

	}

}
