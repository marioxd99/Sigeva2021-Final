package com.vacuna.vacuna.Centros;

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
import com.vacuna.vacuna.model.CentroSanitario;

import net.minidev.json.JSONObject;

@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = VacunaApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)

public class AddCentrosApplicationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext controller;

	@Autowired
	private CentroSanitarioDAO DAO;
	
	private CentroSanitario centro;
	
	private String TEST_NOMBRE = "Centro Prueba";
	private int TEST_DOSIS = 2000;
	private int TEST_AFORO = 2;
	private int TEST_HINICIO = 8;
	private int TEST_HFIN = 20;
	private String TEST_LOCALIDAD = "Ciudad Real";
	private String TEST_PROVINCIA = "Ciudad Real";
	@BeforeEach
	public void setupMockMvc(){
	    mockMvc = MockMvcBuilders.webAppContextSetup(controller).build();
	    centro = new CentroSanitario(TEST_NOMBRE, TEST_DOSIS, TEST_AFORO, TEST_HINICIO, TEST_HFIN, TEST_LOCALIDAD, TEST_PROVINCIA);
		DAO.save(centro);
	}
	
	@Test 
	@Order(1)
	public void saveCentroCorrecto() throws Exception {
		JSONObject json = new JSONObject();
		json.put("nombre", TEST_NOMBRE);
		json.put("dosisTotales", TEST_DOSIS);
		json.put("aforo", TEST_AFORO);
		json.put("horaInicio", TEST_HINICIO);
		json.put("horaFin", TEST_HFIN);
		json.put("localidad", TEST_LOCALIDAD);
		json.put("provincia", TEST_PROVINCIA);
		System.out.println(json.toJSONString());
		final ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.put("/centro/add")
				.content(json.toString())
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(200));

		
	}
	@Test
	public void expectedErrorDatosIncompletos() throws Exception {
		JSONObject json = new JSONObject();
		json.put("nombre", TEST_NOMBRE);
		json.put("dosisTotales", TEST_DOSIS);
		json.put("aforo", TEST_AFORO);
		json.put("horaInicio", TEST_HINICIO);
		json.put("horaFin", TEST_HFIN);
		json.put("localidad", "");
		json.put("provincia", "");
		System.out.println(json.toJSONString());
		final ResultActions resultado = mockMvc.perform(MockMvcRequestBuilders.put("/centro/add")
				.content(json.toString())
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(409));
	}
	
	@AfterAll
	public void deleteAll() {

		DAO.delete(centro);
	}
}
