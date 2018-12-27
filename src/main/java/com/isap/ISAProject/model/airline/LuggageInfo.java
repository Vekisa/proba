package com.isap.ISAProject.model.airline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

@Entity
@Table(name = "luggage_info")
public class LuggageInfo {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private double minWeight;
	
	@Column(nullable = false)
	private double maxWeight;
	
	@Column(nullable = false)
	private double price;
	
	@ManyToOne
	private Airline airline;
	
	public double getMinWeight() {
		return minWeight;
	}
	
	public void setMinWeight(double minWeight) {
		this.minWeight = minWeight;
	}

	public double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void copyFieldsFrom(@Valid LuggageInfo newLuggageInfo) {
		this.setMinWeight(newLuggageInfo.getMinWeight());
		this.setMaxWeight(newLuggageInfo.getMaxWeight());
		this.setPrice(newLuggageInfo.getPrice());
	}

	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	public Long getId() {
		return this.id;
	}

	public Airline getAirline() {
		return this.airline;
	}
	
}
