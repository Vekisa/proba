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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.RatableEntity;
import com.isap.ISAProject.model.rating.VehicleRating;
@Entity
@Table(name = "vehicle")
public class Vehicle extends RatableEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private double pricePerDay;
	
	@Column(nullable = false)
	private double discount;
	
	@Column(nullable = false)
	private String brand;
	
	@Column(nullable = false)
	private String model;
	
	@Column(nullable = false)
	private int productionYear;
	
	@Column(nullable = false)
	private String type;
	
	@Column(nullable = false)
	private int seatsNumber;
	
	@ManyToOne
	private BranchOffice branchOffice;
	
	@JsonIgnore
	@OneToMany(mappedBy="vehicle")
	private List<VehicleReservation> vehicleReservations = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "vehicle")
	private List<VehicleRating> ratings;
	
	@Version
	private Long version;
	
	public double getPricePerDay() {
		return pricePerDay;
	}
	public void setPricePerDay(double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public BranchOffice getBranchOffice() {
		return branchOffice;
	}
	public void setBranchOffice(BranchOffice branchOffice) {
		this.branchOffice = branchOffice;
	}
	public Long getId() {
		return id;
	}
	public List<VehicleReservation> getVehicleReservations() {
		return vehicleReservations;
	}
	
	public void addVehicleReservation(VehicleReservation vr) {
		this.vehicleReservations.add(vr);
	}
	
	public void removeVehicleReservation(VehicleReservation vr) {
		this.vehicleReservations.add(vr);
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getProductionYear() {
		return productionYear;
	}
	public void setProductionYear(int productionYear) {
		this.productionYear = productionYear;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSeatsNumber() {
		return seatsNumber;
	}
	public void setSeatsNumber(int seatsNumber) {
		this.seatsNumber = seatsNumber;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Vehicle [id=" + id + "]";
	}
}
