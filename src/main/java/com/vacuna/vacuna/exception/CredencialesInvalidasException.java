package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value=HttpStatus.CONFLICT, reason="Credenciales invalidas")
public class CredencialesInvalidasException extends Exception{

	private static final long serialVersionUID = -2656015766161455311L;

	public CredencialesInvalidasException() {

	}
}
