package com.bankslips.api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstract class for entities
 * 
 * @author Cristiano Firmino
 *
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column(name = "created", nullable = false, updatable = false)
	@CreationTimestamp
	private Date created;

	@Column(name = "updated", nullable = false)
	@UpdateTimestamp
	private Date updated;

}
