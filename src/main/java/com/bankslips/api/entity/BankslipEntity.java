package com.bankslips.api.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.bankslips.api.enums.StatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity class Bankslip
 * 
 * @author Cristiano Firmino
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "bankslips")
public class BankslipEntity extends AbstractEntity {

	@Column(name = "due_date", nullable = false)
	private Date dueDate;

	@Column(name = "total_in_cents", nullable = false)
	private BigDecimal totalInCents;

	@Column(name = "customer", nullable = false)
	private String customer;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StatusEnum status;

	@Column(name = "payment_date", nullable = true)
	private Date paymentDate;

	@PrePersist
	public void prePersist() {
		this.status = StatusEnum.PENDING;
	}

}
