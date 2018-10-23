package com.bankslips.api.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.bankslips.api.enums.StatusEnum;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Entity class Bankslip
 * 
 * @author Cristiano Firmino
 *
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "bankslips")
public class Bankslip extends AbstractEntity {

	private Date dueDate;
	private Date created;
	private Date updated;
	private BigDecimal totalInCents;
	private String customer;
	private StatusEnum status;

	@Column(name = "due_date", nullable = false)
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Column(name = "created", nullable = false)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "updated", nullable = false)
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Column(name = "total_in_cents", nullable = false)
	public BigDecimal getTotalInCents() {
		return totalInCents;
	}

	public void setTotalInCents(BigDecimal totalInCents) {
		this.totalInCents = totalInCents;
	}

	@Column(name = "customer", nullable = false)
	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	@PreUpdate
	public void preUpdate() {
		this.updated = new Date();
	}

	@PrePersist
	public void prePersist() {
		final Date current = new Date();
		this.created = current;
		this.updated = current;
		this.status = StatusEnum.PENDING;
	}

}
