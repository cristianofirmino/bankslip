package com.bankslips.api.dto;

import java.util.Optional;

public abstract class DTO {

	private Optional<String> id = Optional.empty();

	public Optional<String> getId() {
		return id;
	}

	public void setId(Optional<String> id) {
		this.id = id;
	}
}
