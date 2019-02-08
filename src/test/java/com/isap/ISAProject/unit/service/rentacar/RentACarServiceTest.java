package com.isap.ISAProject.unit.service.rentacar;

import static com.isap.ISAProject.unit.constants.rentacar.RentACarConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
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

import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.repository.airline.LocationRepository;
import com.isap.ISAProject.repository.rentacar.BranchOfficeRepository;
import com.isap.ISAProject.repository.rentacar.RentACarRepository;
import com.isap.ISAProject.repository.rentacar.VehicleRepository;
import com.isap.ISAProject.repository.rentacar.VehicleReservationRepository;
import com.isap.ISAProject.service.rentacar.RentACarService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentACarServiceTest {
	@Mock
	private RentACarRepository repositoryMock;
	
	@Mock
	private RentACar rentacarMock;
	
	@Mock
	private VehicleReservationRepository vrRepo;
	
	@Mock
	private BranchOfficeRepository broRepo;
	
	@Mock
	private LocationRepository locationRepository;
	
	@Mock
	VehicleRepository vRepo;
	
	@InjectMocks
	private RentACarService service;
	
	@Test
	public void testGetAllRentacars() {
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		when(repositoryMock.findAll(pageRequest))
		.thenReturn(new PageImpl<RentACar>(Arrays.asList(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION))
				.subList(0, 1), pageRequest, 1));
		List<RentACar> rentacars = service.getAllRentACars(pageRequest);
		assertThat(rentacars).hasSize(1);
		verify(repositoryMock, times(1)).findAll(pageRequest);
		verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
	public void testGetRentacarById() {
		rentacarMock = new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION);
		when(repositoryMock.findById(DB_ID)).thenReturn(Optional.of(rentacarMock));
		RentACar dbRentacar = service.getRentACarById(DB_ID);
		assertEquals(dbRentacar.getId(), DB_ID);
		verify(repositoryMock, times(1)).findById(DB_ID);
		verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
    @Transactional
    @Rollback(true) //it can be omitted because it is true by default
	public void testSaveRentacar() {
		PageRequest pageRequest = new PageRequest(0, PAGE_SIZE);
		when(repositoryMock.findAll(pageRequest))
		.thenReturn(new PageImpl<RentACar>(Arrays.asList(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION))
				.subList(0, 1), pageRequest, 1));
		List<RentACar> cars = service.getAllRentACars(pageRequest);
		RentACar car = new RentACar();
		car.setName(NEW_NAME);
		car.setAddress(NEW_ADDRESS);
		car.setDescription(NEW_DESCRIPTION);
		
		when(repositoryMock.save(car)).thenReturn(car);
		
		int dbSizeBeforeAdd = cars.size();
		
		RentACar dbRentacar = service.saveRentACar(car);
		assertThat(dbRentacar).isNotNull();
		
		when(repositoryMock.findAll(pageRequest))
		.thenReturn(new PageImpl<RentACar>(Arrays.asList(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION), dbRentacar), pageRequest, 1));
		// Validate that new RentACar is in the database
        List<RentACar> rentacars = service.getAllRentACars(pageRequest);
        assertThat(rentacars).hasSize(dbSizeBeforeAdd + 1);
	}
	
	@Test
    @Transactional
    @Rollback(true)
	public void testUpdateRentacar() {
		
		when(repositoryMock.findById(DB_ID)).thenReturn(Optional.of(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION)));
		RentACar dbCar = service.getRentACarById(DB_ID);
		
		dbCar.setName(NEW_NAME);
		dbCar.setAddress(NEW_ADDRESS);
		dbCar.setDescription(NEW_DESCRIPTION);
		
		when(repositoryMock.save(dbCar)).thenReturn(dbCar);
		dbCar = service.saveRentACar(dbCar);
		assertThat(dbCar).isNotNull();
		
		//verify that database contains updated data
		dbCar = service.getRentACarById(DB_ID);
        assertThat(dbCar.getName()).isEqualTo(NEW_NAME);
        assertThat(dbCar.getAddress()).isEqualTo(NEW_ADDRESS);
        assertThat(dbCar.getDescription()).isEqualTo(NEW_DESCRIPTION);
        verify(repositoryMock, times(2)).findById(DB_ID);
        verify(repositoryMock, times(1)).save(dbCar);
        verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testRemove() {
		
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		when(repositoryMock.findAll(pageRequest))
		.thenReturn(new PageImpl<RentACar>(Arrays.asList(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION))
				.subList(0, 1), pageRequest, 1));
		int dbSizeBeforeRemove = service.getAllRentACars(pageRequest).size();
		
		rentacarMock = new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION);
		when(repositoryMock.findById(DB_ID)).thenReturn(Optional.of(rentacarMock));
		RentACar dbRentACar = service.getRentACarById(DB_ID_TO_DELETE);
		doNothing().when(repositoryMock).delete(dbRentACar);
		service.deleteRentACar(DB_ID_TO_DELETE);
		
		when(repositoryMock.findAll(pageRequest))
		.thenReturn(new PageImpl<RentACar>(Arrays.asList(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION))
				.subList(0, 1), pageRequest, 1));
		List<RentACar> RentACars = service.getAllRentACars(pageRequest);
		assertThat(RentACars).hasSize(dbSizeBeforeRemove);
	}
}
