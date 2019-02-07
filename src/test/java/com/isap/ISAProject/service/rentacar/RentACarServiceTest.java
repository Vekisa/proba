package com.isap.ISAProject.service.rentacar;

import static com.isap.ISAProject.constants.RentACarConstants.DB_ADDRESS;
import static com.isap.ISAProject.constants.RentACarConstants.DB_DESCRIPTION;
import static com.isap.ISAProject.constants.RentACarConstants.DB_ID;
import static com.isap.ISAProject.constants.RentACarConstants.DB_NAME;
import static com.isap.ISAProject.constants.RentACarConstants.NEW_ADDRESS;
import static com.isap.ISAProject.constants.RentACarConstants.NEW_DESCRIPTION;
import static com.isap.ISAProject.constants.RentACarConstants.NEW_NAME;
import static com.isap.ISAProject.constants.RentACarConstants.PAGE_SIZE;
import static com.isap.ISAProject.constants.RentACarConstants.DB_ID_TO_DELETE;
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

import javax.swing.text.html.Option;

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
import com.isap.ISAProject.repository.rentacar.RentACarRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentACarServiceTest {
	@Mock
	private RentACarRepository repositoryMock;
	
	@Mock
	private Optional<RentACar> rentacarMock;
	
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
		when(repositoryMock.findById(DB_ID)).thenReturn(rentacarMock);
		RentACar dbRentacar = service.getRentACarById(DB_ID);
		assertEquals(rentacarMock.get(), dbRentacar);
		verify(repositoryMock, times(1)).findById(DB_ID);
		verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
    @Transactional
    @Rollback(true) //it can be omitted because it is true by default
	public void testSaveRentacar() {
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION)));
		RentACar car = new RentACar();
		car.setName(DB_NAME);
		car.setAddress(DB_ADDRESS);
		car.setDescription(DB_DESCRIPTION);
		
		when(repositoryMock.save(car)).thenReturn(car);
		
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		int dbSizeBeforeAdd = service.getAllRentACars(pageRequest).size();
		
		RentACar dbRentacar = service.saveRentACar(car);
		assertThat(dbRentacar).isNotNull();
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION), car));
		// Validate that new RentACar is in the database
        List<RentACar> rentacars = service.getAllRentACars(pageRequest);
        assertThat(rentacars).hasSize(dbSizeBeforeAdd + 1);
        dbRentacar = rentacars.get(rentacars.size() - 1); //get last RentACar
        assertThat(dbRentacar.getName()).isEqualTo(NEW_NAME);
        assertThat(dbRentacar.getAddress()).isEqualTo(NEW_ADDRESS);
        assertThat(dbRentacar.getDescription()).isEqualTo(NEW_DESCRIPTION);
        verify(repositoryMock, times(2)).findAll();
        verify(repositoryMock, times(1)).save(car);
        verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
    @Transactional
    @Rollback(true)
	public void testUpdateRentacar() {
		
		when(repositoryMock.findById(DB_ID).get()).thenReturn(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION));
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
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION)));
		int dbSizeBeforeRemove = service.getAllRentACars(pageRequest).size();
		RentACar dbRentACar = service.getRentACarById(DB_ID_TO_DELETE);
		doNothing().when(repositoryMock).delete(dbRentACar);
		service.deleteRentACar(DB_ID_TO_DELETE);
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new RentACar(DB_ID, DB_NAME, DB_ADDRESS, DB_DESCRIPTION)));
		List<RentACar> RentACars = service.getAllRentACars(pageRequest);
		assertThat(RentACars).hasSize(dbSizeBeforeRemove - 1);
		
		when(repositoryMock.findById(DB_ID_TO_DELETE)).thenReturn(null);
		assertThat(dbRentACar).isNull();
		verify(repositoryMock, times(1)).delete(dbRentACar);
		verify(repositoryMock, times(2)).findAll();
        verify(repositoryMock, times(1)).findById(DB_ID_TO_DELETE);
        verifyNoMoreInteractions(repositoryMock);
	}
}
