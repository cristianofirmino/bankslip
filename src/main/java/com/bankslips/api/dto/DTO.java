package com.bankslips.api.dto;

import java.util.Optional;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstract Class for DTOs
 * @author Cristiano Firmino
 *
 */
@Data
@NoArgsConstructor
public abstract class DTO {

	private Optional<String> id = Optional.empty();
	
}
