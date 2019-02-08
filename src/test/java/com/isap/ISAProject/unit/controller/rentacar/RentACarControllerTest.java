package com.isap.ISAProject.unit.controller.rentacar;

import static com.isap.ISAProject.unit.constants.RentACarConstants.DB_ADDRESS;
import static com.isap.ISAProject.unit.constants.RentACarConstants.DB_DESCRIPTION;
import static com.isap.ISAProject.unit.constants.RentACarConstants.DB_ID;
import static com.isap.ISAProject.unit.constants.RentACarConstants.DB_NAME;
import static com.isap.ISAProject.unit.constants.RentACarConstants.NEW_ADDRESS;
import static com.isap.ISAProject.unit.constants.RentACarConstants.NEW_DESCRIPTION;
import static com.isap.ISAProject.unit.constants.RentACarConstants.NEW_ID;
import static com.isap.ISAProject.unit.constants.RentACarConstants.NEW_NAME;
import static com.isap.ISAProject.unit.constants.RentACarConstants.PAGE_SIZE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.isap.ISAProject.controller.rentacar.RentACarController;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.service.rentacar.RentACarService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentACarControllerTest {
	private static final String URL_PREFIX = "/rent-a-cars";
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	private MockMvc mockMvc;

	@Mock
	private RentACarService service;
	
	@InjectMocks
    private RentACarController accessController;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accessController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
	}

	@Test
	public void testGetAllRentacars() throws Exception {
		RentACar car1 = new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION);
		RentACar car2 = new RentACar(NEW_ID, NEW_NAME, NEW_ADDRESS, NEW_DESCRIPTION);
		
		Pageable pageRequest = PageRequest.of(0, PAGE_SIZE);
		when(service.getAllRentACars(pageRequest)).thenReturn(Arrays.asList(car1, car2));
		mockMvc.perform(get(URL_PREFIX + "?page=0&size=" + PAGE_SIZE)).andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(DB_ID))
		.andExpect(jsonPath("$[0].name").value(DB_NAME))
		.andExpect(jsonPath("$[0].address").value(DB_ADDRESS))
		.andExpect(jsonPath("$[0].description").value(DB_DESCRIPTION))
		.andExpect(jsonPath("$[1].id").value(NEW_ID))
		.andExpect(jsonPath("$[1].name").value(NEW_NAME))
		.andExpect(jsonPath("$[1].address").value(NEW_ADDRESS))
		.andExpect(jsonPath("$[1].description").value(NEW_DESCRIPTION));
	}

	@Test
	public void testGetRentacarById() throws Exception {
		RentACar car = new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION);
		when(service.getRentACarById(DB_ID)).thenReturn(car);
		mockMvc.perform(get(URL_PREFIX + "/" + DB_ID)).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(DB_ID))
		.andExpect(jsonPath("$.name").value(DB_NAME))
		.andExpect(jsonPath("$.address").value(DB_ADDRESS))
		.andExpect(jsonPath("$.description").value(DB_DESCRIPTION));
	}
	
	/*@Test
	public void testUpdateRentACar() throws Exception {
		RentACar car = new RentACar(DB_ID, NEW_NAME, NEW_ADDRESS, NEW_DESCRIPTION);

		String json = com.isap.ISAProject.TestUtil.json(car);
		this.mockMvc.perform(put(URL_PREFIX + "/" + DB_ID).accept(contentType).contentType(contentType).content(json)).andExpect(status().isOk());
	}*/
}
