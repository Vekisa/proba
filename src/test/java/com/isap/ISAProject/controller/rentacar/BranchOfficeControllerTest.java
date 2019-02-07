package com.isap.ISAProject.controller.rentacar;

import static com.isap.ISAProject.constants.BranchOfficeConstants.DB_ADDRESS;
import static com.isap.ISAProject.constants.BranchOfficeConstants.DB_ID;
import static com.isap.ISAProject.constants.BranchOfficeConstants.NEW_ADDRESS;
import static com.isap.ISAProject.constants.BranchOfficeConstants.PAGE_SIZE;
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
import com.isap.ISAProject.model.rentacar.BranchOffice;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BranchOfficeControllerTest {
	private static final String URL_PREFIX = "/branch_offices";

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
	public void testGetBranchOfficesPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + PAGE_SIZE)).andExpect(status().isOk())
		.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(PAGE_SIZE)))
		.andExpect(jsonPath("$.[*].id").value(hasItem(DB_ID.intValue())))
		.andExpect(jsonPath("$.[*].address").value(hasItem(DB_ADDRESS)));
	}
	
	@Test
	public void testGetBranchOffice() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/" + DB_ID)).andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$.id").value(DB_ID.intValue()))
		.andExpect(jsonPath("$.address").value(DB_ADDRESS));
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testSaveBranchOffice() throws Exception {
		BranchOffice car = new BranchOffice();
		car.setAddress(DB_ADDRESS);

		String json = com.isap.ISAProject.TestUtil.json(car);
		this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateBranchOffice() throws Exception {
		BranchOffice car = new BranchOffice();
		car.setId(DB_ID);
		car.setAddress(NEW_ADDRESS);

		String json = TestUtil.json(car);
		this.mockMvc.perform(put(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteBranchOffice() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/" + DB_ID)).andExpect(status().isOk());
	}
}
