package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;

public class CambioPasswordException extends VacunaException{

	private static final long serialVersionUID = -3944299722578489097L;

	public CambioPasswordException() {
		super(HttpStatus.CONFLICT, "Algo ha ido mal. Password no modificada");
	}
}
