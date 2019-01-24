package com.isap.ISAProject.model.airline;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.user.Reservation;

@Entity
@Table(name = "ticket")
public class Ticket {

	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	private double price;
	
	@JsonIgnore
	@OneToMany(mappedBy = "ticket")
	private List<FlightSeat> seats;
	
	@JsonIgnore
	@OneToOne
	private Reservation reservation;

	public double getPrice() { return this.price; }
	
	public List<FlightSeat> getSeats() { return this.seats; }

	public Long getId() { return this.id; }

	public void setPrice(double price) { this.price = price; }
	
}
