package com.isap.ISAProject.model.hotel;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "extra_option")
public class ExtraOption {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	private double pricePerDay;
	
	@Column
	private String description;
	
	@Column
	private double discount;
	
	@JsonIgnore
	@ManyToOne
	private Hotel hotel;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "extraOptions")
	private List<RoomReservation> roomReservations;
	
	@JsonIgnore
	@Version
	private Long version;
	
	public ExtraOption() { }

	public Long getId() { return id; }

	public double getPricePerDay() { return pricePerDay; }

	public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }

	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }

	public double getDiscount() { return discount; }

	public void setDiscount(double discount) { this.discount = discount; }

	public Hotel getHotel() { return hotel; }

	public void setHotel(Hotel hotel) { this.hotel = hotel; }

	public List<RoomReservation> getRoomReservations() { return this.roomReservations; }

}
