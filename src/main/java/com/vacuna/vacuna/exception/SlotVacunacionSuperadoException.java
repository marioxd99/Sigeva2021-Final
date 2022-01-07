package com.vacuna.vacuna.exception;

import org.springframework.http.HttpStatus;

public class SlotVacunacionSuperadoException extends VacunaException{

	private static final long serialVersionUID = -3648425728360294774L;

	public SlotVacunacionSuperadoException() {
		super(HttpStatus.CONFLICT, "La fecha es superior o igual al 31 de Enero de 2021");
	}
}
