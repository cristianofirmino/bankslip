package com.bankslips.api.parse;

import com.bankslips.api.dto.DTO;
import com.bankslips.api.entity.AbstractEntity;

public interface ParseEntityDTO {

	DTO parseEntityToDTO(AbstractEntity entity);
	AbstractEntity parseDTOToEntity(DTO dto);
	
}
