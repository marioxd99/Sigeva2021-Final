package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;

public class VacunaException extends Exception{

private static final long serialVersionUID = -7418484215113578717L;
	
	private final HttpStatus status;
	private final String message;
	
	public VacunaException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	@Override
	public String getMessage() {
		return this.message;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
