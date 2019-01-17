package com.isap.ISAProject.model.airline;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "flight_configuration")
public class FlightConfiguration {

	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@OneToMany(mappedBy = "configuration")
	@Cascade(CascadeType.ALL)
	private List<FlightSegment> segments;
	
	@JsonIgnore
	@ManyToOne
	private Airline airline;

	@JsonIgnore
	@OneToMany(mappedBy = "configuration")
	private List<Flight> flights;
	
	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	public List<FlightSegment> getSegments() {
		return this.segments;
	}

	public boolean add(@Valid FlightSegment flightSegment) {
		for(FlightSegment fs : this.getSegments())
			if(flightSegment.overlapsWith(fs)) return false;
		this.segments.add(flightSegment);
		flightSegment.setConfiguration(this);
		return true;
	}

	public Long getId() {
		return this.id;
	}

	public Airline getAirline() {
		return this.airline;
	}
	
}
