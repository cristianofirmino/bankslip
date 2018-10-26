package com.bankslips.api.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Custom Response Class
 * 
 * @author Cristiano Firmino
 *
 * @param <T>
 */

@Component
public class CustomResponse<T> {

	private List<String> errors;

	public CustomResponse() {
	}

	public List<String> getErrors() {
		if (this.errors == null) {
			this.errors = new ArrayList<String>();
		}
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public void setError(String error) {
		this.errors = new ArrayList<>();
		this.errors.add(error);
	}
}
