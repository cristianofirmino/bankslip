package com.bankslips.api.parse;

import com.bankslips.api.dto.DTO;
import com.bankslips.api.entity.AbstractEntity;

/**
 * Interface that defines the contracts for parses of DTO x AbstractEntity
 * 
 * @author Cristiano Firmino
 *
 */
public interface ParseEntityDTO {

	DTO parseEntityToDTOToShow(AbstractEntity entity);

	AbstractEntity parseDTOToEntityToPersist(DTO dto);

	AbstractEntity parseDTOToEntityWithPayToPersist(DTO dto);

	DTO parseEntityToDTOToPersist(AbstractEntity entity);

}
