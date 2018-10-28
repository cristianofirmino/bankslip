package com.bankslips.api.parse;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.dto.DTO;
import com.bankslips.api.entity.AbstractEntity;
import com.bankslips.api.entity.BankslipEntity;

/**
 * Implementation Class for parse banksplip entity DTO
 * @author Cristiano Firmino
 *
 */
@Component
public class ParseBanksplipEntityDTO implements ParseEntityDTO {

	@Override
	public BankslipDTO parseEntityToDTOToShow(AbstractEntity entity) {
		BankslipEntity bankslip = (BankslipEntity) entity;
		BankslipDTO dto = new BankslipDTO();

		dto.setId(Optional.of(bankslip.getId()));
		dto.setCustomer(bankslip.getCustomer());
		dto.setStatus(bankslip.getStatus());
		dto.setDueDate(bankslip.getDueDate());
		dto.setTotalInCents(bankslip.getTotalInCents());
		
		if(Optional.ofNullable(bankslip.getPaymentDate()).isPresent()) {
			dto.setPaymentDate(bankslip.getPaymentDate());
		}

		return dto;
	}
	
	@Override
	public BankslipDTO parseEntityToDTOToPersist(AbstractEntity entity) {
		BankslipEntity bankslip = (BankslipEntity) entity;
		BankslipDTO dto = new BankslipDTO();
		
		dto.setCustomer(bankslip.getCustomer());
		dto.setStatus(bankslip.getStatus());
		dto.setDueDate(bankslip.getDueDate());
		dto.setTotalInCents(bankslip.getTotalInCents());

		return dto;
	}

	@Override
	public BankslipEntity parseDTOToEntityToPersist(DTO dto) {
		BankslipDTO bankslipDTO = (BankslipDTO) dto;
		BankslipEntity entity = new BankslipEntity();

		entity.setCustomer(bankslipDTO.getCustomer());
		entity.setDueDate(bankslipDTO.getDueDate());
		entity.setTotalInCents(bankslipDTO.getTotalInCents());

		return entity;
	}

	@Override
	public AbstractEntity parseDTOToEntityWithPayToPersist(DTO dto) {
		BankslipEntity entity = this.parseDTOToEntityToPersist(dto);
		entity.setPaymentDate(((BankslipDTO) dto).getPaymentDate());
		entity.setId(((BankslipDTO) dto).getId().get());

		return entity;
	}

}
