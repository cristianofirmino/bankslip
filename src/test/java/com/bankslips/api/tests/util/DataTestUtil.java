package com.bankslips.api.tests.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.dto.DTO;
import com.bankslips.api.entity.BankslipEntity;
import com.bankslips.api.repository.BankslipstRepository;
import com.bankslips.api.service.IService;

/**
 * Utility Test Class for many parse of the DTOs
 * @author crist
 *
 */
public final class DataTestUtil {

	public static BankslipDTO getBankslipDTO(IService service, String id) {
		BankslipDTO dtoPersistedAndPaid = (BankslipDTO) service.findById(id).get();
		return dtoPersistedAndPaid;
	}

	public static String getIdFromOptionalDTO(Optional<DTO> optionalDTO) {
		String id = optionalDTO.get().getId().get();
		return id;
	}

	public static String getIdFindCustomer(BankslipstRepository repository, int daysAgo, String customer) {
		String idDue = repository.findByCustomer(customer + daysAgo).get().getId();
		return idDue;
	}

	public static Optional<DTO> getDTOById(IService service, String id) {
		Optional<DTO> dtoFoundDueGreater10Days = service.findById(id);
		return dtoFoundDueGreater10Days;
	}

	public static Optional<BankslipEntity> getBankslipEntityById(BankslipstRepository repository, String id) {
		Optional<BankslipEntity> bankslipEntity = repository.findById(id);
		return bankslipEntity;
	}

	public static Optional<DTO> getOptionalDTO(IService service, DTO dto) {
		Optional<DTO> dtoPersisted = service.persist(dto);
		return dtoPersisted;
	}

	public static Optional<DTO> getOptionalDTO(IService service, Date date, String customer, BigDecimal totalInCents) {
		return DatabaseMockUtility.getOptionalDTOPersisted(service, date, customer, totalInCents);
	}

	public static Optional<BankslipEntity> getOptionalBankslipEntity(Optional<DTO> dtoPersisted,
			BankslipstRepository repository) {
		Optional<BankslipEntity> entityPersisted = repository.findById(dtoPersisted.get().getId().get());
		return entityPersisted;
	}

	public static BankslipDTO getBankslipDTO(DTO dto) {
		BankslipDTO bankslipDTO = (BankslipDTO) dto;
		bankslipDTO.setId(null);
		return bankslipDTO;
	}

	public static BankslipDTO getBankSlipDTO(Optional<DTO> dtoPersisted) {
		BankslipDTO bankslipDTOPersisted = (BankslipDTO) dtoPersisted.get();
		return bankslipDTOPersisted;
	}

}
