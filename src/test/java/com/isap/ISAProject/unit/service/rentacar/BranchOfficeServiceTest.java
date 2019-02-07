package com.isap.ISAProject.unit.service.rentacar;

import static com.isap.ISAProject.unit.constants.BranchOfficeConstants.DB_ADDRESS;
import static com.isap.ISAProject.unit.constants.BranchOfficeConstants.DB_ID;
import static com.isap.ISAProject.unit.constants.BranchOfficeConstants.DB_ID_TO_DELETE;
import static com.isap.ISAProject.unit.constants.BranchOfficeConstants.NEW_ADDRESS;
import static com.isap.ISAProject.unit.constants.BranchOfficeConstants.PAGE_SIZE;
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

import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.repository.rentacar.BranchOfficeRepository;
import com.isap.ISAProject.service.rentacar.BranchOfficeService;

public class BranchOfficeServiceTest {
	@Mock
	private BranchOfficeRepository repositoryMock;

	@Mock
	private Optional<BranchOffice> BranchOfficeMock;
	
	@InjectMocks
	private BranchOfficeService service;
	
	@Test
	public void testGetAllBranchOffices() {
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		when(repositoryMock.findAll(pageRequest))
		.thenReturn(new PageImpl<BranchOffice>(Arrays.asList(new BranchOffice(DB_ID, DB_ADDRESS))
				.subList(0, 1), pageRequest, 1));
		List<BranchOffice> BranchOffices = service.getAllBranchOffices(pageRequest);
		assertThat(BranchOffices).hasSize(1);
		verify(repositoryMock, times(1)).findAll(pageRequest);
		verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
	public void testGetBranchOfficeById() {
		when(repositoryMock.findById(DB_ID)).thenReturn(BranchOfficeMock);
		BranchOffice dbBranchOffice = service.getBranchOfficeById(DB_ID);
		assertEquals(BranchOfficeMock.get(), dbBranchOffice);
		verify(repositoryMock, times(1)).findById(DB_ID);
		verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
    @Transactional
    @Rollback(true) //it can be omitted because it is true by default
	public void testSaveBranchOffice() {
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new BranchOffice(DB_ID, DB_ADDRESS)));
		BranchOffice car = new BranchOffice();
		car.setAddress(DB_ADDRESS);
		
		when(repositoryMock.save(car)).thenReturn(car);
		
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		int dbSizeBeforeAdd = service.getAllBranchOffices(pageRequest).size();
		
		BranchOffice dbBranchOffice = service.saveBranchOffice(car);
		assertThat(dbBranchOffice).isNotNull();
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new BranchOffice(DB_ID, DB_ADDRESS), car));
		// Validate that new BranchOffice is in the database
        List<BranchOffice> BranchOffices = service.getAllBranchOffices(pageRequest);
        assertThat(BranchOffices).hasSize(dbSizeBeforeAdd + 1);
        dbBranchOffice = BranchOffices.get(BranchOffices.size() - 1); //get last BranchOffice
        assertThat(dbBranchOffice.getAddress()).isEqualTo(NEW_ADDRESS);
        verify(repositoryMock, times(2)).findAll();
        verify(repositoryMock, times(1)).save(car);
        verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
    @Transactional
    @Rollback(true)
	public void testUpdateBranchOffice() {
		
		when(repositoryMock.findById(DB_ID).get()).thenReturn(new BranchOffice(DB_ID, DB_ADDRESS));
		BranchOffice dbCar = service.getBranchOfficeById(DB_ID);
		
		dbCar.setAddress(NEW_ADDRESS);
		
		when(repositoryMock.save(dbCar)).thenReturn(dbCar);
		dbCar = service.saveBranchOffice(dbCar);
		assertThat(dbCar).isNotNull();
		
		//verify that database contains updated data
		dbCar = service.getBranchOfficeById(DB_ID);
        assertThat(dbCar.getAddress()).isEqualTo(NEW_ADDRESS);
        verify(repositoryMock, times(2)).findById(DB_ID);
        verify(repositoryMock, times(1)).save(dbCar);
        verifyNoMoreInteractions(repositoryMock);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testRemove() {
		
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new BranchOffice(DB_ID, DB_ADDRESS)));
		int dbSizeBeforeRemove = service.getAllBranchOffices(pageRequest).size();
		BranchOffice dbBranchOffice = service.getBranchOfficeById(DB_ID_TO_DELETE);
		doNothing().when(repositoryMock).delete(dbBranchOffice);
		service.deleteBranchOfficeWithId(DB_ID_TO_DELETE);
		
		when(repositoryMock.findAll()).thenReturn(Arrays.asList(new BranchOffice(DB_ID, DB_ADDRESS)));
		List<BranchOffice> BranchOffices = service.getAllBranchOffices(pageRequest);
		assertThat(BranchOffices).hasSize(dbSizeBeforeRemove - 1);
		
		when(repositoryMock.findById(DB_ID_TO_DELETE)).thenReturn(null);
		assertThat(dbBranchOffice).isNull();
		verify(repositoryMock, times(1)).delete(dbBranchOffice);
		verify(repositoryMock, times(2)).findAll();
        verify(repositoryMock, times(1)).findById(DB_ID_TO_DELETE);
        verifyNoMoreInteractions(repositoryMock);
	}
}
