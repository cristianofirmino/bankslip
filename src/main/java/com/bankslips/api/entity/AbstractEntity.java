package com.bankslips.api.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstract class for entities
 * 
 * @author Cristiano Firmino
 *
 * @param <ID>
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity {

	/**
	 * Identification attribute
	 */
	/*@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)*/
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
}
