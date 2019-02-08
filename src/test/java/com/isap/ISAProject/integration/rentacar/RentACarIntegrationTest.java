package com.isap.ISAProject.integration.rentacar;

import static com.isap.ISAProject.unit.constants.RentACarConstants.DB_ID;
import static com.isap.ISAProject.unit.constants.RentACarConstants.NEW_ID;
import static com.isap.ISAProject.unit.constants.RentACarConstants.NEW_ADDRESS;
import static com.isap.ISAProject.unit.constants.RentACarConstants.NEW_DESCRIPTION;
import static com.isap.ISAProject.unit.constants.RentACarConstants.NEW_NAME;
import static com.isap.ISAProject.unit.constants.RentACarConstants.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.service.rentacar.RentACarService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentACarIntegrationTest {
	
	@Autowired
	RentACarService service;
	
	private static final String URL_PREFIX = "/rent-a-cars";

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void testGetAllRentacars() {
		PageRequest pageRequest = new PageRequest(0, PAGE_SIZE);
		List<RentACar> rentacars = service.getAllRentACars(pageRequest);
		assertThat(rentacars).isNotEmpty();
	}
	
	@Test
	public void testGetRentacarsPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + PAGE_SIZE)).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(1))
		.andExpect(jsonPath("$[0].name").value("Reichel-Braun"))
		.andExpect(jsonPath("$[0].address").value("52337 Logan Point"))
		.andExpect(jsonPath("$[0].description").value("Automated bifurcated matrices"));
	}
	
	@Test
	public void testGetRentacar() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/" + 3L)).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(3))
		.andExpect(jsonPath("$.name").value("Corwin, Dietrich and Gerlach"));
	}
	
	@Test
	public void testSaveRentacar() throws Exception {
		RentACar car = new RentACar(DB_ID, NEW_NAME, NEW_ADDRESS, NEW_DESCRIPTION);

		String json = com.isap.ISAProject.TestUtil.json(car);
		this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	public void testUpdateRentacar() throws Exception {
		RentACar car = new RentACar(DB_ID, NEW_NAME, NEW_ADDRESS, NEW_DESCRIPTION);

		String json = com.isap.ISAProject.TestUtil.json(car);
		this.mockMvc.perform(put(URL_PREFIX + "/" + DB_ID).accept(contentType).contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteStudent() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/" + NEW_ID)).andExpect(status().isOk());
	}
}
