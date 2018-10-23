package com.bankslips.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bankslips.api.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class BankslipDTO extends DTO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
	private Date dueDate;

	private BigDecimal totalInCents;

	private String customer;

	private StatusEnum status;

	@JsonProperty("due_date")
	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@JsonProperty("total_in_cents")
	public BigDecimal getTotalInCents() {
		return totalInCents;
	}

	public void setTotalInCents(BigDecimal totalInCents) {
		this.totalInCents = totalInCents;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BankslipDTO [Id=" + getId() + ", dueDate=" + dueDate + ", totalInCents=" + totalInCents + ", customer="
				+ customer + ", status=" + status + "]";
	}

}
