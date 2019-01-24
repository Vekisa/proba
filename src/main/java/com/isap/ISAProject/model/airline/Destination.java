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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "destination")
public class Destination {

	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@JsonIgnore
	@ManyToOne
	private Airline airline;
	
	@JsonIgnore
	@OneToMany(mappedBy = "startDestination", orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	private List<Flight> flightsFromHere;
	
	@JsonIgnore
	@OneToMany(mappedBy = "finishDestination")
	private List<Flight> flightsToHere;

	public List<Flight> getFlightsFromHere() { return this.flightsFromHere; }
	
	public List<Flight> getFlightsToHere() { return this.flightsToHere; }
	
	public String getName() { return this.name; }
	
	public void setName(String name) { this.name = name; }
	
	public void setAirline(Airline airline) { this.airline = airline; }

	public Long getId() { return this.id; }

	public Airline getAirline() { return this.airline; }
	
}
