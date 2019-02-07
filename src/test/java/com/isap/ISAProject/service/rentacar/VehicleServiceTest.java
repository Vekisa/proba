package com.isap.ISAProject.service.rentacar;

import static com.isap.ISAProject.constants.VehicleConstants.DB_PRICE_PER_DAY;
import static com.isap.ISAProject.constants.VehicleConstants.DB_DISCOUNT;
import static com.isap.ISAProject.constants.VehicleConstants.DB_ID;
import static com.isap.ISAProject.constants.VehicleConstants.DB_ID_TO_DELETE;
import static com.isap.ISAProject.constants.VehicleConstants.DB_BRAND;
import static com.isap.ISAProject.constants.VehicleConstants.DB_MODEL;
import static com.isap.ISAProject.constants.VehicleConstants.DB_PROD_YEAR;
import static com.isap.ISAProject.constants.VehicleConstants.DB_TYPE;
import static com.isap.ISAProject.constants.VehicleConstants.DB_SEATS_NUMBER;
import static com.isap.ISAProject.constants.VehicleConstants.NEW_PRICE_PER_DAY;
import static com.isap.ISAProject.constants.VehicleConstants.NEW_DISCOUNT;
import static com.isap.ISAProject.constants.VehicleConstants.NEW_BRAND;
import static com.isap.ISAProject.constants.VehicleConstants.NEW_MODEL;
import static com.isap.ISAProject.constants.VehicleConstants.NEW_PROD_YEAR;
import static com.isap.ISAProject.constants.VehicleConstants.NEW_TYPE;
import static com.isap.ISAProject.constants.VehicleConstants.NEW_SEATS_NUMBER;
import static com.isap.ISAProject.constants.VehicleConstants.PAGE_SIZE;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.repository.rentacar.VehicleRepository;

public class VehicleServiceTest {
	@Mock
	private VehicleRepository repositoryMock;
	
	@Mock
	private Optional<Vehicle> VehicleMock;
	
	@InjectMocks
	private VehicleService service;
	
	@Test
	public void testGetAllVehicles() {
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		when(repositoryMock.findAll(pageRequest))
		.thenReturn(new PageImpl<Vehicle>(Arrays.asList(new Vehicle(DB_ID, DB_PRICE_PER_DAY, DB_DISCOUNT, DB_BRAND, DB_MODEL, DB_PROD_YEAR, DB_TYPE, DB_SEATS_NUMBER))
				.subList(0, 1), pageRequest, 1));
		List<Vehicle> Vehicles = service.getAllVehicles(pageRequest);
		assertThat(Vehicles).hasSize(1);
		verify(repositoryMock, times(1)).findAll(pageRequest);
		verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
	public void testGetVehicleById() {
		when(repositoryMock.findById(DB_ID)).thenReturn(VehicleMock);
		Vehicle dbVehicle = service.getVehicleById(DB_ID);
		assertEquals(VehicleMock.get(), dbVehicle);
		verify(repositoryMock, times(1)).findById(DB_ID);
		verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
    @Transactional
    @Rollback(true) //it can be omitted because it is true by default
	public void testSaveVehicle() {
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new Vehicle(DB_ID, DB_PRICE_PER_DAY, DB_DISCOUNT, DB_BRAND, DB_MODEL, DB_PROD_YEAR, DB_TYPE, DB_SEATS_NUMBER)));
		Vehicle car = new Vehicle();
		car.setPricePerDay(DB_PRICE_PER_DAY);
		car.setDiscount(DB_DISCOUNT);
		car.setBrand(DB_BRAND);
		car.setModel(DB_MODEL);
		car.setProductionYear(DB_PROD_YEAR);
		car.setType(DB_TYPE);
		car.setSeatsNumber(DB_SEATS_NUMBER);
		
		when(repositoryMock.save(car)).thenReturn(car);
		
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		int dbSizeBeforeAdd = service.getAllVehicles(pageRequest).size();
		
		Vehicle dbVehicle = service.saveVehicle(car);
		assertThat(dbVehicle).isNotNull();
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new Vehicle(DB_ID, DB_PRICE_PER_DAY, DB_DISCOUNT
				, DB_BRAND, DB_MODEL, DB_PROD_YEAR, DB_TYPE, DB_SEATS_NUMBER), car));
		// Validate that new Vehicle is in the database
        List<Vehicle> Vehicles = service.getAllVehicles(pageRequest);
        assertThat(Vehicles).hasSize(dbSizeBeforeAdd + 1);
        dbVehicle = Vehicles.get(Vehicles.size() - 1); //get last Vehicle
        assertThat(dbVehicle.getPricePerDay()).isEqualTo(NEW_PRICE_PER_DAY);
        assertThat(dbVehicle.getDiscount()).isEqualTo(NEW_DISCOUNT);
        assertThat(dbVehicle.getBrand()).isEqualTo(NEW_BRAND);
        assertThat(dbVehicle.getModel()).isEqualTo(NEW_MODEL);
        assertThat(dbVehicle.getProductionYear()).isEqualTo(NEW_PROD_YEAR);
        assertThat(dbVehicle.getType()).isEqualTo(NEW_TYPE);
        assertThat(dbVehicle.getSeatsNumber()).isEqualTo(NEW_SEATS_NUMBER);
        verify(repositoryMock, times(2)).findAll();
        verify(repositoryMock, times(1)).save(car);
        verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
    @Transactional
    @Rollback(true)
	public void testUpdateVehicle() {
		
		when(repositoryMock.findById(DB_ID).get()).thenReturn(new Vehicle(DB_ID, DB_PRICE_PER_DAY, DB_DISCOUNT
				, DB_BRAND, DB_MODEL, DB_PROD_YEAR, DB_TYPE, DB_SEATS_NUMBER));
		Vehicle dbCar = service.getVehicleById(DB_ID);
		
		dbCar.setPricePerDay(DB_PRICE_PER_DAY);
		dbCar.setDiscount(DB_DISCOUNT);
		dbCar.setBrand(DB_BRAND);
		dbCar.setModel(DB_MODEL);
		dbCar.setProductionYear(DB_PROD_YEAR);
		dbCar.setType(DB_TYPE);
		dbCar.setSeatsNumber(DB_SEATS_NUMBER);
		
		when(repositoryMock.save(dbCar)).thenReturn(dbCar);
		dbCar = service.saveVehicle(dbCar);
		assertThat(dbCar).isNotNull();
		
		//verify that database contains updated data
		dbCar = service.getVehicleById(DB_ID);
		assertThat(dbCar.getPricePerDay()).isEqualTo(NEW_PRICE_PER_DAY);
        assertThat(dbCar.getDiscount()).isEqualTo(NEW_DISCOUNT);
        assertThat(dbCar.getBrand()).isEqualTo(NEW_BRAND);
        assertThat(dbCar.getModel()).isEqualTo(NEW_MODEL);
        assertThat(dbCar.getProductionYear()).isEqualTo(NEW_PROD_YEAR);
        assertThat(dbCar.getType()).isEqualTo(NEW_TYPE);
        assertThat(dbCar.getSeatsNumber()).isEqualTo(NEW_SEATS_NUMBER);
        verify(repositoryMock, times(2)).findById(DB_ID);
        verify(repositoryMock, times(1)).save(dbCar);
        verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testRemove() {
		
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new Vehicle(DB_ID, DB_PRICE_PER_DAY, DB_DISCOUNT
				, DB_BRAND, DB_MODEL, DB_PROD_YEAR, DB_TYPE, DB_SEATS_NUMBER)));
		int dbSizeBeforeRemove = service.getAllVehicles(pageRequest).size();
		Vehicle dbVehicle = service.getVehicleById(DB_ID_TO_DELETE);
		doNothing().when(repositoryMock).delete(dbVehicle);
		service.deleteVehicle(DB_ID_TO_DELETE);
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new Vehicle(DB_ID, DB_PRICE_PER_DAY, DB_DISCOUNT
				, DB_BRAND, DB_MODEL, DB_PROD_YEAR, DB_TYPE, DB_SEATS_NUMBER)));
		List<Vehicle> Vehicles = service.getAllVehicles(pageRequest);
		assertThat(Vehicles).hasSize(dbSizeBeforeRemove - 1);
		
		when(repositoryMock.findById(DB_ID_TO_DELETE)).thenReturn(null);
		assertThat(dbVehicle).isNull();
		verify(repositoryMock, times(1)).delete(dbVehicle);
		verify(repositoryMock, times(2)).findAll();
        verify(repositoryMock, times(1)).findById(DB_ID_TO_DELETE);
        verifyNoMoreInteractions(repositoryMock);
	}
}
