package com.isap.ISAProject.model.airline;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.rentacar.BranchOffice;

@Entity
@Table(name = "destination")
public class Location {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@Version
	private Long version;
	
	@Column(nullable = false, unique = true, length = 30)
	private String name;
	
	@JsonIgnore
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	@OneToMany(mappedBy = "startDestination")
	private List<Flight> flightsFromHere;
	
	@JsonIgnore
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	@OneToMany(mappedBy = "finishDestination")
	private List<Flight> flightsToHere;
	
	@JsonIgnore
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	@OneToMany(mappedBy = "location")
	private List<BranchOffice> branchOffices;
	
	@JsonIgnore
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	@OneToMany(mappedBy = "location")
	private List<Hotel> hotels;
	
	@JsonIgnore
	@OneToMany(mappedBy = "location")
	private List<Airline> airlines;
	
	public Location() {}
	
	public List<Flight> getFlightsFromHere() { return this.flightsFromHere; }public Location(Long id, Long version, String name) {
		super();
		this.id = id;
		this.version = version;
		this.name = name;
	}

	public List<Flight> getFlightsToHere() { return this.flightsToHere; }
	
	public String getName() { return this.name; }
	
	public void setName(String name) { this.name = name; }

	public Long getId() { return this.id; }

	public List<Airline> getAirlines() { return this.airlines; }

	public List<BranchOffice> getBranchOffices() { return this.branchOffices; }

	public List<Hotel> getHotels() { return this.hotels; }
	
}
