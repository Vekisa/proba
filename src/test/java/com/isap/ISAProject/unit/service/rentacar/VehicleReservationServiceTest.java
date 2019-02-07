package com.isap.ISAProject.unit.service.rentacar;

import static com.isap.ISAProject.unit.constants.VehicleReservationConstants.DB_BEGIN_DATE;
import static com.isap.ISAProject.unit.constants.VehicleReservationConstants.DB_END_DATE;
import static com.isap.ISAProject.unit.constants.VehicleReservationConstants.DB_ID;
import static com.isap.ISAProject.unit.constants.VehicleReservationConstants.DB_ID_TO_DELETE;
import static com.isap.ISAProject.unit.constants.VehicleReservationConstants.DB_PRICE;
import static com.isap.ISAProject.unit.constants.VehicleReservationConstants.NEW_BEGIN_DATE;
import static com.isap.ISAProject.unit.constants.VehicleReservationConstants.NEW_END_DATE;
import static com.isap.ISAProject.unit.constants.VehicleReservationConstants.NEW_PRICE;
import static com.isap.ISAProject.unit.constants.VehicleReservationConstants.PAGE_SIZE;
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

import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.repository.rentacar.VehicleReservationRepository;
import com.isap.ISAProject.service.rentacar.VehicleReservationService;

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

import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.repository.rentacar.VehicleReservationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleReservationServiceTest {
	@Mock
	private VehicleReservationRepository repositoryMock;
	
	@Mock
	private Optional<VehicleReservation> VehicleReservationMock;
	
	@InjectMocks
	private VehicleReservationService service;
	
	@Test
	public void testGetAllVehicleReservations() {
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		when(repositoryMock.findAll(pageRequest))
		.thenReturn(new PageImpl<VehicleReservation>(Arrays.asList(new VehicleReservation(DB_ID, DB_BEGIN_DATE, DB_END_DATE, DB_PRICE))
				.subList(0, 1), pageRequest, 1));
		List<VehicleReservation> VehicleReservations = service.getAllVehicleReservation(pageRequest);
		assertThat(VehicleReservations).hasSize(1);
		verify(repositoryMock, times(1)).findAll(pageRequest);
		verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
	public void testGetVehicleReservationById() {
		when(repositoryMock.findById(DB_ID)).thenReturn(VehicleReservationMock);
		VehicleReservation dbVehicleReservation = service.getVehicleReservationById(DB_ID);
		assertEquals(VehicleReservationMock.get(), dbVehicleReservation);
		verify(repositoryMock, times(1)).findById(DB_ID);
		verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
    @Transactional
    @Rollback(true) //it can be omitted because it is true by default
	public void testSaveVehicleReservation() {
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new VehicleReservation(DB_ID, DB_BEGIN_DATE, DB_END_DATE, DB_PRICE)));
		VehicleReservation car = new VehicleReservation();
		car.setBeginDate(DB_BEGIN_DATE);
		car.setEndDate(DB_END_DATE);
		car.setPrice(DB_PRICE);
		
		when(repositoryMock.save(car)).thenReturn(car);
		
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		int dbSizeBeforeAdd = service.getAllVehicleReservation(pageRequest).size();
		
		VehicleReservation dbVehicleReservation = service.saveVehicleReservation(car);
		assertThat(dbVehicleReservation).isNotNull();
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new VehicleReservation(DB_ID, DB_BEGIN_DATE, DB_END_DATE, DB_PRICE), car));
		// Validate that new VehicleReservation is in the database
        List<VehicleReservation> VehicleReservations = service.getAllVehicleReservation(pageRequest);
        assertThat(VehicleReservations).hasSize(dbSizeBeforeAdd + 1);
        dbVehicleReservation = VehicleReservations.get(VehicleReservations.size() - 1); //get last VehicleReservation
        assertThat(dbVehicleReservation.getBeginDate()).isEqualTo(NEW_BEGIN_DATE);
        assertThat(dbVehicleReservation.getEndDate()).isEqualTo(NEW_END_DATE);
        assertThat(dbVehicleReservation.getPrice()).isEqualTo(NEW_PRICE);
        verify(repositoryMock, times(2)).findAll();
        verify(repositoryMock, times(1)).save(car);
        verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
    @Transactional
    @Rollback(true)
	public void testUpdateVehicleReservation() {
		
		when(repositoryMock.findById(DB_ID).get()).thenReturn(new VehicleReservation(DB_ID, DB_BEGIN_DATE, DB_END_DATE, DB_PRICE));
		VehicleReservation dbCar = service.getVehicleReservationById(DB_ID);
		
		dbCar.setBeginDate(NEW_BEGIN_DATE);
		dbCar.setEndDate(NEW_END_DATE);
		dbCar.setPrice(NEW_PRICE);
		
		when(repositoryMock.save(dbCar)).thenReturn(dbCar);
		dbCar = service.saveVehicleReservation(dbCar);
		assertThat(dbCar).isNotNull();
		
		//verify that database contains updated data
		dbCar = service.getVehicleReservationById(DB_ID);
        assertThat(dbCar.getBeginDate()).isEqualTo(NEW_BEGIN_DATE);
        assertThat(dbCar.getEndDate()).isEqualTo(NEW_END_DATE);
        assertThat(dbCar.getPrice()).isEqualTo(NEW_PRICE);
        verify(repositoryMock, times(2)).findById(DB_ID);
        verify(repositoryMock, times(1)).save(dbCar);
        verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testRemove() {
		
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new VehicleReservation(DB_ID, DB_BEGIN_DATE, DB_END_DATE, DB_PRICE)));
		int dbSizeBeforeRemove = service.getAllVehicleReservation(pageRequest).size();
		VehicleReservation dbVehicleReservation = service.getVehicleReservationById(DB_ID_TO_DELETE);
		doNothing().when(repositoryMock).delete(dbVehicleReservation);
		service.deleteVehicleReservation(DB_ID_TO_DELETE);
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new VehicleReservation(DB_ID, DB_BEGIN_DATE, DB_END_DATE, DB_PRICE)));
		List<VehicleReservation> VehicleReservations = service.getAllVehicleReservation(pageRequest);
		assertThat(VehicleReservations).hasSize(dbSizeBeforeRemove - 1);
		
		when(repositoryMock.findById(DB_ID_TO_DELETE)).thenReturn(null);
		assertThat(dbVehicleReservation).isNull();
		verify(repositoryMock, times(1)).delete(dbVehicleReservation);
		verify(repositoryMock, times(2)).findAll();
        verify(repositoryMock, times(1)).findById(DB_ID_TO_DELETE);
        verifyNoMoreInteractions(repositoryMock);
	}
}
