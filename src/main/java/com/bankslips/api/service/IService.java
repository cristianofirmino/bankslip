package com.bankslips.api.service;

import java.util.Optional;

import com.bankslips.api.dto.DTO;

/**
 * Service Interface
 * 
 * @author Cristiano Firmino
 *
 */
public interface IService {

	/**
	 * Persist a entity in database.
	 * 
	 * @param DTO
	 * @return DTO
	 */
	Optional<DTO> persist(DTO dto);

	/**
	 * Find and returns a entity given an id.
	 * 
	 * @param id
	 * @return Optional<Bankslip>
	 */
	Optional<DTO> findById(String id);

	/**
	 * Delete a entity given an id.
	 * 
	 * @param id
	 * @return
	 */
	void delete(String id);

}
