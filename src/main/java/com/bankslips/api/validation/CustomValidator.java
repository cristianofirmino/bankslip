package com.bankslips.api.validation;

import com.bankslips.api.dto.DTO;
import com.bankslips.api.response.CustomResponse;

/**
 * Validator Interface
 * 
 * @author Cristiano Firmino
 *
 */
public interface CustomValidator {

	public CustomResponse<Object> validateForCreation(DTO dto);
}
