package com.bankslips.api.util;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.dto.DTO;

/**
 * Utility Class for Business Calculations
 * 
 * @author Cristiano Firmino
 *
 */
public final class CalcsUtil {

	/**
	 * Calculates the fine of a bankslip
	 * 
	 * @param DTO
	 * @return DTO
	 */
	public static DTO calcFine(DTO dto) {

		BankslipDTO bankslipDTO = (BankslipDTO) dto;
		BigDecimal fine = null;
		BigDecimal totalInCents = bankslipDTO.getTotalInCents();
		Date dueDate = bankslipDTO.getDueDate();
		Date paymentDate = Optional.ofNullable(bankslipDTO.getPaymentDate()).isPresent() ? bankslipDTO.getPaymentDate()
				: new Date();
		long days = ChronoUnit.DAYS.between(dueDate.toInstant(), paymentDate.toInstant());

		if (days > 10) {
			fine = (totalInCents.multiply(BigDecimal.valueOf(0.01))).multiply(BigDecimal.valueOf(days));
		}

		if (days > 0 && days <= 10) {
			fine = (totalInCents.multiply(BigDecimal.valueOf(0.005))).multiply(BigDecimal.valueOf(days));
		}

		bankslipDTO.setFine(fine);

		return bankslipDTO;
	}
}