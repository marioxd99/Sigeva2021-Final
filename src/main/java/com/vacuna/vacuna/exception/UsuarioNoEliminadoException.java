package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;

public class UsuarioNoEliminadoException extends VacunaException{

	private static final long serialVersionUID = -2811001448616422254L;

	public UsuarioNoEliminadoException() {
		super(HttpStatus.CONFLICT, "Este usuario no puede ser eliminado");
	}
}
