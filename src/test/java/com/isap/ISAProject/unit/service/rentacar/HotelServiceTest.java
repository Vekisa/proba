package com.isap.ISAProject.unit.service.rentacar;

import static com.isap.ISAProject.unit.constants.rentacar.RentACarConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.repository.airline.LocationRepository;
import com.isap.ISAProject.repository.hotel.CatalogueRepository;
import com.isap.ISAProject.repository.hotel.FloorRepository;
import com.isap.ISAProject.repository.hotel.HotelRepository;
import com.isap.ISAProject.repository.hotel.RoomRepository;
import com.isap.ISAProject.repository.hotel.RoomReservationRepository;
import com.isap.ISAProject.service.hotel.HotelService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HotelServiceTest {
	@Mock
	private HotelRepository hotelRepository;

	@Mock
	private Hotel rentacarMock;
	
	@Mock
	private LocationRepository destinationRepository;
	
	@Mock
	private CatalogueRepository catalogueRepository;
	
	@Mock
	private FloorRepository floorRepository;
	
	@Mock
	private RoomReservationRepository rrRepo;
	
	@Mock
	private RoomRepository roomRepo;
	
	@InjectMocks
	private HotelService service;
	
	@Test
	public void testGetAllRentacars() {
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		when(hotelRepository.findAll(pageRequest))
		.thenReturn(new PageImpl<Hotel>(Arrays.asList(new Hotel(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION))
				.subList(0, 1), pageRequest, 1));
		List<Hotel> rentacars = service.findAll(pageRequest);
		assertThat(rentacars).hasSize(1);
		verify(hotelRepository, times(1)).findAll(pageRequest);
		verifyNoMoreInteractions(hotelRepository);
	}
	
	@Test
	public void testById() {
		rentacarMock = new Hotel(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION);
		when(hotelRepository.findById(DB_ID)).thenReturn(Optional.of(rentacarMock));
		Hotel dbRentacar = service.findById(DB_ID);
		assertEquals(dbRentacar.getId(), DB_ID);
		verify(hotelRepository, times(1)).findById(DB_ID);
		verifyNoMoreInteractions(hotelRepository);
	}
	
	@Test
    @Transactional
    @Rollback(true) //it can be omitted because it is true by default
	public void testSaveRentacar() {
		PageRequest pageRequest = new PageRequest(0, PAGE_SIZE);
		when(hotelRepository.findAll(pageRequest))
		.thenReturn(new PageImpl<Hotel>(Arrays.asList(new Hotel(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION))
				.subList(0, 1), pageRequest, 1));
		List<Hotel> cars = service.findAll(pageRequest);
		Hotel car = new Hotel();
		car.setName(NEW_NAME);
		car.setAddress(NEW_ADDRESS);
		car.setDescription(NEW_DESCRIPTION);
		
		when(hotelRepository.save(car)).thenReturn(car);
		
		int dbSizeBeforeAdd = cars.size();
		
		Hotel dbRentacar = service.save(car);
		assertThat(dbRentacar).isNotNull();
		
		when(hotelRepository.findAll(pageRequest))
		.thenReturn(new PageImpl<Hotel>(Arrays.asList(new Hotel(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION), dbRentacar), pageRequest, 1));
		// Validate that new RentACar is in the database
        List<Hotel> rentacars = service.findAll(pageRequest);
        assertThat(rentacars).hasSize(dbSizeBeforeAdd + 1);
	}
	
	@Test
    @Transactional
    @Rollback(true)
	public void testUpdateRentacar() {
		
		when(hotelRepository.findById(DB_ID)).thenReturn(Optional.of(new Hotel(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION)));
		Hotel dbCar = service.findById(DB_ID);
		
		dbCar.setName(NEW_NAME);
		dbCar.setAddress(NEW_ADDRESS);
		dbCar.setDescription(NEW_DESCRIPTION);
		
		when(hotelRepository.save(dbCar)).thenReturn(dbCar);
		dbCar = service.save(dbCar);
		assertThat(dbCar).isNotNull();
		
		//verify that database contains updated data
		dbCar = service.findById(DB_ID);
        assertThat(dbCar.getName()).isEqualTo(NEW_NAME);
        assertThat(dbCar.getAddress()).isEqualTo(NEW_ADDRESS);
        assertThat(dbCar.getDescription()).isEqualTo(NEW_DESCRIPTION);
        verify(hotelRepository, times(2)).findById(DB_ID);
        verify(hotelRepository, times(1)).save(dbCar);
        verifyNoMoreInteractions(hotelRepository);
	}
}
