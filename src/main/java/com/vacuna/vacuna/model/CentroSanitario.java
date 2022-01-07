package com.vacuna.vacuna.model;

import java.util.UUID;
import org.springframework.data.annotation.Id;

public class CentroSanitario {
	/*
	 * CLASE CENTROSANITARIO
	 * Contiene los atributos de los centrosanitario
	 * Contiene getters y setters relativos a centrosanitario
	 */
		@Id 
		private String id;
		private String nombre;
		private int dosisTotales;
		private int aforo;
		private int horaInicio;
		private int horaFin;
		private String localidad;
		private String provincia;
		
		
		public CentroSanitario(String nombre, int dosisTotales, int aforo,int horaInicio,int horaFin, String localidad,
				String provincia) {
			super();
			this.nombre = nombre;
			this.dosisTotales = dosisTotales; //Valor constante de momento
			this.aforo = aforo; //Valor constante de momento
			this.horaInicio = horaInicio;
			this.horaFin = horaFin;
			this.localidad = localidad;
			this.provincia = provincia;
		}

		
		public int getHoraInicio() {
			return horaInicio;
		}



		public void setHoraInicio(int horaInicio) {
			this.horaInicio = horaInicio;
		}



		public int getHoraFin() {
			return horaFin;
		}



		public void setHoraFin(int horaFin) {
			this.horaFin = horaFin;
		}



		public void restarDosis() {
			dosisTotales -= 2;
		}
		
		public String getNombre() {
			return nombre;
		}


		public void setNombre(String nombre) {
			this.nombre = nombre;
		}


		public int getDosisTotales() {
			return dosisTotales;
		}


		public void setDosisTotales(int dosisTotales) {
			this.dosisTotales = dosisTotales;
		}


		public int getAforo() {
			return aforo;
		}


		public void setAforo(int aforo) {
			this.aforo = aforo;
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

		
		public CentroSanitario() {
			this.id = UUID.randomUUID().toString();
		}
		

		public String getId() {
			return id;
		}
		
}
