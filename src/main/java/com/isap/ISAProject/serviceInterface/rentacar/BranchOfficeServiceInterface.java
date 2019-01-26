package com.isap.ISAProject.serviceInterface.rentacar;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.Vehicle;

public interface BranchOfficeServiceInterface {

	List<BranchOffice> getAllBranchOffices(Pageable pageable);
	
	BranchOffice getBranchOfficeById(Long id);
	
	BranchOffice saveBranchOffice(BranchOffice branchOffice);
	
	BranchOffice updateBranchOffice(Long id, BranchOffice branchOffice);
	
	void deleteBranchOfficeWithId(Long id);
	
	List<Vehicle> getVehiclesForBranchOfficeWithId(Long id);
	
	Vehicle addVehicleForBranchOfficeWithId(Long id, Vehicle vehicle);
	
	void deleteVehicleForBranchOfficeWithId(Long id, Vehicle vehicle);
}
