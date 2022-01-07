package com.vacuna.vacuna.DAO;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vacuna.vacuna.model.Paciente;


@Repository
public interface pacienteDAO extends MongoRepository<Paciente, String> {
	/*
	 * Clase PacienteDAO
	 * Contiene todo lo relativo a la base de datos de los paciente
	 */
	Paciente findByDni(String dni);
	
	Paciente deleteByDni(String dni);
	
	public List<Paciente> findAll();

	Paciente findByEmail(String email);
	
	Paciente findByEmailAndPassword(String email, String sha512Hex);
}
