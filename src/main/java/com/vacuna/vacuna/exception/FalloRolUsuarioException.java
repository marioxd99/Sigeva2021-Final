package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;

public class FalloRolUsuarioException extends VacunaException{
	private static final long serialVersionUID = 3022922104369446824L;

	public FalloRolUsuarioException() {
		super(HttpStatus.CONFLICT, "No se ha podido encontrar el rol del usuario");
	}
}
