package com.isap.ISAProject.model.airline;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.user.Reservation;

@Entity
@Table(name = "ticket")
public class Ticket {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@Version
	private Long version;
	
	@Column(nullable = false)
	private double price;
	
	@Column(nullable = false)
	private int numberOfSeats;
	
	@JsonIgnore
	@Cascade({CascadeType.MERGE, CascadeType.PERSIST})
	@OneToMany(mappedBy = "ticket")
	private List<FlightSeat> seats;
	
	@JsonIgnore
	@Cascade({CascadeType.MERGE, CascadeType.PERSIST})
	@OneToOne
	private Reservation reservation;

	public Ticket() { this.seats = new ArrayList<>(); }
	
	public double getPrice() { return this.price; }
	
	public List<FlightSeat> getSeats() { return this.seats; }

	public Long getId() { return this.id; }

	public void setPrice(double price) { this.price = price; }
	
	public void setReservation(Reservation reservation) { this.reservation = reservation; }

	public Reservation getReservation() { return this.reservation; }

	public int getNumberOfSeats() { return numberOfSeats; }

	public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }
	
}
