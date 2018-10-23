package com.bankslips.api.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
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

	//private T data;	
	private List<String> errors;

	public CustomResponse() {
	}

	/*public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}*/

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
