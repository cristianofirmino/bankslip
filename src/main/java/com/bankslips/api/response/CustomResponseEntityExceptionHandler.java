package com.bankslips.api.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Custom Response Entity Exception Handler Class
 * 
 * @author Cristiano Firmino
 *
 */
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	CustomResponse<?> customResponse;

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
		List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());

		String error;

		for (FieldError fieldError : fieldErrors) {
			error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
			errors.add(error);
		}

		for (ObjectError objectError : globalErrors) {
			error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
			errors.add(error);
		}
		customResponse.setErrors(errors);
		return new ResponseEntity<>(customResponse, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> errors = new ArrayList<>();

		errors.add("Unsupported content type: " + ex.getContentType());
		errors.add("Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes()));
		customResponse.setErrors(errors);

		return new ResponseEntity<>(customResponse, headers, status);
	}

	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest req) {

		List<String> errors = new ArrayList<>();
		Throwable mostSpecificCause = ex.getMostSpecificCause();

		if (mostSpecificCause != null) {
			errors = new ArrayList<>();
			customResponse.setErrors(errors);

			if (mostSpecificCause.toString().contains("InvalidFormatException")) {
				errors.add("Invalid bankslip provided.The possible reasons are:"
						+ " A field of the provided bankslip was null or with invalid values");
				return new ResponseEntity<>(customResponse, headers, HttpStatus.UNPROCESSABLE_ENTITY);

			} else {
				errors.add("Bankslip not provided in the request body");
				return new ResponseEntity<>(customResponse, headers, status);
			}

		}

		return new ResponseEntity<>(customResponse, headers, status);

	}
}