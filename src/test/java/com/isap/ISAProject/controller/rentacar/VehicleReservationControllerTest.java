package com.isap.ISAProject.controller.rentacar;

import static com.isap.ISAProject.constants.VehicleReservationConstants.DB_ID;
import static com.isap.ISAProject.constants.VehicleReservationConstants.DB_ID_TO_DELETE;
import static com.isap.ISAProject.constants.VehicleReservationConstants.DB_BEGIN_DATE;
import static com.isap.ISAProject.constants.VehicleReservationConstants.DB_END_DATE;
import static com.isap.ISAProject.constants.VehicleReservationConstants.DB_PRICE;
import static com.isap.ISAProject.constants.VehicleReservationConstants.NEW_BEGIN_DATE;
import static com.isap.ISAProject.constants.VehicleReservationConstants.NEW_END_DATE;
import static com.isap.ISAProject.constants.VehicleReservationConstants.NEW_PRICE;
import static com.isap.ISAProject.constants.VehicleReservationConstants.PAGE_SIZE;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.isap.ISAProject.TestUtil;
import com.isap.ISAProject.model.rentacar.VehicleReservation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleReservationControllerTest {
	private static final String URL_PREFIX = "/vehicle-reservations";

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
	public void testGetVehicleReservationsPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + PAGE_SIZE)).andExpect(status().isOk())
		.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(PAGE_SIZE)))
		.andExpect(jsonPath("$.[*].id").value(hasItem(DB_ID.intValue())))
		.andExpect(jsonPath("$.[*].beginDate").value(hasItem(DB_BEGIN_DATE)))
		.andExpect(jsonPath("$.[*].endDate").value(hasItem(DB_END_DATE)))
		.andExpect(jsonPath("$.[*].price").value(hasItem(DB_PRICE)));
	}
	
	@Test
	public void testGetVehicleReservation() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/" + DB_ID)).andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$.id").value(DB_ID.intValue()))
		.andExpect(jsonPath("$.beginDate").value(DB_BEGIN_DATE))
		.andExpect(jsonPath("$.endDate").value(DB_END_DATE))
		.andExpect(jsonPath("$.price").value(DB_PRICE));
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testSaveVehicleReservation() throws Exception {
		VehicleReservation car = new VehicleReservation();
		car.setBeginDate(DB_BEGIN_DATE);
		car.setEndDate(DB_END_DATE);
		car.setPrice(DB_PRICE);

		String json = com.isap.ISAProject.TestUtil.json(car);
		this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateVehicleReservation() throws Exception {
		VehicleReservation car = new VehicleReservation();
		car.setId(DB_ID);
		car.setBeginDate(NEW_BEGIN_DATE);
		car.setEndDate(NEW_END_DATE);
		car.setPrice(NEW_PRICE);

		String json = TestUtil.json(car);
		this.mockMvc.perform(put(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteVehicleReservation() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/" + DB_ID)).andExpect(status().isOk());
	}
}
