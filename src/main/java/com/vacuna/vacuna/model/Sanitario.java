package com.vacuna.vacuna.model;

public class Sanitario extends Usuario {
	/*
	 * CLASE SANITARIO
	 * Contiene los atributos de los sanitarios. Hereda de usuarios
	 * Contiene getters y setters relativos a sanitario
	 */
	private boolean primeraVez = true;
	public Sanitario(String nombre, String email, byte[] password, String dni, String tipoUsuario, String centroAsignado) {
		super(nombre, email, password, dni, tipoUsuario, centroAsignado);
	}

	public boolean isPrimeraVez() {
		return primeraVez;
	}

	public void setPrimeraVez(boolean primeraVez) {
		this.primeraVez = primeraVez;
	}
}