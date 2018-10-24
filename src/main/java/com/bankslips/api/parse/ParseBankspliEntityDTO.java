package com.bankslips.api.parse;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bankslips.api.dto.BankslipDTO;
import com.bankslips.api.dto.DTO;
import com.bankslips.api.entity.AbstractEntity;
import com.bankslips.api.entity.BankslipEntity;

@Component
public class ParseBankspliEntityDTO implements ParseEntityDTO {

	@Override
	public BankslipDTO parseEntityToDTO(AbstractEntity entity) {

		BankslipEntity bankslip = (BankslipEntity) entity;
		BankslipDTO dto = new BankslipDTO();

		dto.setId(Optional.of(bankslip.getId()));
		dto.setCustomer(bankslip.getCustomer());
		dto.setStatus(bankslip.getStatus());
		dto.setDueDate(bankslip.getDueDate());
		dto.setTotalInCents(bankslip.getTotalInCents());

		return dto;
	}

	@Override
	public BankslipEntity parseDTOToEntity(DTO dto) {

		BankslipDTO bankslipDTO = (BankslipDTO) dto;
		BankslipEntity entity = new BankslipEntity();

		entity.setCustomer(bankslipDTO.getCustomer());
		entity.setDueDate(bankslipDTO.getDueDate());
		entity.setTotalInCents(bankslipDTO.getTotalInCents());

		return entity;
	}

}
