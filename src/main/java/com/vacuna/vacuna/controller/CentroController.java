package com.vacuna.vacuna.controller;

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
import com.vacuna.vacuna.DAO.UsuarioDAO;
import com.vacuna.vacuna.exception.CentroNoEliminadoException;
import com.vacuna.vacuna.exception.CentroNoExisteException;
import com.vacuna.vacuna.exception.CentrosNoEncontradosException;
import com.vacuna.vacuna.exception.ControlHorasVacunacionException;
import com.vacuna.vacuna.exception.DatosIncompletosException;
import com.vacuna.vacuna.model.CentroSanitario;
import com.vacuna.vacuna.model.Usuario;

@RestController
@RequestMapping("centro")
public class CentroController {

	@Autowired
	private CentroSanitarioDAO repository;

	
	@Autowired
	private UsuarioDAO repositoryUsuario; 

	@PutMapping("/add")
	public CentroSanitario add(@RequestBody Map<String, Object> info) throws Exception {
		JSONObject jso = new JSONObject(info);
		String nombre = jso.optString("nombre");
		String dosisT = jso.optString("dosisTotales");
		String localidad = jso.optString("localidad"); 
		String provincia = jso.optString("provincia");
		if(!formValido(nombre, dosisT, localidad, provincia)) {
			throw new DatosIncompletosException();
		}
		int dosisTotales = Integer.parseInt(dosisT);
		int aforo = 0;  

		int hInicio = 0;
		int hFin = 0;
		List<CentroSanitario> listCentro = repository.findAll();
		if(listCentro.size()>0) {
			 hInicio = listCentro.get(0).getHoraInicio();
			 hFin = listCentro.get(0).getHoraFin();
			 aforo = listCentro.get(0).getAforo();
		}else {
			 String af = jso.optString("aforo");
			 String horaInicio = jso.optString("horaInicio");
			 String horaFin = jso.optString("horaFin");
			 if(!formValido(af, horaInicio, horaFin, provincia)) {
				 throw new DatosIncompletosException();
				} 
			 aforo = Integer.parseInt(af);
			 hInicio = Integer.parseInt(horaInicio);
			 hFin = Integer.parseInt(horaFin);
			 if(hInicio>=hFin) {
				 throw new ControlHorasVacunacionException();
			 }
		}
		CentroSanitario c = new CentroSanitario(nombre, dosisTotales, aforo,hInicio,hFin, localidad, provincia);
		  
		return repository.insert(c);

	}
	
	public boolean formValido(String... values) {
		boolean valid = values.length > 0;
		for(String value : values) {
			if(value.length() == 0) {
				valid = false;
				break;
			}
		}
		return valid;
	}

	@GetMapping("/getTodos")
	public List<CentroSanitario> get() throws CentrosNoEncontradosException {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new CentrosNoEncontradosException();
		}
	}
	
	@GetMapping("/comprobarCentros")
	public String comprobarCentros() {
			 if(repository.findAll().size() <= 0 ) {
				 return "1";	 
			 }
		return "0";
	}

	@Transactional
	@PutMapping("/modificarCentro/{id}")
	public CentroSanitario modificarCentro(@PathVariable String id, 
			@RequestBody Map<String, Object> info)
			throws Exception {
		Optional<CentroSanitario> optCentroSanitario = repository.findById(id);

		JSONObject jso = new JSONObject(info);
		String nombre = jso.optString("nombre");
		String dosisT = jso.optString("dosisTotales");
		String af = jso.optString("aforo");
		String horaIncicio = jso.optString("horaInicio");
		String horaFin = jso.optString("horaFin");
		String localidad = jso.optString("localidad");
		String provincia = jso.optString("provincia");
		
		if(!formValido(nombre, dosisT, af, horaIncicio, horaFin, localidad, provincia)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,"Rellena todos los datos");
		}
		
		int dosisTotales = Integer.parseInt(dosisT);
		int aforo = Integer.parseInt(af);
		int hInicio = Integer.parseInt(horaIncicio);
		int hFin = Integer.parseInt(horaFin);
		if(hInicio>=hFin) {
			 throw new ControlHorasVacunacionException();
		 }
		
		CentroSanitario centro = new CentroSanitario();
		if (optCentroSanitario.isPresent()) {
			centro = optCentroSanitario.get();
		}
		List<CentroSanitario> listCentro = repository.findAll();
		for(int i=0;i<listCentro.size();i++) {
			CentroSanitario cs = listCentro.get(i);
			listCentro.get(i).setAforo(aforo);
			listCentro.get(i).setHoraInicio(hInicio);
			listCentro.get(i).setHoraFin(hFin);
			repository.save(cs);
		}

		centro.setNombre(nombre);
		centro.setAforo(aforo);
		centro.setHoraInicio(hInicio);
		centro.setHoraFin(hFin);
		centro.setLocalidad(localidad);
		centro.setProvincia(provincia);
		centro.setDosisTotales(dosisTotales);

		return repository.save(centro);
	}

	
	
	@Transactional
	@DeleteMapping("/eliminarCentro/{id}") 
	public void eliminarCentro(@PathVariable String id) throws CentroNoExisteException, CentroNoEliminadoException {
		Optional<CentroSanitario> cs = repository.findById(id);
		if (cs.isPresent()) {
			List<Usuario> listaUsuarios=repositoryUsuario.findAllByCentroAsignado(cs.get().getNombre());
			if(listaUsuarios.size() > 0) {
					throw new CentroNoEliminadoException();
				}
			repository.deleteById(id);
		}else {
			throw new CentroNoExisteException();
		}
		
	}
	

}