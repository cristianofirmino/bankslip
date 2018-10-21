package com.bankslip.api.service;

import java.util.Optional;

import com.bankslip.api.entity.Bankslip;

/**
 * Banskslip Service Interface
 * 
 * @author Cristiano Firmino
 *
 */
public interface BankslipService {

	/**
	 * Persist a bankslip in database.
	 * 
	 * @param bankslip
	 * @return Bankslip
	 */
	Optional<Bankslip> persist(Bankslip bankslip);

	/**
	 * Find and returns a bankslip given an id.
	 * 
	 * @param id
	 * @return Optional<Bankslip>
	 */
	Optional<Bankslip> findById(String id);

	/**
	 * Delete a bankslip given an id.
	 * 
	 * @param id
	 * @return
	 */
	void delete(String id);

}
