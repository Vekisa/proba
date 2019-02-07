package com.isap.ISAProject.controller.rentacar;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.isap.ISAProject.constants.RentACarConstants.DB_ADDRESS;
import static com.isap.ISAProject.constants.RentACarConstants.DB_DESCRIPTION;
import static com.isap.ISAProject.constants.RentACarConstants.DB_ID;
import static com.isap.ISAProject.constants.RentACarConstants.DB_NAME;
import static com.isap.ISAProject.constants.RentACarConstants.NEW_ADDRESS;
import static com.isap.ISAProject.constants.RentACarConstants.NEW_DESCRIPTION;
import static com.isap.ISAProject.constants.RentACarConstants.NEW_NAME;
import static com.isap.ISAProject.constants.RentACarConstants.PAGE_SIZE;

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
import com.isap.ISAProject.model.rentacar.RentACar;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentACarControllerTest {
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
	public void testGetRentacarsPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + PAGE_SIZE)).andExpect(status().isOk())
		.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(PAGE_SIZE)))
		.andExpect(jsonPath("$.[*].id").value(hasItem(DB_ID.intValue())))
		.andExpect(jsonPath("$.[*].name").value(hasItem(DB_NAME)))
		.andExpect(jsonPath("$.[*].address").value(hasItem(DB_ADDRESS)))
		.andExpect(jsonPath("$.[*].description").value(hasItem(DB_DESCRIPTION)));
	}
	
	@Test
	public void testGetRentacar() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/" + DB_ID)).andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$.id").value(DB_ID.intValue()))
		.andExpect(jsonPath("$.name").value(DB_NAME))
		.andExpect(jsonPath("$.address").value(DB_ADDRESS))
		.andExpect(jsonPath("$.description").value(DB_DESCRIPTION));
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testSaveRentacar() throws Exception {
		RentACar car = new RentACar();
		car.setName(DB_NAME);
		car.setAddress(DB_ADDRESS);
		car.setDescription(DB_DESCRIPTION);

		String json = com.isap.ISAProject.TestUtil.json(car);
		this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateRentacar() throws Exception {
		RentACar car = new RentACar();
		car.setId(DB_ID);
		car.setName(NEW_NAME);
		car.setAddress(NEW_ADDRESS);
		car.setDescription(NEW_DESCRIPTION);

		String json = TestUtil.json(car);
		this.mockMvc.perform(put(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteRentacar() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/" + DB_ID)).andExpect(status().isOk());
	}
}
