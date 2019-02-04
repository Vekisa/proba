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
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "flight_seat_category")
public class FlightSeatCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long version;
	
	@Column(nullable = false)
	private double price;

	@Column(nullable = false)
	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "category")
	private List<FlightSegment> segments;

	@JsonIgnore
	@OneToMany(mappedBy = "category")
	private List<FlightSeat> seats;

	@JsonIgnore
	@ManyToOne
	private Airline airline;

	public double getPrice() { return price; }

	public void setPrice(double price) { this.price = price; }

	public Airline getAirline() { return this.airline; }

	public Long getId() { return this.id; }

	public void setName(String name) { this.name = name; }

	public String getName() { return this.name; }

	public void setAirline(Airline airline) { this.airline = airline; }

	public List<FlightSeat> getSeats() { return this.seats; }

	public List<FlightSegment> getSegments() { return this.segments; }

}
