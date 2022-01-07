package com.vacuna.vacuna.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.vacuna.vacuna.model.CentroSanitario;

public interface CentroSanitarioDAO extends MongoRepository<CentroSanitario, String> {
	/*
	 * Clase CentroSanitarioDAO
	 * Contiene todo lo relativo a la base de datos de los centroSanitario
	 */
	CentroSanitario findByNombre(String nombre);

	public CentroSanitario deleteByNombre(String nombre);
}
