package com.isap.ISAProject.model.airline;

import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "flight_configuration")
public class FlightConfiguration {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private int numberOfSegments;
	
	@Transient
	private Vector<Vector<FlightSeat>> seatsMatrix;			// TODO : resiti mapiranje
	
	@ManyToOne
	private Airline airline;
	
	public int getNumberOfSegments() {
		return numberOfSegments;
	}

	public void setNumberOfSegments(int numberOfSegments) {
		this.numberOfSegments = numberOfSegments;
	}

	public Vector<Vector<FlightSeat>> getSeatsMatrix() {
		return seatsMatrix;
	}

	public void setAirline(Airline airline) {
		this.airline = airline;
	}
	
}
