package com.isap.ISAProject.repository.rentacar;

import org.springframework.data.jpa.domain.Specification;

import com.isap.ISAProject.model.rentacar.RentACar;

public class RentACarSpecifications {
	public static Specification<RentACar> withName(String name){
		if(name == null) {
			return null;
		}
		else {
			return (root, query, cb) -> cb.equal(root.get("name"), name);
		}
	}
}
