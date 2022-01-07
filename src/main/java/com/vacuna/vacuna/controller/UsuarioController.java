package com.vacuna.vacuna.controller;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vacuna.vacuna.DAO.CentroSanitarioDAO;
import com.vacuna.vacuna.DAO.CitaDAO;
import com.vacuna.vacuna.DAO.UsuarioDAO;
import com.vacuna.vacuna.exception.CentrosNoEncontradosException;
import com.vacuna.vacuna.exception.DatosIncompletosException;
import com.vacuna.vacuna.exception.DniNoValidoException;
import com.vacuna.vacuna.exception.EmailExistenteException;
import com.vacuna.vacuna.exception.EmailIncorrectoException;
import com.vacuna.vacuna.exception.PasswordIncorrectaException;
import com.vacuna.vacuna.exception.PasswordNoCoincideException;
import com.vacuna.vacuna.exception.UsuarioExistenteException;
import com.vacuna.vacuna.exception.UsuarioNoEliminadoException;
import com.vacuna.vacuna.exception.UsuarioNoExisteException;
import com.vacuna.vacuna.exception.UsuariosNoEncontradosException;
import com.vacuna.vacuna.model.Administrador;
import com.vacuna.vacuna.model.CentroSanitario;
import com.vacuna.vacuna.model.Cita;
import com.vacuna.vacuna.model.Paciente;
import com.vacuna.vacuna.model.Sanitario;
import com.vacuna.vacuna.model.Usuario;


@RestController
@RequestMapping("Usuario")

@CrossOrigin("*")
public class UsuarioController {
 
	@Autowired
	private UsuarioDAO repository;
	  
	@Autowired
	private CitaDAO repositoryCita;
	
	@Autowired
	private CentroSanitarioDAO repositoryCentro;

	@Autowired
	private CentroSanitarioDAO csrepository;
	@PutMapping("/add")
	public void add(@RequestBody Map<String, Object> info) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		JSONObject jso = new JSONObject(info);
		String nombre = jso.optString("nombre");
		String email = jso.optString("email");
		String dni = jso.optString("dni"); 
		String tipoUsuario = jso.optString("tipoUsuario");
		String password = jso.optString("password"); 
		String password2 = jso.optString("password2");
		String centroAsignado = jso.optString("centroAsignado");
		String localidad = jso.optString("localidad");
		String provincia = jso.optString("provincia");
		
		if (!password.equals(password2))
			throw new PasswordNoCoincideException();

		byte[] pwd = md.digest(password.getBytes());
		
		Usuario u = new Usuario(nombre, email, pwd, dni, tipoUsuario, centroAsignado);
		
		if(!formValido(nombre, email, dni, centroAsignado, tipoUsuario, pwd.toString())) {
			throw new DatosIncompletosException();
		}
		validarDNI(dni); 
		validarEmail(email);
		validarPassword(password);
		
		if (repository.findByEmail(email) != null) {
			throw new UsuarioExistenteException();
		}
		
		switch(tipoUsuario) {
		case "Administrador":
			u = new Administrador(nombre, email, pwd, dni, tipoUsuario, centroAsignado);
			break;
		case "Paciente":
			if(!formValido(provincia, localidad)) {
				throw new DatosIncompletosException();
			}
			u = new Paciente(nombre, email, pwd, dni, tipoUsuario, centroAsignado, "0", localidad, provincia);
			break;
		case "Sanitario":
			u = new Sanitario(nombre, email, pwd, dni, tipoUsuario, centroAsignado);
			break;
		}
		repository.insert(u);

	} 
	
	@PutMapping("/actualizarDosis")
	public void actualizarDosis(@RequestBody Map<String, Object> info){
		JSONObject jso = new JSONObject(info);
		String dniPaciente = jso.optString("dniPaciente");
		String primeraDosis = jso.optString("primeraDosis");
		String segundaDosis = jso.optString("segundaDosis");
		Paciente u = (Paciente) repository.findByDni(dniPaciente);
		if(primeraDosis.equals("1")) {
			u.setDosisAdministradas("1");
		}
		if(segundaDosis.equals("1")) {
			u.setDosisAdministradas("2");
		}
		repository.save(u);
	}
	
	@GetMapping("/getNombrePaciente/{dni}")
	public String getNombrePaciente(@PathVariable String dni){
		Paciente u = (Paciente) repository.findByDni(dni);
		return u.getNombre();	
	}
	
	@GetMapping("/dosisMarcadas/{email}")
	public ArrayList<Integer> dosisMarcadas(@PathVariable String email){
		Usuario sanitario = repository.findByEmail(email);
		CentroSanitario cs = repositoryCentro.findByNombre(sanitario.getCentroAsignado());
		List<Cita> citas = repositoryCita.findAllByNombreCentro(cs.getNombre());
		ArrayList<Integer> dosis = new ArrayList<Integer>();
		for(int i= 0; i < citas.size(); i++) {	
			Paciente paciente = (Paciente) repository.findByDni(citas.get(i).getDniPaciente());
			Cita c =  repositoryCita.findByDniPaciente(paciente.getDni());
			long fechaPrimeraDosis = c.getFechaPrimeraDosis();
			long fechaSegundaDosis = c.getFechaSegundaDosis();
			
			Date d1 = new Date(fechaPrimeraDosis);
			Date d2 = new Date(fechaSegundaDosis);
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String fechaHOY = sdf.format(today);
			String fechaPD = sdf.format(d1);
			String fechaSD = sdf.format(d2);
			
		    if(fechaPD.equals(fechaHOY) || fechaSD.equals(fechaHOY) ) {
				dosis.add(Integer.parseInt(paciente.getDosisAdministradas()));
			}
			
		}
		System.out.println(dosis);
		return dosis;
	}
	
	@GetMapping("/getTodos")
	public List<Usuario> get() throws UsuariosNoEncontradosException {
		try {
			return repository.findAll();
		} catch (Exception e) {
			throw new UsuariosNoEncontradosException();
		}
	}

	@GetMapping("/getCentros")
	public List<CentroSanitario> devolverCentro() throws CentrosNoEncontradosException {
		try {
			return csrepository.findAll();

		} catch (Exception e) {
			throw new CentrosNoEncontradosException();
		}

	}

	@Transactional
	@DeleteMapping("/eliminarUsuario/{email}")
	//Eliminar citas
	public Usuario eliminarUsuario(@PathVariable String email) throws UsuarioNoExisteException, UsuarioNoEliminadoException {
		if (repository.findByEmail(email) == null) {
			throw new UsuarioNoExisteException();
		} 
		if (repository.findByEmail(email).getTipoUsuario().equals("Administrador")) {
			throw new UsuarioNoEliminadoException();
		}
		if (repository.findByEmail(email).getTipoUsuario().equals("Paciente")) {
			Paciente p = (Paciente) repository.findByEmail(email);
			Cita c = repositoryCita.findByDniPaciente(p.getDni());
			
			if(!p.getDosisAdministradas().equals("0")) {
				throw new UsuarioNoEliminadoException();
			}
			repositoryCita.deleteById(c.getId());
		}
		return repository.deleteByEmail(email);
	}

	@RequestMapping("/modificarUsuarios")
	public void update(@RequestBody Map<String, Object> info) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		JSONObject jso = new JSONObject(info);
		String nombre = jso.optString("nombre");
		String email = jso.optString("email");
		String password = jso.optString("password");
		String centroAsignado = jso.optString("centroAsignado");
		String localidad = jso.optString("localidad");
		String provincia = jso.optString("provincia");
		byte[] pwd = md.digest(password.getBytes());
		
		
		Usuario u =  repository.findByEmail(email);
		
		if(!formValido(nombre, email, centroAsignado, pwd.toString())) {
			throw new DatosIncompletosException();
		}
		if (u == null) {
			throw new UsuarioNoExisteException();
		}
		if(password.length() > 0)
			u.setPassword(pwd);
		
		u.setNombre(nombre);
		u.setCentroAsignado(centroAsignado); 
		
		if(u instanceof Paciente) {
			if(!formValido(provincia, localidad)) {
				throw new DatosIncompletosException();
			}
			Paciente p = (Paciente)u;
			p.setProvincia(provincia);
			p.setLocalidad(localidad);
		}
		repository.save(u);
		
	}
	
	@GetMapping("/buscarEmail/{email}")
	public Usuario readAll(@PathVariable String email) throws UsuarioNoExisteException {
		if (repository.findByEmail(email) == null) {
			throw new UsuarioNoExisteException();
		}
		return repository.findByEmail(email);
	}
	
	@GetMapping("/cogerTipoUsuario/{email}")
	public String cogerTipoUsuario(@PathVariable String email) {
		Usuario u =  repository.findByEmail(email);
		return u.getTipoUsuario();
		
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
	public boolean validarDNI(String dni) throws DniNoValidoException {
		boolean valido = true;
		if (dni.length() != 9) {
			throw new DniNoValidoException();
		}
		String entero = dni.substring(0, 8);

		if (!entero.matches("[0-9]+")) {
			throw new DniNoValidoException();
		}
		String letra = dni.substring(8, 9);
		if (letra.matches("[0-9]+")) {
			throw new DniNoValidoException();
		}
		return valido;
	}
	

	public boolean validarEmail(String email) throws EmailIncorrectoException, EmailExistenteException {
		boolean valido = true;
		String regex = "^(.+)@(.+)(.+).(.+)$";
		Pattern pattern = Pattern.compile(regex);
		if(!email.matches(regex)) {
			throw new EmailIncorrectoException();
			
		}else if (repository.findByEmail(email) != null) {
			throw new EmailExistenteException();
		}
		return valido;
	}
	
	public boolean validarPassword(String password) throws PasswordIncorrectaException {
		boolean valido = true;
		String regex = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
		Pattern pattern = Pattern.compile(regex);
		if(!password.matches(regex)) {
			throw new PasswordIncorrectaException(); 
			
		}
		return valido;
	}
	
}