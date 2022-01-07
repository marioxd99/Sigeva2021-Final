package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;

public class CentrosNoEncontradosException extends VacunaException{

	private static final long serialVersionUID = 1420679248958971029L;

	public CentrosNoEncontradosException() {
		super(HttpStatus.CONFLICT, "Fallo al obtener la lista de centros");
	}
}
