package com.bankslips.api.tests.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.dto.DTO;
import com.bankslips.api.entity.BankslipEntity;
import com.bankslips.api.service.IService;

/**
 * Utility Class for Mock Database and utilities
 * 
 * @author Cristiano Firmino
 *
 */
public final class DatabaseMockUtility {

	/**
	 * Return a new BankslipEntity.
	 * 
	 * @param customer
	 * @return BankslipEntity
	 */
	public static BankslipEntity newBankslip(String customer, int days) {
		BankslipEntity bankslip = new BankslipEntity();
		Date date = new Date();
		bankslip.setCustomer(customer);
		bankslip.setDueDate(DatesUtil.plusDays(date, days));
		bankslip.setTotalInCents(new BigDecimal(Math.random() * 1000.00));

		return bankslip;
	}

	/**
	 * Return an equal DTO received by the controller.
	 * 
	 * @param date
	 * @param customer
	 * @param totalInCents
	 * @return DTO
	 */
	public static DTO getOneDTO(Date date, String customer, BigDecimal totalInCents) {
		BankslipDTO dto = new BankslipDTO();
		dto.setCustomer(customer);
		dto.setDueDate(date);
		dto.setTotalInCents(totalInCents);

		return dto;
	}

	/**
	 * Return a Optional<DTO> persisted.
	 * 
	 * @param service
	 * @param date
	 * @param consumer
	 * @param totalInCents
	 * @return Optional<DTO>
	 */
	public static Optional<DTO> getOptionalDTOPersisted(IService service, Date date, String consumer,
			BigDecimal totalInCents) {
		DTO dto = getOneDTO(date, consumer, totalInCents);
		Optional<DTO> dtoPersisted = service.persist(dto);

		return dtoPersisted;

	}
}
