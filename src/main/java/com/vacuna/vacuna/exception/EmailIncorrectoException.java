package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value=HttpStatus.CONFLICT, reason="Rellena todos los campos correctamente")
public class EmailIncorrectoException extends Exception{

	private static final long serialVersionUID = 8319572958302591099L;

	public EmailIncorrectoException() {
	}
}
