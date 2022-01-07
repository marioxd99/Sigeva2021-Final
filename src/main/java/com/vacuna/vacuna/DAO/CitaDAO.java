package com.vacuna.vacuna.DAO;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vacuna.vacuna.model.CentroSanitario;
import com.vacuna.vacuna.model.Cita;

@Repository
public interface CitaDAO extends MongoRepository<Cita, String>{
	/*
	 * Clase CitaDAO
	 * Contiene todo lo relativo a la base de datos de los cita
	 */
	Cita findByDniPaciente(String dniPaciente);
	
	void deleteById(String id);

	List<Cita> findAllByNombreCentro(String nombre);
}
