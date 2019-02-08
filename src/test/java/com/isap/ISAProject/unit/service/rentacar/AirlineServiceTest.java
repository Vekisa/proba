package com.isap.ISAProject.unit.service.rentacar;

import static com.isap.ISAProject.unit.constants.airline.AirlineConstants.DB_ADDRESS;
import static com.isap.ISAProject.unit.constants.airline.AirlineConstants.DB_DESCRIPTION;
import static com.isap.ISAProject.unit.constants.airline.AirlineConstants.DB_ID;
import static com.isap.ISAProject.unit.constants.airline.AirlineConstants.DB_NAME;
import static com.isap.ISAProject.unit.constants.airline.AirlineConstants.NEW_ADDRESS;
import static com.isap.ISAProject.unit.constants.airline.AirlineConstants.NEW_DESCRIPTION;
import static com.isap.ISAProject.unit.constants.airline.AirlineConstants.NEW_NAME;
import static com.isap.ISAProject.unit.constants.airline.AirlineConstants.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.repository.airline.AirlineRepository;
import com.isap.ISAProject.repository.airline.FlightRepository;
import com.isap.ISAProject.repository.airline.LocationRepository;
import com.isap.ISAProject.service.airline.AirlineService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AirlineServiceTest {
	@Mock
	private AirlineRepository repository;

	@Mock
	private LocationRepository destinationRepository;

	@Mock
	private FlightRepository flightRepository;
	
	@InjectMocks
	private AirlineService service;
	
	@Mock
	private Airline rentacarMock;
	
	@Test
	public void testGetAll() {
		PageRequest pageRequest = new PageRequest(0, PAGE_SIZE);
		when(repository.findAll(pageRequest))
		.thenReturn(new PageImpl<Airline>(Arrays.asList(new Airline(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION))
				.subList(0, 1), pageRequest, 1));
		List<Airline> rentacars = service.findAll(pageRequest);
		assertThat(rentacars).hasSize(1);
		verify(repository, times(1)).findAll(pageRequest);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testGetById() {
		rentacarMock = new Airline(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION);
		when(repository.findById(DB_ID)).thenReturn(Optional.of(rentacarMock));
		Airline dbRentacar = service.findById(DB_ID);
		assertEquals(dbRentacar.getId(), DB_ID);
		verify(repository, times(1)).findById(DB_ID);
		verifyNoMoreInteractions(repository);
	}
}
