package com.bankslips.api.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.response.CustomResponse;

@Component
public class BanksplitCreateValidation implements CustomValidation {

	public CustomResponse<Object> validation(BankslipDTO bankslipDTO) {

		CustomResponse<Object> response = new CustomResponse<>();
		List<String> errors = new ArrayList<>();

		if (bankslipDTO.getCustomer() == null || bankslipDTO.getDueDate() == null
				|| bankslipDTO.getTotalInCents() == null) {

			errors.add("Invalid bankslip provided.The possible reasons are:"
					+ " A field of the provided bankslip was null or with invalid values");
			response.setErrors(errors);
			return response;
		}

		return response;

	}
}
