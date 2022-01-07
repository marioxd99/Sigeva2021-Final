package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;

public class PrimerInicioException extends VacunaException{

	private static final long serialVersionUID = -6761168478704676696L;

	public PrimerInicioException() {
		super(HttpStatus.CONFLICT, "No se ha podido iniciar sesion");
	}
}
