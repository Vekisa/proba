package com.isap.ISAProject.model.airline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "flight_segment")
public class FlightSegment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private int startRow;
	
	@Column(nullable = false)
	private int endRow;
	
	@ManyToOne
	private FlightConfiguration configuration;
	
	@ManyToOne
	private FlightSeatCategory category;

	public void setConfiguration(FlightConfiguration flightConfiguration) {
		this.configuration = flightConfiguration;
	}
	
}
