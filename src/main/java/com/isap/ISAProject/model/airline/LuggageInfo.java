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
@Table(name = "luggage_info")
public class LuggageInfo {
	
	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Version
	private Long version;
	
	@Column(nullable = false)
	private double minWeight;
	
	@Column(nullable = false)
	private double maxWeight;
	
	@Column(nullable = false)
	private double price;
	
	@JsonIgnore
	@ManyToOne
	private Airline airline;
	
	@JsonIgnore
	@OneToMany(mappedBy = "luggageInfo")
	private List<FlightSeat> seats;
	
	public double getMinWeight() { return minWeight; }
	
	public void setMinWeight(double minWeight) { this.minWeight = minWeight; }

	public double getMaxWeight() { return maxWeight; }

	public void setMaxWeight(double maxWeight) { this.maxWeight = maxWeight; }

	public double getPrice() { return price; }

	public void setPrice(double price) { this.price = price; }

	public void setAirline(Airline airline) { this.airline = airline; }

	public Long getId() { return this.id; }

	public Airline getAirline() { return this.airline; }
	
	public List<FlightSeat> getSeats() { return this.seats; }
	
}
