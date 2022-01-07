
package com.vacuna.vacuna.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vacuna.vacuna.DAO.CentroSanitarioDAO;
import com.vacuna.vacuna.DAO.CitaDAO;
import com.vacuna.vacuna.DAO.UsuarioDAO;
import com.vacuna.vacuna.exception.CentrosNoEncontradosException;
import com.vacuna.vacuna.exception.CitasNoEncontradasException;
import com.vacuna.vacuna.exception.DiasEntreDosisIncorrectosException;
import com.vacuna.vacuna.exception.ErrorDosisAdministradasException;
import com.vacuna.vacuna.exception.NoHayDosisException;
import com.vacuna.vacuna.exception.SlotVacunacionSuperadoException;
import com.vacuna.vacuna.model.CentroSanitario;
import com.vacuna.vacuna.model.Cita;
import com.vacuna.vacuna.model.Paciente;
import com.vacuna.vacuna.model.Usuario;

@RestController
@RequestMapping("cita")
public class CitaController {
	/*
	 * Clase CitaController
	 * Encargada de todo lo que tiene que ver con las citas
	 * Contiene los métodos de add, getTodos, getCentroSanitario, eliminarCita, getCitaPaciente, modificarCita y crearCita
	 */
	CentroSanitario centroSanitario;


	@Autowired
	private CitaDAO repositoryCita;


	@Autowired
	private CentroSanitarioDAO repositoryCentro;


	@Autowired
	private UsuarioDAO repositoryUsuario;


	@PutMapping("/add")
	public Cita add(@RequestBody Map<String, Object> info) throws Exception {
		JSONObject jso = new JSONObject(info);
		String dniUsuario = jso.optString("dni");

		Usuario usuario = repositoryUsuario.findByDni(dniUsuario);
		String nombre = usuario.getNombre();
		String nombreCentro = jso.optString("centroAsignado"); 
		if ((Integer.parseInt(((Paciente) usuario).getDosisAdministradas()) < 2)) {
			Cita c = crearCita(usuario.getCentroAsignado(), dniUsuario,nombre);
			return c;
		} else {
			throw new ErrorDosisAdministradasException();
		}
	}


	@GetMapping("/getTodos")
	public List<Cita> get() throws CitasNoEncontradasException {
		try {
			return repositoryCita.findAll();
		} catch (Exception e) {
			throw new CitasNoEncontradasException();
		}
	}


	@GetMapping("/getCentroSanitario/{email}")
	public List<Cita> getCentroSanitario(@PathVariable String email) throws CentrosNoEncontradosException {
		Usuario sanitario = repositoryUsuario.findByEmail(email);

		CentroSanitario cs = repositoryCentro.findByNombre(sanitario.getCentroAsignado());
		try {
			return repositoryCita.findAllByNombreCentro(cs.getNombre());
		} catch (Exception e) {
			throw new CentrosNoEncontradosException();
		}
	}

	@Transactional
	@DeleteMapping("/eliminarCitaCompleta/{id}")
	public Cita eliminarCitaCompleta(@PathVariable String id){
		Optional<Cita> c = repositoryCita.findById(id);
		Cita citaAux = new Cita();
		if (c.isPresent()) {
			citaAux = c.get();
		}
		Paciente p = (Paciente) repositoryUsuario.findByDni(c.get().getDniPaciente());
		p.setDosisAdministradas("0");
		repositoryUsuario.save(p);
		String nombreCentro = citaAux.getNombreCentro();
		CentroSanitario cs = repositoryCentro.findByNombre(nombreCentro);
		cs.setDosisTotales(cs.getDosisTotales() + 1);
		repositoryCentro.save(cs);
		repositoryCita.deleteById(id);
		return null;
	}

	@Transactional
	@PutMapping("/eliminarCita/{id}")
	public Cita eliminarCita(@PathVariable String id, @RequestBody Map<String, Object> info) throws Exception{

		Optional<Cita> c = repositoryCita.findById(id);
		Cita cita = new Cita();

		if (c.isPresent()) {
			cita = c.get();
		}


		JSONObject jso = new JSONObject(info);
		String dniPaciente = jso.optString("dniPaciente");
		String nombreCentro = jso.optString("centrosSanitarios");
		String fechaPrimeraMod = jso.getString("fechaPrimeraDosis");

		CentroSanitario cs = repositoryCentro.findByNombre(nombreCentro);
		cs.setDosisTotales(cs.getDosisTotales() + 1);
		repositoryCentro.save(cs);

		DateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date citaMod = fechaHora.parse(fechaPrimeraMod);

		cita.setFechaPrimeraDosis(citaMod.getTime());

		cita.setFechaSegundaDosis(0);

		return repositoryCita.save(cita);
	}


	@GetMapping("/getCitaPaciente/{dni}")
	public Cita getCitaPaciente(@PathVariable String dni) {
		Cita c = repositoryCita.findByDniPaciente(dni);

		if (c != null) {
			return c;
		}
		return null;
	}


	@GetMapping("/")
	public List<Cita> readAll() {
		return repositoryCita.findAll();
	}


	@Transactional
	@PutMapping("/modificarCita/{id}")
	public Cita modificarCita(@PathVariable String id, @RequestBody Map<String, Object> info) throws Exception {
		Optional<Cita> c = repositoryCita.findById(id);
		Cita cita = new Cita();
		if (c.isPresent()) {
			cita = c.get();
		}

		JSONObject jso = new JSONObject(info);
		String dniPaciente = jso.optString("dniPaciente");

		String nombreCentro = jso.optString("centrosSanitarios");
		String fechaPrimeraMod = jso.getString("fechaPrimeraDosis");
		String fechaSegundaMod = jso.getString("fechaSegundaDosis");
		Paciente usuario = (Paciente) repositoryUsuario.findByDni(c.get().getDniPaciente());

		Date today = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date dataFormateada = formato.parse(fechaPrimeraMod); 
		Date dataFormateada2 = formato.parse(fechaSegundaMod); 

		long fechaHOY = new Date(today.getYear(), today.getMonth(), today.getDate(), 0, 0).getTime(); 
		long fechaPrimeraDosisMS = new Date(dataFormateada.getYear(), dataFormateada.getMonth(), dataFormateada.getDate(), 0, 0).getTime();
		long fechaSegundaDosisMS = new Date(dataFormateada2.getYear(), dataFormateada2.getMonth(), dataFormateada2.getDate(), 0, 0).getTime();

		long fechaLimitePD = 1641772800000L;//10 de enero de 2022
		long fechaLimiteSD = 1643587200000L;//31 de enero de 2022
		if(fechaPrimeraDosisMS<=fechaHOY || fechaSegundaDosisMS<=fechaHOY ) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,"No puedes viajar en el tiempo");
		}
		if(fechaLimitePD<=fechaPrimeraDosisMS || fechaLimiteSD<=fechaSegundaDosisMS ) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,"No puedes superar las fechas limites");
		}

		String nombre = usuario.getNombre();
		Cita citaModificada = modificar(dniPaciente, nombreCentro, fechaPrimeraMod, fechaSegundaMod,nombre);
		cita.setFechaPrimeraDosis(citaModificada.getFechaPrimeraDosis());
		cita.setFechaSegundaDosis(citaModificada.getFechaSegundaDosis());
		return repositoryCita.save(cita);
	}


	private Cita modificar(String dniPaciente, String nombreCentro, String fechaPrimeraMod,
			String fechaSegundaMod,String nombre) throws Exception {
		List<Cita> listaCitas = repositoryCita.findAll();
		List<CentroSanitario> listaCentros = repositoryCentro.findAll();
		Cita c = new Cita();

		for (int i = 0; i < listaCentros.size(); i++) {
			if (listaCentros.get(i).getNombre().equals(nombreCentro)) {
				centroSanitario = listaCentros.get(i);
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date fecha1 = sdf.parse(fechaPrimeraMod);
		Date fecha2 = sdf.parse(fechaSegundaMod);

		if ((fecha2.getTime()-fecha1.getTime()) < (1000 * 60 * 60 * 24 * 21)) {
			throw new DiasEntreDosisIncorrectosException();
		}

		//Continuar luego

		Date today = new Date();
		Date tomorrow = fecha1;

		long aux1 = new Date(fecha1.getYear(), fecha1.getMonth(), fecha1.getDate(), centroSanitario.getHoraInicio(), 0).getTime(); 
		long aux2 = new Date(fecha2.getYear(), fecha2.getMonth(), fecha2.getDate(), centroSanitario.getHoraInicio(), 0).getTime();

		long fechaActual = new Date(tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDate(), centroSanitario.getHoraInicio(), 0).getTime();
		//long fechaSegundaModificada = new Date(fecha2.getYear(), fecha2.getMonth(), fecha2.getDate(), centroSanitario.getHoraInicio(), 0).getTime();
		long nocheVieja = 1643587200000L; //Dia 31 de Enero
		int contadorAforo = 0; // Aforo para el centro que cogemos
		boolean asignada = true;
		if (listaCitas.isEmpty()) {
			if (centroSanitario.getDosisTotales() >= 2) {
				++contadorAforo;
				c = new Cita();
				c.setNombreCentro(centroSanitario.getNombre());
				c.setFechaPrimeraDosis(aux1);
				//c.setFechaSegundaDosis(fecha1.getTime() + (1000 * 60 * 60 * 24 * 21));

				c.setFechaSegundaDosis(aux2);
				c.setDniPaciente(dniPaciente);
				c.setNombrePaciente(nombre);
				centroSanitario.setDosisTotales(centroSanitario.getDosisTotales() - 2);
				repositoryCentro.save(centroSanitario);
				return c;
			} else {
				throw new NoHayDosisException();
			}
		} else {
			asignada = false;
			while (!asignada) {
				for (int i = 0; i < listaCitas.size(); i++) { //Esto ahora mismo no hace nada
					Cita cita = listaCitas.get(i);
					if ((fecha1.getTime() == fechaActual) || (fecha2.getTime() == fechaActual)) {
						++contadorAforo;
					}
				}
				if (contadorAforo >= centroSanitario.getAforo()) {
					if (new Date(fechaActual).getHours() == centroSanitario.getHoraFin()) {
						fechaActual += (3600000 * 12); // Proximo dia a las 08.00am
					} else {
						fechaActual += 3600000; // Siguiente rango de horas
					}
					if ((aux1 >= nocheVieja) || (aux2 >= nocheVieja)) {
						throw new SlotVacunacionSuperadoException();
					}
					contadorAforo = 0;
				} else {
					++contadorAforo;
					c = new Cita();
					c.setNombreCentro(centroSanitario.getNombre());
					c.setFechaPrimeraDosis(aux1);
					c.setFechaSegundaDosis(aux2);
					c.setDniPaciente(dniPaciente);
					c.setNombrePaciente(nombre);
					centroSanitario.setDosisTotales(centroSanitario.getDosisTotales() - 2);
					repositoryCentro.save(centroSanitario);
					asignada = true;
					return c;

				}
			}
		}

		return c;
	}


	public Cita crearCita(String nombreCentro, String dniUsuario,String nombre) throws Exception { // Cambiar slot de vacunacion al 10 de Enero

		List<Cita> listaCitas = repositoryCita.findAll();
		List<CentroSanitario> listaCentros = repositoryCentro.findAll();
		
		for (int i = 0; i < listaCentros.size(); i++) {
			if (listaCentros.get(i).getNombre().equals(nombreCentro)) {
				centroSanitario = listaCentros.get(i);
			}
		}

		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

		long fechaActual = new Date(tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDate(), centroSanitario.getHoraInicio(), 0).getTime();
		long nocheVieja = 1643587200000L; //Dia 31 de Enero
		int contadorAforo = 0; // Aforo para el centro que cogemos
		boolean asignada = true;

		Cita c = new Cita();

		if(repositoryCita.findByDniPaciente(dniUsuario) != null) {
			c = repositoryCita.findByDniPaciente(dniUsuario);
			asignada = false;
			while (!asignada) {
				for (int i = 0; i < listaCitas.size(); i++) {
					Cita cita = listaCitas.get(i);
					if ((cita.getFechaSegundaDosis() == fechaActual)) {
						++contadorAforo;
					}
				}
				if (contadorAforo >= centroSanitario.getAforo()) {
					if (new Date(fechaActual).getHours() == centroSanitario.getHoraFin()) {
						fechaActual += (3600000 * 12); // Proximo dia a las 08.00am
					} else {
						fechaActual += 3600000; // Siguiente rango de horas
					}
					if ((fechaActual >= nocheVieja) || (fechaActual + (1000 * 60 * 60 * 24 * 21) >= nocheVieja)) {
						throw new SlotVacunacionSuperadoException();
					}
					contadorAforo = 0;
				} else {
					++contadorAforo;
					c.setFechaSegundaDosis(c.getFechaPrimeraDosis() + (1000 * 60 * 60 * 24 * 21));
					centroSanitario.setDosisTotales(centroSanitario.getDosisTotales() - 1);
					repositoryCentro.save(centroSanitario);
					asignada = true;
					return repositoryCita.save(c);
				}
			}
		}else {
			if (listaCitas.isEmpty()) {
				if (centroSanitario.getDosisTotales() >= 2) {
					++contadorAforo;
					c = new Cita();
					c.setNombreCentro(centroSanitario.getNombre());
					c.setFechaPrimeraDosis(fechaActual);
					c.setFechaSegundaDosis(fechaActual + (1000 * 60 * 60 * 24 * 21));
					c.setDniPaciente(dniUsuario);
					c.setNombrePaciente(nombre);
					repositoryCita.insert(c);
					centroSanitario.setDosisTotales(centroSanitario.getDosisTotales() - 2);
					repositoryCentro.save(centroSanitario);
					return c;
				} else {
					throw new NoHayDosisException();
				}
			} else {
				asignada = false;
				while (!asignada) {
					for (int i = 0; i < listaCitas.size(); i++) {
						Cita cita = listaCitas.get(i);
						if ((cita.getFechaPrimeraDosis() == fechaActual) || (cita.getFechaSegundaDosis() == fechaActual)) {
							++contadorAforo;
						}
					}
					if (contadorAforo >= centroSanitario.getAforo()) {
						if (new Date(fechaActual).getHours() == centroSanitario.getHoraFin()) {
							fechaActual += (3600000 * 12); // Proximo dia a las 08.00am
						} else {
							fechaActual += 3600000; // Siguiente rango de horas
						}
						if ((fechaActual >= nocheVieja) || (fechaActual + (1000 * 60 * 60 * 24 * 21) >= nocheVieja)) {
							throw new SlotVacunacionSuperadoException();
						}
						contadorAforo = 0;
					} else {
						++contadorAforo;
						c.setNombreCentro(centroSanitario.getNombre());
						c.setFechaPrimeraDosis(fechaActual);
						c.setFechaSegundaDosis(fechaActual + (1000 * 60 * 60 * 24 * 21));
						c.setDniPaciente(dniUsuario);
						c.setNombrePaciente(nombre);
						repositoryCita.insert(c);
						centroSanitario.setDosisTotales(centroSanitario.getDosisTotales() - 2);
						repositoryCentro.save(centroSanitario);
						asignada = true;
						return c;
					}
				}
			}
		}
		return c;
	}
}