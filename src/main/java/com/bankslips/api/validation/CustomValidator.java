package com.bankslips.api.validation;

import com.bankslips.api.dto.DTO;
import com.bankslips.api.response.CustomResponse;

public interface CustomValidator {

	public CustomResponse<Object> validateForCreation(DTO dto);
}
