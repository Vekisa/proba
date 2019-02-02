package com.isap.ISAProject.repository.rentacar;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.isap.ISAProject.model.rentacar.VehicleReservation;

public class VehicleReservationSpecifications {
	public static Specification<VehicleReservation> withBeginDate(Date date){
		if(date == null) {
			return null;
		}
		else {
			return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("beginDate"), date); 
		}
	}
	
	public static Specification<VehicleReservation> withEndDate(Date date){
		if(date == null) {
			return null;
		}
		else {
			return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("endDate"), date); 
		}
	}
	
	public static Specification<VehicleReservation> withBranchOffice(Long id){
		if(id == null) {
			return null;
		}
		else {
			return (root, query, cb) -> cb.equal(root.join("vehicle").join("branchOffice").get("id"), id);
		}
	}
}
