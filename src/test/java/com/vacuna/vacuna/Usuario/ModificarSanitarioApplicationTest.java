package com.vacuna.vacuna.Usuario;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.vacuna.vacuna.VacunaApplication;
import com.vacuna.vacuna.DAO.CentroSanitarioDAO;
import com.vacuna.vacuna.DAO.UsuarioDAO;
import com.vacuna.vacuna.model.CentroSanitario;
import com.vacuna.vacuna.model.Sanitario;
import com.vacuna.vacuna.model.Usuario;

import net.minidev.json.JSONObject;

@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = VacunaApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ModificarSanitarioApplicationTest {
	@Autowired 
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext controller;
	@Autowired
	private CentroSanitarioDAO DAO;
	@Autowired
	private UsuarioDAO userDAO;
	private Sanitario s;
	
	
	private CentroSanitario centro;
	private String TEST_NOMBRE = "Cristina Sanitario";
	private String TEST_EMAIL = "pruebaAdministrador@gmail.com";
	private String TEST_PASSWORD = "Hola1236=";
	private String TEST_DNI = "05724787H";
	private String TEST_TIPOUSUARIO = "Sanitario";
	private String TEST_CENTROASIGNADO = "Centro Prueba";
	
	
	@BeforeEach
	public void setupMockMvc(){
		mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
		centro = new CentroSanitario(TEST_CENTROASIGNADO, 2000, 2, 8, 20, "Ciudad Real", "Ciudad Real");
		DAO.save(centro);
		s = new Sanitario(TEST_NOMBRE, TEST_EMAIL,TEST_PASSWORD.getBytes(), TEST_DNI, TEST_TIPOUSUARIO, TEST_CENTROASIGNADO);
		userDAO.save(s);
		
	}
	@Test
	@Order(1)
	public void modificarSanitarioCorrecto() throws Exception {
		JSONObject json = new JSONObject();
		json.put("nombre", "Cris Sanitario");
		json.put("email", TEST_EMAIL);
		json.put("password", TEST_PASSWORD);
		json.put("dni", TEST_DNI);
		json.put("tipoUsuario", TEST_TIPOUSUARIO);
		json.put("centroAsignado", TEST_CENTROASIGNADO);
		System.out.println(json.toJSONString());

		
		final ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.put("/Usuario/modificarUsuarios")
				.content(json.toString())
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(200));
		
	}
	@Test 
	public void expectedErrorSanitarioInexisistente() throws Exception {
		JSONObject json = new JSONObject();
		json.put("nombre", TEST_NOMBRE);
		json.put("email", "pruebaSanitario@sigeva.com");
		json.put("password", TEST_PASSWORD);
		json.put("dni", TEST_DNI);
		json.put("tipoUsuario", TEST_TIPOUSUARIO);
		json.put("centroAsignado", TEST_CENTROASIGNADO);
		System.out.println(json.toJSONString());

		
		final ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.put("/Usuario/modificarUsuarios")
				.content(json.toString())
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(409));
	}
	@Test
	public void expectedErrorDatosIncompletos() throws Exception {
		JSONObject json = new JSONObject();
		json.put("nombre", TEST_NOMBRE);
		json.put("email", "");
		json.put("password", TEST_PASSWORD);
		json.put("dni", TEST_DNI);
		json.put("tipoUsuario", TEST_TIPOUSUARIO);
		json.put("centroAsignado", "");
		System.out.println(json.toJSONString());

		
		final ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.put("/Usuario/modificarUsuarios")
				.content(json.toString())
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(409));
				
	}
	@AfterAll
	public void deleteAll() {
		Usuario u = userDAO.findByEmail(TEST_EMAIL);
		if(u!=null)
			userDAO.delete(u);
		DAO.delete(centro);
	}
}
