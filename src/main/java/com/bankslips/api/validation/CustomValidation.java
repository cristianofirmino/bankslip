package com.bankslips.api.validation;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.response.CustomResponse;

public interface CustomValidation {

	public CustomResponse<Object> validation(BankslipDTO bankslipDTO);
}
