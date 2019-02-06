package com.isap.ISAProject.repository.rentacar;

import org.springframework.data.jpa.domain.Specification;

import com.isap.ISAProject.model.rentacar.BranchOffice;

public class BranchOfficeSpecifications {
	public static Specification<BranchOffice> withLocationName(String locationName){
		if(locationName == null) {
			return null;
		}
		else {
			return (root, query, cb) -> cb.like(root.join("location").get("name"), "%"+locationName+"%");
		}
	}
	
	public static Specification<BranchOffice> withRentacar(Long rentacar){
		if(rentacar == null) {
			return null;
		}
		else {
			return (root, query, cb) -> cb.equal(root.join("rentACar").get("id"), rentacar);
		}
	}
}
