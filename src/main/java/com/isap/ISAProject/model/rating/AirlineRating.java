package com.isap.ISAProject.model.rating;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.user.RegisteredUser;

@Entity
public class AirlineRating {

	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@Version
	private Long version;
	
	@JsonIgnore
	@ManyToOne
	private Airline airline;
	
	@JsonIgnore
	@ManyToOne
	private RegisteredUser user;
	
	private int rating;

	public AirlineRating() { }
	
	public AirlineRating(RegisteredUser user, Airline airline) {
		this.user = user;
		this.airline = airline;
	}

	public Airline getAirline() { return airline; }

	public void setAirline(Airline airline) { this.airline = airline; }

	public RegisteredUser getUser() { return user; }

	public void setUser(RegisteredUser user) { this.user = user; }

	public int getRating() { return rating; }
	
}
