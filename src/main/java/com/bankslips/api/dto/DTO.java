package com.bankslips.api.dto;

import java.util.Optional;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class DTO {

	private Optional<String> id = Optional.empty();
	
}
