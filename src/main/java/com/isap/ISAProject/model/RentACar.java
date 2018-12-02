package com.isap.ISAProject.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "rent_a_car")
public class RentACar extends Company {
	
	@OneToMany(mappedBy="rentACar")
	private List<BranchOffice> branchOffices = new ArrayList<>();

	public List<BranchOffice> getBranchOffices() {
		return branchOffices;
	}
	
	public void addBranchOffice(BranchOffice bo) {
		this.branchOffices.add(bo);
	}
	
	public void removeBranchOffice(BranchOffice bo) {
		this.branchOffices.remove(bo);
	}
}
