package com.isap.ISAProject.integration.airline;

import java.nio.charset.Charset;
import java.util.List;

import static com.isap.ISAProject.unit.constants.airline.AirlineConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.service.airline.AirlineService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AirlineIntegrationTest {
	
	@Autowired
	AirlineService service;
	
	private static final String URL_PREFIX = "/airlines";
	
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
	public void testGetAll() {
		PageRequest pageRequest = new PageRequest(0, PAGE_SIZE);
		List<Airline> rentacars = service.findAll(pageRequest);
		assertThat(rentacars).isNotEmpty();
	}
	
	@Test
	public void testGetRentacarsPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + PAGE_SIZE)).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(1))
		.andExpect(jsonPath("$[0].name").value("Dickinson-Weissnat"))
		.andExpect(jsonPath("$[0].address").value("9 Springs Plaza"))
		.andExpect(jsonPath("$[0].description").value("Self-enabling uniform Graphic Interface"));
	}
	
	@Test
	public void testGet() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/" + 3L)).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(3))
		.andExpect(jsonPath("$.name").value("Jast-Bashirian"));
	}
	
	/*@Test
	public void testSave() throws Exception {
		Airline air = new Airline(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION);

		String json = com.isap.ISAProject.TestUtil.json(air);
		this.mockMvc.perform(post(URL_PREFIX + "?destination=" + LOCATION_ID).contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	public void testUpdate() throws Exception {
		Airline car = new Airline(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION);

		String json = com.isap.ISAProject.TestUtil.json(car);
		this.mockMvc.perform(put(URL_PREFIX + "/" + DB_ID).accept(contentType).contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteStudent() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/" + NEW_ID)).andExpect(status().isOk());
	}*/
}
