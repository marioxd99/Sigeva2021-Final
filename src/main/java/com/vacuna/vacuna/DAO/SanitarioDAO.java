package com.vacuna.vacuna.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vacuna.vacuna.model.Sanitario;

@Repository
public interface SanitarioDAO extends MongoRepository<Sanitario, String> {
	/*
	 * Clase SanitarioDAO
	 * Contiene todo lo relativo a la base de datos de los sanitario
	 */
	Sanitario findByEmail(String email);
	Sanitario findByDni(String dni);
}
