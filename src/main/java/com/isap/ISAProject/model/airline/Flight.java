package com.isap.ISAProject.model.airline;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "flight")
public class Flight {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	private Destination destination;
	
	@OneToMany(mappedBy = "flight")
	private List<Transfer> transfers;
	
	@OneToMany(mappedBy = "flight")
	private List<Ticket> tickets;
	
	@OneToMany(mappedBy = "flight")
	private List<FlightSeat> seats;
	
	@OneToMany(mappedBy = "flight")
	private List<FlightSeatCategory> categories;
	
	@Column(nullable = false)
	private boolean finished;
	
	@Column(nullable = false)
	private Date departureTime;
	
	@Column(nullable = false)
	private Date arrivalTime;
	
	@Column(nullable = false)
	private double flightLength;
	
	@Column(nullable = false)
	private double minPrice;
	
	public List<Transfer> getTransfers() {
		return transfers;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public List<FlightSeatCategory> getCategories() {
		return categories;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getFlightLength() {
		return flightLength;
	}

	public void setFlightLength(double flightLength) {
		this.flightLength = flightLength;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	
}
