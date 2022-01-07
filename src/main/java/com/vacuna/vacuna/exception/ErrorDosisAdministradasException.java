package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;

public class ErrorDosisAdministradasException extends VacunaException{

	private static final long serialVersionUID = -1669044336424992591L;

	public ErrorDosisAdministradasException() {
		super(HttpStatus.CONFLICT, "Paciente con alguna dosis administrada o cita ya asignada");
	}
}
