package com.isap.ISAProject.model.rentacar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.airline.Location;

@Entity
@Table(name = "branch_office")
public class BranchOffice {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private String address;
	
	@JsonIgnore
	@ManyToOne
	private RentACar rentACar;
	
	@ManyToOne
	private Location location;
	
	@JsonIgnore
	@OneToMany(mappedBy="branchOffice")
	@Cascade(CascadeType.ALL)
	private List<Vehicle> vehicles = new ArrayList<>();
	
	@JsonIgnore
	@Version
	private Long version;
	
	public BranchOffice(Long id, String address) {
		super();
		this.id = id;
		this.address = address;
	}

	public BranchOffice() {}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public RentACar getRentACar() {
		return rentACar;
	}

	public void setRentACar(RentACar rentACar) {
		this.rentACar = rentACar;
	}

	public Long getId() {
		return id;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void addVehicle(Vehicle vehicle) {
		this.vehicles.add(vehicle);
	}
	
	public void removeVehicle(Vehicle vehicle) {
		this.vehicles.remove(vehicle);
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) { this.location = location; }

	@Override
	public String toString() {
		return "BranchOffice [id=" + id + "]";
	}

	public void setId(Long id) {
		this.id = id;
	}
}
