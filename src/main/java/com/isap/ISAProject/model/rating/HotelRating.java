package com.isap.ISAProject.model.rating;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.user.RegisteredUser;

@Entity
public class HotelRating {

	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@Version
	private Long version;
	
	@JsonIgnore
	@ManyToOne
	private Hotel hotel;
	
	@JsonIgnore
	@ManyToOne
	private RegisteredUser user;

	@Column(nullable = false)
	private int rating;
	
	public HotelRating() { }
	
	public HotelRating(RegisteredUser user, Hotel hotel) {
		this.user = user;
		this.hotel = hotel;
		this.setRating(0);
	}

	public Hotel getHotel() { return hotel; }

	public void setHotel(Hotel hotel) { this.hotel = hotel; }

	public RegisteredUser getUser() { return user; }

	public void setUser(RegisteredUser user) { this.user = user; }
	
	public void setRating(int rating) { this.rating = rating; }
	
}
