package com.bankslips.api.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.bankslips.api.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Component
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BankslipDTO extends DTO {

	private String customer;
	private StatusEnum status;
	private BigDecimal fine;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
	@JsonProperty("due_date")
	private Date dueDate;

	@JsonProperty("total_in_cents")
	private BigDecimal totalInCents;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
	@JsonProperty("payment_date")
	private Date paymentDate;

}
