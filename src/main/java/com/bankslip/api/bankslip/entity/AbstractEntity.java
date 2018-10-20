package com.bankslip.api.bankslip.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstract class for entities
 * 
 * @author Cristiano Firmino
 *
 * @param <ID>
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

	/**
	 * Identification attribute
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private ID id;
}
