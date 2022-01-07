package com.vacuna.vacuna.model;

import java.util.UUID;
import org.springframework.data.annotation.Id;

public class Cita {
	/*
	 * CLASE CITA
	 * Contiene los atributos de las citas
	 * Contiene getters y setters relativos a sanitario
	 */
	@Id 
	private String id ;
	private long fechaPrimeraDosis;
	private long fechaSegundaDosis;
	private String nombreCentro;
	private String dniPaciente;
	private String nombrePaciente;
	
	public Cita(long fechaPrimeraDosis, long fechaSegundaDosis, String nombreCentro, String dniPaciente,String nombrePaciente) {
		super();
		this.fechaPrimeraDosis = fechaPrimeraDosis;
		this.fechaSegundaDosis = fechaSegundaDosis;
		this.nombreCentro = nombreCentro;
		this.dniPaciente = dniPaciente;
		this.nombrePaciente = nombrePaciente;
	}
	

	public String getNombrePaciente() {
		return nombrePaciente;
	}


	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}


	public String getDniPaciente() {
		return dniPaciente;
	}

	public void setDniPaciente(String dniPaciente) {
		this.dniPaciente = dniPaciente;
	}


	public String getNombreCentro() {
		return nombreCentro;
	}

	public void setNombreCentro(String nombreCentro) {
		this.nombreCentro = nombreCentro;
	}

	public Cita() {
		this.id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}

	public long getFechaPrimeraDosis() {
		return fechaPrimeraDosis;
	}

	public void setFechaPrimeraDosis(long fechaPrimeraDosis) {
		this.fechaPrimeraDosis = fechaPrimeraDosis;
	}

	public long getFechaSegundaDosis() {
		return fechaSegundaDosis;
	}

	public void setFechaSegundaDosis(long fechaSegundaDosis) {
		this.fechaSegundaDosis = fechaSegundaDosis;
	}
}
