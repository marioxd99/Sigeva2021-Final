package com.vacuna.vacuna.Centros;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.vacuna.vacuna.VacunaApplication;
import com.vacuna.vacuna.DAO.CentroSanitarioDAO;
import com.vacuna.vacuna.model.CentroSanitario;

@SpringBootTest(classes = VacunaApplication.class)
@AutoConfigureMockMvc()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ModificarCentrosApplicationTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private CentroSanitarioDAO DAO;
	private CentroSanitario centro;

	
	@Autowired
	private WebApplicationContext controller;
	@BeforeEach
	public void setupMockMvc(){
	    mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
	}
	
	 
	@BeforeAll
	public void setUpTest() {
		centro = new CentroSanitario("El Carmen", 0, 300, 8, 20, "Ciudad Real", "Ciudad Real");
		DAO.save(centro);
	}
	
	@Test 
	@Order(1)
	public void modificarCentroCorrecto() throws Exception {
		centro.setNombre("El Carmen");
		centro.setAforo(0); 
		centro.setHoraInicio(8);
		centro.setHoraFin(20);
		centro.setLocalidad("CR");
		centro.setProvincia("CR");
		centro.setDosisTotales(300);
		
		JSONObject json = new JSONObject();
		json.put("id", centro.getId());
		json.put("nombre", centro.getNombre());
		json.put("dosisTotales", centro.getDosisTotales());
		json.put("aforo", centro.getAforo());
		json.put("horaInicio", centro.getHoraInicio());
		json.put("horaFin", centro.getHoraFin());
		json.put("localidad", centro.getLocalidad());
		json.put("provincia", centro.getProvincia());
	
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/centro/modificarCentro/"+centro.getId()).content(json.toString()).contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)).andDo(MockMvcResultHandlers.print()).andReturn();
		assertEquals(200, result.getResponse().getStatus());
	}
	
	@Test 
	@Order(2)
	public void expectedErrorDatosIncompletos() throws Exception {
		JSONObject json = new JSONObject();
		json.put("id", centro.getId());
		json.put("nombre", "");
		json.put("dosisTotales", centro.getDosisTotales());
		json.put("aforo", centro.getAforo());
		json.put("horaInicio", centro.getHoraInicio());
		json.put("horaFin", centro.getHoraFin());
		json.put("localidad", centro.getLocalidad());
		json.put("provincia", centro.getProvincia());
	
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/centro/modificarCentro/"+centro.getId()).content(json.toString()).contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8)).andDo(MockMvcResultHandlers.print()).andReturn();
		assertEquals(409, result.getResponse().getStatus());
	}
	
	@AfterAll
	public void finishTest() {
		DAO.delete(centro);
	}
}
