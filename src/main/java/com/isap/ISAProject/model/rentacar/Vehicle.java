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
@Entity
@Table(name = "vehicle")
public class Vehicle {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private double pricePerDay;
	
	@Column(nullable = false)
	private double discount;
	
	@Column(nullable = false)
	private String naziv;
	
	@Column(nullable = false)
	private String marka;
	
	@Column(nullable = false)
	private String model;
	
	@Column(nullable = false)
	private int godProizvodnje;
	
	@Column(nullable = false)
	private String tip;
	
	@Column(nullable = false)
	private int brojSedista;
	
	@ManyToOne
	private BranchOffice branchOffice;
	
	@OneToMany(mappedBy="vehicle")
	private List<VehicleReservation> vehicleReservations = new ArrayList<>();
	
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
	
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getMarka() {
		return marka;
	}
	public void setMarka(String marka) {
		this.marka = marka;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getGodProizvodnje() {
		return godProizvodnje;
	}
	public void setGodProizvodnje(int godProizvodnje) {
		this.godProizvodnje = godProizvodnje;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public int getBrojSedista() {
		return brojSedista;
	}
	public void setBrojSedista(int brojSedista) {
		this.brojSedista = brojSedista;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Vehicle [id=" + id + "]";
	}
}
