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

@Entity
@Table(name = "flight_configuration")
public class FlightConfiguration {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToMany(mappedBy = "configuration")
	@Cascade(CascadeType.ALL)
	private List<FlightSegment> segments;
	
	@ManyToOne
	private Airline airline;

	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	public void copyFieldsFrom(@Valid FlightConfiguration newConfiguration) {
		// TODO ?
	}

	public List<FlightSegment> getSegments() {
		return this.segments;
	}

	public void add(@Valid FlightSegment flightSegment) {
		// TODO : Izvesti u biznis sloj, kako bi se vršila provera da se segmenti ne poklapaju i da idu jedan za drugim
		this.segments.add(flightSegment);
		flightSegment.setConfiguration(this);
	}

	public Long getId() {
		return this.id;
	}

	public Airline getAirline() {
		return this.airline;
	}
	
}
