package com.isap.ISAProject.serviceInterface.rentacar;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.model.user.CompanyAdmin;

public interface RentACarServiceInterface {
	List<RentACar> getAllRentACars(Pageable pageable);
	
	RentACar getRentACarById(Long id);
	
	RentACar saveRentACar(RentACar rentacar);
	
	RentACar updateRentACar(Long id, RentACar rentacar);
	
	void deleteRentACar(Long id);
	
	List<BranchOffice> getBranchOffices(Long id);
	
	BranchOffice addBranchOffice(Long id, BranchOffice brOff);
	
	void deleteBranchOffice(Long id, BranchOffice brOff);

	List<CompanyAdmin> getAdminsOfRentACar(Long id);
	
	List<RentACar> search(Pageable p, String locationName, String rentacarName, Date beginDate, Date endDate);
}
