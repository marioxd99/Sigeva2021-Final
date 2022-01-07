package com.vacuna.vacuna.model;

import javax.persistence.Lob;

import org.springframework.data.annotation.Id;

public class Usuario {
	/*
	 * CLASE USUARIO 
	 * Contiene todos los datos comunes a todos los usuarios creados en nuestra aplicación
	 * Las clases hijas heredan estos atributos
	 * Contiene getters y setters relativos a usuario
	 */
	protected String nombre;
	@Id
	protected String email;
	@Lob
	protected byte[] password;
	protected String dni;
	protected String tipoUsuario;
	protected String centroAsignado;
	public Usuario(String nombre, String email, byte[] password, String dni, String tipoUsuario, String centroAsignado) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.dni = dni; 
		this.tipoUsuario = tipoUsuario;
		this.centroAsignado = centroAsignado;
	}

	public String getCentroAsignado() {
		return centroAsignado;
	}

	public void setCentroAsignado(String centroAsignado) {
		this.centroAsignado = centroAsignado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public String getDni() {
		return dni;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}
}
