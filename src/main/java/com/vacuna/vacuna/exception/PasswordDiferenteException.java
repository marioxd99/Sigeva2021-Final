package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value=HttpStatus.CONFLICT, reason="La contraseña tiene que ser diferente a la original")
public class PasswordDiferenteException extends Exception{

	private static final long serialVersionUID = 1533065720952344178L;

	public PasswordDiferenteException() {

	}
}
