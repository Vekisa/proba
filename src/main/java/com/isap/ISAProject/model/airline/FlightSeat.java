package com.isap.ISAProject.model.airline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "flight_seat")
public class FlightSeat {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SeatState state;

	@Column(nullable = false)
	private int rNumber;

	@Column(nullable = false)
	private int cNumber;

	@Column(nullable = false)
	private double price;

	@JsonIgnore
	@ManyToOne
	private Flight flight;

	@JsonIgnore
	@ManyToOne
	private Ticket ticket;

	@JsonIgnore
	@ManyToOne
	private LuggageInfo luggageInfo;

	@JsonIgnore
	@ManyToOne
	private Passenger passenger;

	@JsonIgnore
	@ManyToOne
	private FlightSeatCategory category;

	public FlightSeat() {}

	public FlightSeat(int rowNumber, int colNumber) {
		this.rNumber = rowNumber;
		this.cNumber = colNumber;
		this.state = SeatState.FREE;
	}

	public SeatState getState() {
		return state;
	}

	public void setState(SeatState state) {
		this.state = state;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LuggageInfo getLuggageInfo() {
		return luggageInfo;
	}

	public void setLuggageInfo(LuggageInfo luggageInfo) {
		if(this.getLuggageInfo() != null) this.price -= this.getLuggageInfo().getPrice();
		this.luggageInfo = luggageInfo;
		this.luggageInfo.addSeat(this);
		this.price += this.getLuggageInfo().getPrice();
	}

	public int getRow() {
		return this.rNumber;
	}

	public int getColumn() {
		return this.cNumber;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
		this.setState(SeatState.TAKEN);
	}

	public Passenger getPassenger() {
		return this.passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	public FlightSeatCategory getCategory() {
		return this.category;
	}

	public Long getId() {
		return this.id;
	}

	public Ticket getTicket() {
		return this.ticket;
	}

	public void setCategory(FlightSeatCategory category) {
		this.category = category;
	}

}
