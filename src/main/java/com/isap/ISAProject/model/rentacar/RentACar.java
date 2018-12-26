package com.isap.ISAProject.model.rentacar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import com.isap.ISAProject.model.Company;

@Entity
@Table(name = "rent_a_car")
public class RentACar extends Company {
	
	@OneToMany(mappedBy="rentACar")
	private List<BranchOffice> branchOffices;
	
	public RentACar() {
		this.branchOffices = new ArrayList<>();
	}

	public List<BranchOffice> getBranchOffices() {
		return branchOffices;
	}
	
	public void addBranchOffice(BranchOffice bo) {
		if(!this.branchOffices.contains(bo))
			this.branchOffices.add(bo);
	}
	
	public void removeBranchOffice(BranchOffice bo) {
		this.branchOffices.remove(bo);
	}
	
	public void copyFieldsFrom(@Valid RentACar newRac) {
		this.setName(newRac.getName());
		this.setAddress(newRac.getAddress());
		this.setDescription(newRac.getDescription());
	}
}
