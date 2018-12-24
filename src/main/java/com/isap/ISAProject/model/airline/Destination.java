package com.isap.ISAProject.model.airline;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

@Entity
@Table(name = "destination")
public class Destination {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne
	private Airline airline;
	
	@OneToMany(mappedBy = "destination")
	private List<Transfer> transfers;
	
	@OneToMany(mappedBy = "destination")
	private List<Flight> flights;

	public List<Flight> getFlights() {
		return flights;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	public void copyFieldsFrom(@Valid Destination newDestination) {
		this.setName(newDestination.getName());
	}
	
}
