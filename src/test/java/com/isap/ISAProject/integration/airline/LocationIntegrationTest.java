package com.isap.ISAProject.integration.airline;

import static com.isap.ISAProject.unit.constants.airline.LocationConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.service.airline.LocationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationIntegrationTest {
	@Autowired
	LocationService service;
	
	private static final String URL_PREFIX = "/destinations";

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
	public void testGet() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/" + 3L)).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(3));
	}
	
	@Test
	public void testGetPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + PAGE_SIZE)).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(1));
	}
	
	/*@Test
	public void testSave() throws Exception {
		Location room = new Location(4l, DB_VERSION, DB_NAME);

		String json = com.isap.ISAProject.TestUtil.json(room);
		this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isCreated());
	}*/
}
