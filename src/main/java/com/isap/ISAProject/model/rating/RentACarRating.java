package com.isap.ISAProject.model.rating;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.model.user.RegisteredUser;

@Entity
public class RentACarRating {

	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@Version
	private Long version;
	
	@JsonIgnore
	@ManyToOne
	private RentACar rentACar;
	
	@JsonIgnore
	@ManyToOne
	private RegisteredUser user;
	
	private int rating;

	public RentACarRating() { }
	
	public RentACarRating(RegisteredUser user, RentACar rentACar) {
		this.user = user;
		this.rentACar = rentACar;
	}

	public RentACar getRentACar() { return rentACar; }

	public void setRentACar(RentACar rentACar) { this.rentACar = rentACar; }

	public RegisteredUser getUser() { return user; }

	public void setUser(RegisteredUser user) { this.user = user; }

	public int getRating() { return rating; }
	
}
