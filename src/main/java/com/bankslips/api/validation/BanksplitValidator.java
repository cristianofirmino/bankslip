package com.bankslips.api.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.dto.DTO;
import com.bankslips.api.response.CustomResponse;

/**
 * Banksplit Validator
 * 
 * @author Cristiano Firmino
 *
 */
@Component
public class BanksplitValidator implements CustomValidator {

	public CustomResponse<Object> validateForCreation(DTO dto) {

		BankslipDTO bankslipDTO = (BankslipDTO) dto;
		CustomResponse<Object> response = new CustomResponse<>();
		List<String> errors = new ArrayList<>();

		if (bankslipDTO.getCustomer() == null || bankslipDTO.getDueDate() == null
				|| bankslipDTO.getTotalInCents() == null || bankslipDTO.getTotalInCents().doubleValue() <= 0) {

			errors.add("Invalid bankslip provided.The possible reasons are:"
					+ " A field of the provided bankslip was null or with invalid values");
			response.setErrors(errors);
			return response;
		}

		return response;

	}
}
