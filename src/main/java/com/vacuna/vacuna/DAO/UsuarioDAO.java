package com.vacuna.vacuna.DAO;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vacuna.vacuna.model.CentroSanitario;
import com.vacuna.vacuna.model.Cita;
import com.vacuna.vacuna.model.Usuario;

@Repository
public interface UsuarioDAO extends MongoRepository<Usuario, String> {
	/*
	 * Clase UsuarioDAO
	 * Contiene todo lo relativo a la base de datos de los usuarios
	 */
	Usuario findByDni(String dni);
	Usuario findByEmail(String email);
	
	Usuario deleteByDni(String dni);
	Usuario deleteByEmail(String email);
	Usuario findByEmailAndPassword(String email, byte[] pwd);
	
	List<Usuario> findAllByTipoUsuario(String tipoUsuario);
	List<Usuario> findAllByCentroAsignado(String centroAsignado);
	//public List<Usuario> findAll();
	CentroSanitario findByCentroAsignado(String email);
}
