package com.bankslips.api.service;

import java.util.Date;
import java.util.List;
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
	 * @return Optional<DTO>
	 */
	Optional<DTO> persist(DTO dto);

	/**
	 * Update payment date of the entity in database.
	 * 
	 * @param optionalDTO
	 * @param date
	 * @return boolean
	 */
	boolean pay(Optional<DTO> optionalDTO, Date date);

	/**
	 * Find and returns a entity given an id.
	 * 
	 * @param id
	 * @return Optional<DTO>
	 */
	Optional<DTO> findById(String id);

	/**
	 * Search all entities in database.
	 * 
	 * @return List<DTO>
	 */
	List<DTO> findAll();

	/**
	 * Change status to cancel of the entity given an id.
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean cancel(String id);

}
