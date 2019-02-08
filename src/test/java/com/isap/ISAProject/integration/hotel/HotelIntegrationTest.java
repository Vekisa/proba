package com.isap.ISAProject.integration.hotel;

import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.DB_ID;
import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.NEW_ADDRESS;
import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.NEW_DESCRIPTION;
import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.NEW_NAME;
import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.PAGE_SIZE;
import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.DB_ID;
import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.NEW_ADDRESS;
import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.NEW_DESCRIPTION;
import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.NEW_ID;
import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.NEW_NAME;
import static com.isap.ISAProject.unit.constants.hotel.HotelConstants.LOCATION_ID;
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

import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.service.hotel.HotelService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HotelIntegrationTest {
	
	@Autowired
	HotelService service;
	
	private static final String URL_PREFIX = "/hotels";

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
	public void testGetRentacarsPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + PAGE_SIZE)).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(1))
		.andExpect(jsonPath("$[0].name").value("Herman, Flatley and Gleichner"))
		.andExpect(jsonPath("$[0].address").value("41 Mallard Road"))
		.andExpect(jsonPath("$[0].description").value("Versatile mobile emulation"));
	}
	
	@Test
	public void testGetRentacar() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/" + 3L)).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(3))
		.andExpect(jsonPath("$.name").value("Marquardt-Gusikowski"));
	}
	
	/*@Test
	public void testSaveRentacar() throws Exception {
		Hotel hotel = new Hotel(DB_ID, NEW_NAME, NEW_ADDRESS, NEW_DESCRIPTION);

		String json = com.isap.ISAProject.TestUtil.json(hotel);
		this.mockMvc.perform(post(URL_PREFIX + "?location=" + LOCATION_ID).contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	public void testUpdateRentacar() throws Exception {
		Hotel car = new Hotel(DB_ID, NEW_NAME, NEW_ADDRESS, NEW_DESCRIPTION);

		String json = com.isap.ISAProject.TestUtil.json(car);
		this.mockMvc.perform(put(URL_PREFIX + "/" + DB_ID).accept(contentType).contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteStudent() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/" + NEW_ID)).andExpect(status().isOk());
	}*/
}
