package com.isap.ISAProject.model.airline;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.RatableEntity;
import com.isap.ISAProject.model.rating.FlightRating;

@Entity
@Table(name = "flight")
public class Flight extends RatableEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long version;
	
	@ManyToOne
	private Location startDestination;

	@ManyToOne
	private Location finishDestination;

	@JsonIgnore
	@OneToMany(mappedBy = "flight", orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	private List<FlightSeat> seats;

	@Column(nullable = false)
	private String transfers;

	@Column(nullable = false)
	private int numberOfTransfers;

	@Column(nullable = false)
	private Date departureTime;

	@Column(nullable = false)
	private Date arrivalTime;

	@Column(nullable = false)
	private double flightLength;

	@Column(nullable = false)
	private double basePrice;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TripType tripType;
	
	@ManyToOne
	private Airline airline;

	@JsonIgnore
	@OneToMany(mappedBy = "flight")
	List<FlightRating> ratings;

	public Date getDepartureTime() { return departureTime; }

	public void setDepartureTime(Date departureTime) { this.departureTime = departureTime; }

	public Date getArrivalTime() { return arrivalTime; }

	public void setArrivalTime(Date arrivalTime) { this.arrivalTime = arrivalTime; }

	public double getFlightLength() {
		double length = this.getArrivalTime().getTime() - this.getDepartureTime().getTime();
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.valueOf(df.format(length/1000/60/60));
	}

	public void setFlightLength(double flightLength) { this.flightLength = flightLength; }

	public double getBasePrice() { return basePrice; }

	public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

	public String getTransfers() { return transfers; }

	public void setTransfers(String transfers) { this.transfers = transfers; }

	public void setStartDestination(Location destination) { this.startDestination = destination; }

	public void setFinishDestination(Location destination) { this.finishDestination = destination; }

	public  List<FlightSeat> getSeats() { return this.seats; }

	public Long getId() { return this.id; }

	public Location getStartDestination() { return startDestination; }

	public Location getFinishDestination() { return finishDestination; }

	public void setAirline(Airline airline) { this.airline = airline;}
	
	public Airline getAirline() { return this.airline; }
	
	public void setTripType(TripType tripType) { this.tripType = tripType;}
	
	public TripType getTripType() { return this.tripType; }

	@Override
	public String toString() {
		return "Flight [id=" + id + "]";
	}
}
