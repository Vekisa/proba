package com.isap.ISAProject.model.hotel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

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
	
	@ManyToOne
	private Hotel hotel;
	
	@ManyToOne
	private RoomReservation roomReservation;
	
	public ExtraOption() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public RoomReservation getRoomReservation() {
		return roomReservation;
	}

	public void setRoomReservation(RoomReservation roomReservation) {
		this.roomReservation = roomReservation;
	}
	
	public void copyFieldsFrom(@Valid ExtraOption newExtraOption) {
		this.setPricePerDay(newExtraOption.getPricePerDay());
		this.setDescription(newExtraOption.getDescription());
		this.setDiscount(newExtraOption.getDiscount());
	}

}
