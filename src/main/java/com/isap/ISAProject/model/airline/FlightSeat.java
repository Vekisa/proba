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
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "flight_seat")
public class FlightSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long version;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SeatState state;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SeatType type;
	
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

	@ManyToOne
	private LuggageInfo luggageInfo;

	@JsonIgnore
	@ManyToOne
	private Passenger passenger;

	@ManyToOne
	private FlightSeatCategory category;

	public FlightSeat() {}

	public FlightSeat(int rowNumber, int colNumber) {
		this.rNumber = rowNumber;
		this.cNumber = colNumber;
		this.state = SeatState.FREE;
	}

	public SeatState getState() { return state; }

	public void setState(SeatState state) { this.state = state; }

	public Flight getFlight() { return flight; }

	public void setFlight(Flight flight) { this.flight = flight; }

	public double getPrice() { return this.price; }

	public void setPrice(double price) { this.price = price; }

	public LuggageInfo getLuggageInfo() { return luggageInfo; }

	public int getRow() { return this.rNumber; }

	public int getColumn() { return this.cNumber; }

	public void setTicket(Ticket ticket) { this.ticket = ticket; }

	public Passenger getPassenger() { return this.passenger; }

	public void setPassenger(Passenger passenger) { this.passenger = passenger; }

	public FlightSeatCategory getCategory() { return this.category; }

	public Long getId() { return this.id; }

	public Ticket getTicket() { return this.ticket; }

	public void setCategory(FlightSeatCategory category) { this.category = category; }

	public void setLuggageInfo(LuggageInfo luggageInfo) { this.luggageInfo = luggageInfo; }

	public boolean isTaken() { return this.getState().equals(SeatState.TAKEN); }

	public SeatType getType() { return type; }

	public void setType(SeatType type) { this.type = type; }
	
}
