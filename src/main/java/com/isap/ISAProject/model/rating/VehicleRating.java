package com.isap.ISAProject.model.rating;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.user.RegisteredUser;

@Entity
public class VehicleRating {

	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@Version
	private Long version;
	
	@JsonIgnore
	@ManyToOne
	private Vehicle vehicle;
	
	@JsonIgnore
	@ManyToOne
	private RegisteredUser user;
	
	private int rating;

	public VehicleRating() { }
	
	public VehicleRating(RegisteredUser user, Vehicle vehicle) {
		this.user = user;
		this.vehicle = vehicle;
	}

	public Vehicle getVehicle() { return vehicle; }

	public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

	public RegisteredUser getUser() { return user; }

	public void setUser(RegisteredUser user) { this.user = user; }

	public int getRating() { return rating; }
	
}
