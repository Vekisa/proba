package com.isap.ISAProject.integration.user;

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

import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.service.user.RegisteredUserService;
import com.isap.ISAProject.unit.service.user.UsersStaticData;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisteredUserIntegrationTest {
	
	@Autowired
	RegisteredUserService service;
	
	private static final String URL_PREFIX = "/users/registered";

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
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + 5)).andExpect(status().isOk())
		.andExpect(jsonPath("$[2].id").value(4))
		.andExpect(jsonPath("$[2].email").value("asantora3@pinterest.com"))
		.andExpect(jsonPath("$[2].username").value("user4"))
		.andExpect(jsonPath("$[2].firstName").value("Ailsun"))
		.andExpect(jsonPath("$[2].lastName").value("Santora"))
		.andExpect(jsonPath("$[2].city").value("Stenungsund"))
		.andExpect(jsonPath("$[2].phoneNumber").value("839-156-2257"));
	}
	
	@Test
	public void testGetUserById() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/4")).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(4))
		.andExpect(jsonPath("$.email").value("asantora3@pinterest.com"))
		.andExpect(jsonPath("$.username").value("user4"))
		.andExpect(jsonPath("$.firstName").value("Ailsun"))
		.andExpect(jsonPath("$.lastName").value("Santora"))
		.andExpect(jsonPath("$.city").value("Stenungsund"))
		.andExpect(jsonPath("$.phoneNumber").value("839-156-2257"));
	}
	
	@Test
	public void testSaveUser() throws Exception {
		RegisteredUser user = new RegisteredUser(
				UsersStaticData.REGISTERED_1_EMAIL, 
				UsersStaticData.REGISTERED_1_USERNAME, 
				UsersStaticData.REGISTERED_1_PASSWORD, 
				UsersStaticData.REGISTERED_1_FIRSTNAME, 
				UsersStaticData.REGISTERED_1_LASTNAME, 
				UsersStaticData.REGISTERED_1_CITY, 
				UsersStaticData.REGISTERED_1_PHONE_NUMBER);
		String json = com.isap.ISAProject.TestUtil.json(user);
		this.mockMvc.perform(post(URL_PREFIX).contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		RegisteredUser user = new RegisteredUser(
				UsersStaticData.UNIQUE_EMAIL, 
				UsersStaticData.UNIQUE_USERNAME, 
				UsersStaticData.REGISTERED_1_PASSWORD, 
				UsersStaticData.REGISTERED_1_FIRSTNAME, 
				UsersStaticData.REGISTERED_1_LASTNAME, 
				UsersStaticData.REGISTERED_1_CITY, 
				UsersStaticData.REGISTERED_1_PHONE_NUMBER);
		String json = com.isap.ISAProject.TestUtil.json(user);
		this.mockMvc.perform(put(URL_PREFIX + "/2").accept(contentType).contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	public void testGetFriends() throws Exception {
		this.mockMvc.perform(get(URL_PREFIX + "/4/friends")).andExpect(status().isOk());
		this.mockMvc.perform(get(URL_PREFIX + "/45/friends")).andExpect(status().isNoContent());
		this.mockMvc.perform(get(URL_PREFIX + "/8/friends")).andExpect(status().isNoContent());
	}
}
