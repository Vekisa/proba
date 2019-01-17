	package com.isap.ISAProject.model.airline;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "passenger")
public class Passenger {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@OneToMany(mappedBy = "passenger")
	private List<FlightSeat> seats;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false, unique = true)
	private Long passportNumber;

	public String getFirstName() { return this.firstName; }

	public void setFirstName(String name) { this.firstName = name; }

	public String getLastName() { return this.lastName; }

	public void setLastName(String name) { this.lastName = name; }

	public Long getPassportNumber() { return this.passportNumber; }

	public void setPassportNumber(Long passportNumber) { this.passportNumber = passportNumber; }

	public void copyFieldsFrom(@Valid Passenger newPassenger) {
		this.setFirstName(newPassenger.getFirstName());
		this.setLastName(newPassenger.getLastName());
		this.setPassportNumber(newPassenger.getPassportNumber());
	}

	public List<FlightSeat> getFlightSeats() { return this.seats; }

	public Long getId() {
		return this.id;
	}

}
