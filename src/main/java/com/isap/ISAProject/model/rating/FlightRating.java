package com.isap.ISAProject.model.rating;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.user.RegisteredUser;

@Entity
public class FlightRating {

	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@Version
	private Long version;
	
	@JsonIgnore
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	@ManyToOne
	private Flight flight;
	
	@JsonIgnore
	@ManyToOne
	private RegisteredUser user;

	@Column(nullable = false)
	private int rating;
	
	public FlightRating() { }
	
	public FlightRating(RegisteredUser user, Flight flight) {
		this.user = user;
		this.flight = flight;
		this.setRating(0);
	}

	public Flight getFlight() { return flight; }

	public void setFlight(Flight flight) { this.flight = flight; }

	public RegisteredUser getUser() { return user; }

	public void setUser(RegisteredUser user) { this.user = user; }
	
	public void setRating(int rating) { this.rating = rating; }
	
}
