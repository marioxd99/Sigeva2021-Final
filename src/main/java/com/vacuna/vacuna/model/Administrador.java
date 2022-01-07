package com.vacuna.vacuna.model;

public class Administrador extends Usuario {
	/*
	 * CLASE ADMINISTRADOR
	 * Contiene los atributos de los administrador. Hereda de usuarios
	 */
	public Administrador(String nombre, String email, byte[] password, String dni, String tipoUsuario, String centroAsignado) {
		super(nombre, email, password, dni, tipoUsuario, centroAsignado);
	}
}