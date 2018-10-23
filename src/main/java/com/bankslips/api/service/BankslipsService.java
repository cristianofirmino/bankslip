package com.bankslips.api.service;

import java.util.Optional;

import com.bankslips.api.entity.Bankslip;

/**
 * Banskslip Service Interface
 * 
 * @author Cristiano Firmino
 *
 */
public interface BankslipsService {

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
