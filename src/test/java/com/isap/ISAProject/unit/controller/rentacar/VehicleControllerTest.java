package com.isap.ISAProject.unit.controller.rentacar;

import static com.isap.ISAProject.unit.constants.VehicleConstants.DB_BRAND;
import static com.isap.ISAProject.unit.constants.VehicleConstants.DB_DISCOUNT;
import static com.isap.ISAProject.unit.constants.VehicleConstants.DB_ID;
import static com.isap.ISAProject.unit.constants.VehicleConstants.DB_MODEL;
import static com.isap.ISAProject.unit.constants.VehicleConstants.DB_PRICE_PER_DAY;
import static com.isap.ISAProject.unit.constants.VehicleConstants.DB_PROD_YEAR;
import static com.isap.ISAProject.unit.constants.VehicleConstants.DB_SEATS_NUMBER;
import static com.isap.ISAProject.unit.constants.VehicleConstants.DB_TYPE;
import static com.isap.ISAProject.unit.constants.VehicleConstants.NEW_BRAND;
import static com.isap.ISAProject.unit.constants.VehicleConstants.NEW_DISCOUNT;
import static com.isap.ISAProject.unit.constants.VehicleConstants.NEW_MODEL;
import static com.isap.ISAProject.unit.constants.VehicleConstants.NEW_PRICE_PER_DAY;
import static com.isap.ISAProject.unit.constants.VehicleConstants.NEW_PROD_YEAR;
import static com.isap.ISAProject.unit.constants.VehicleConstants.NEW_SEATS_NUMBER;
import static com.isap.ISAProject.unit.constants.VehicleConstants.NEW_TYPE;
import static com.isap.ISAProject.unit.constants.VehicleConstants.PAGE_SIZE;
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
import com.isap.ISAProject.model.rentacar.Vehicle;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleControllerTest {
	private static final String URL_PREFIX = "/vehicles";

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
	public void testGetVehiclesPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + PAGE_SIZE)).andExpect(status().isOk())
		.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(PAGE_SIZE)))
		.andExpect(jsonPath("$.[*].id").value(hasItem(DB_ID.intValue())))
		.andExpect(jsonPath("$.[*].pricePerDay").value(hasItem(DB_PRICE_PER_DAY)))
		.andExpect(jsonPath("$.[*].discount").value(hasItem(DB_DISCOUNT)))
		.andExpect(jsonPath("$.[*].brand").value(hasItem(DB_BRAND)))
		.andExpect(jsonPath("$.[*].model").value(hasItem(DB_MODEL)))
		.andExpect(jsonPath("$.[*].productionYear").value(hasItem(DB_PROD_YEAR)))
		.andExpect(jsonPath("$.[*].type").value(hasItem(DB_TYPE)))
		.andExpect(jsonPath("$.[*].seatsNumber").value(hasItem(DB_SEATS_NUMBER)));
	}
	
	@Test
	public void testGetVehicle() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/" + DB_ID)).andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$.id").value(DB_ID.intValue()))
		.andExpect(jsonPath("$.[*].pricePerDay").value(hasItem(DB_PRICE_PER_DAY)))
		.andExpect(jsonPath("$.[*].discount").value(hasItem(DB_DISCOUNT)))
		.andExpect(jsonPath("$.[*].brand").value(hasItem(DB_BRAND)))
		.andExpect(jsonPath("$.[*].model").value(hasItem(DB_MODEL)))
		.andExpect(jsonPath("$.[*].productionYear").value(hasItem(DB_PROD_YEAR)))
		.andExpect(jsonPath("$.[*].type").value(hasItem(DB_TYPE)))
		.andExpect(jsonPath("$.[*].seatsNumber").value(hasItem(DB_SEATS_NUMBER)));
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testSaveVehicle() throws Exception {
		Vehicle car = new Vehicle();
		car.setPricePerDay(DB_PRICE_PER_DAY);
		car.setDiscount(DB_DISCOUNT);
		car.setBrand(DB_BRAND);
		car.setModel(DB_MODEL);
		car.setProductionYear(DB_PROD_YEAR);
		car.setType(DB_TYPE);
		car.setSeatsNumber(DB_SEATS_NUMBER);

		String json = com.isap.ISAProject.TestUtil.json(car);
		this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateVehicle() throws Exception {
		Vehicle car = new Vehicle();
		car.setPricePerDay(NEW_PRICE_PER_DAY);
		car.setDiscount(NEW_DISCOUNT);
		car.setBrand(NEW_BRAND);
		car.setModel(NEW_MODEL);
		car.setProductionYear(NEW_PROD_YEAR);
		car.setType(NEW_TYPE);
		car.setSeatsNumber(NEW_SEATS_NUMBER);


		String json = TestUtil.json(car);
		this.mockMvc.perform(put(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteVehicle() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/" + DB_ID)).andExpect(status().isOk());
	}
}
