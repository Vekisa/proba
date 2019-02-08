package com.isap.ISAProject.integration.hotel;

import static com.isap.ISAProject.unit.constants.hotel.RoomConstants.DB_ID;
import static com.isap.ISAProject.unit.constants.hotel.RoomConstants.DB_NUMBER_OF_BEDS;
import static com.isap.ISAProject.unit.constants.hotel.RoomConstants.DB_VERSION;
import static com.isap.ISAProject.unit.constants.hotel.RoomConstants.NEW_ID;
import static com.isap.ISAProject.unit.constants.hotel.RoomConstants.PAGE_SIZE;
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

import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.service.hotel.RoomService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomIntegrationTest {
	@Autowired
	RoomService service;
	
	private static final String URL_PREFIX = "/rooms";

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
	public void testGetRoomsPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + PAGE_SIZE)).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(1))
		.andExpect(jsonPath("$[0].numberOfBeds").value(1));
	}
	
	@Test
	public void testGetRoom() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/" + 3L)).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(3))
		.andExpect(jsonPath("$.numberOfBeds").value(4));
	}
	
	/*@Test
	public void testSaveRoom() throws Exception {
		Room room = new Room(DB_ID, DB_NUMBER_OF_BEDS, DB_VERSION);

		String json = com.isap.ISAProject.TestUtil.json(room);
		this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	public void testUpdateRentacar() throws Exception {
		Room room = new Room(DB_ID, DB_NUMBER_OF_BEDS, DB_VERSION);

		String json = com.isap.ISAProject.TestUtil.json(room);
		this.mockMvc.perform(put(URL_PREFIX + "/" + DB_ID).accept(contentType).contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteStudent() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/" + NEW_ID)).andExpect(status().isOk());
	}*/
}
