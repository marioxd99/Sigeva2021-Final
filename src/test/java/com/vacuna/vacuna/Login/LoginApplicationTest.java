package com.vacuna.vacuna.Login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.vacuna.vacuna.VacunaApplication;
import com.vacuna.vacuna.DAO.UsuarioDAO;
import com.vacuna.vacuna.model.Administrador;
import com.vacuna.vacuna.model.CentroSanitario;
import com.vacuna.vacuna.model.Usuario;

import net.minidev.json.JSONObject;

@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = VacunaApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LoginApplicationTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext controller;

	@Autowired
	private UsuarioDAO userDAO;
	private Administrador a;
	
	private String TEST_NOMBRE = "Cristina Administrador";
	private String TEST_EMAIL = "pruebaAdministrador@gmail.com";
	private String TEST_PASSWORD = "Hola1236=";
	private String TEST_DNI = "05724787H";
	private String TEST_TIPOUSUARIO = "Administrador";
	private String TEST_CENTROASIGNADO = "Centro Prueba";
	
	@BeforeEach
	public void setupMockMvc() throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
		a = new Administrador(TEST_NOMBRE, TEST_EMAIL,md.digest(TEST_PASSWORD.getBytes()), TEST_DNI, TEST_TIPOUSUARIO, TEST_CENTROASIGNADO);
		userDAO.save(a);
	}
	@Test 
	public void LoginCorrecto() throws Exception {
		JSONObject json = new JSONObject();
		json.put("email", TEST_EMAIL);
		json.put("password", TEST_PASSWORD);
		System.out.println(json.toJSONString());		
		final ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/login/login")
				.content(json.toString())
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(200));
	}
	
	@Test
	public void expectedErrorCredencialesInvalidas() throws Exception {
		JSONObject json = new JSONObject();
		json.put("email", TEST_EMAIL);
		json.put("password", "Hola");
		System.out.println(json.toJSONString());		
		final ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.post("/login/login")
				.content(json.toString())
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(409));
	}
	
	@AfterAll
	public void deleteAll() {
		Usuario u = userDAO.findByEmail(TEST_EMAIL);
		if(u!=null)
			userDAO.delete(u);
	}
}
