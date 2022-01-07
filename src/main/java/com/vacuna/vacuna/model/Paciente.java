package com.vacuna.vacuna.model;

public class Paciente extends Usuario {
	/*
	 * CLASE PACIENTE
	 * Contiene los atributos de los paciente. Hereda de usuarios
	 * Contiene getters y setters relativos a paciente
	 */
	private String dosisAdministradas;
	private String localidad;
	private String provincia;
	private boolean primeraVez = true;

	public Paciente(String nombre, String email, byte[] password, String dni, String tipoUsuario, String centroAsignado,
			String dosisAdministradas, String localidad, String provincia) {
		super(nombre, email, password, dni, tipoUsuario, centroAsignado);
		this.dosisAdministradas = dosisAdministradas;
		this.localidad = localidad;
		this.provincia = provincia;
	}

	public boolean isPrimeraVez() {
		return primeraVez;
	}

	public void setPrimeraVez(boolean primeraVez) {
		this.primeraVez = primeraVez;
	}

	public String getDosisAdministradas() {
		return dosisAdministradas;
	}

	public void setDosisAdministradas(String dosisAdministradas) {
		this.dosisAdministradas = dosisAdministradas;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

}
