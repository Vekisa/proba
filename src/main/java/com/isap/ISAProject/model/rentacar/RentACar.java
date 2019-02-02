package com.isap.ISAProject.model.rentacar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.Company;
import com.isap.ISAProject.model.rating.RentACarRating;
import com.isap.ISAProject.model.user.CompanyAdmin;

@Entity
@Table(name = "rent_a_car")
public class RentACar extends Company {
	
	@OneToMany(mappedBy="rentACar")
	private List<BranchOffice> branchOffices;
	
	@Version
	private Long version;
	
	@JsonIgnore
	@OneToMany(mappedBy = "rentACar")
	private List<CompanyAdmin> admins;
	
	@JsonIgnore
	@OneToMany(mappedBy = "rentACar")
	private List<RentACarRating> ratings;
	
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
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	public List<CompanyAdmin> getAdmins() { return this.admins; }

	@Override
	public String toString() {
		return "RentACar [Id=" + getId() + "]";
	}
}
