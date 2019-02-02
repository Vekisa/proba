package com.isap.ISAProject.model.airline;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.Company;
import com.isap.ISAProject.model.rating.AirlineRating;
import com.isap.ISAProject.model.user.CompanyAdmin;

@Entity
@Table(name = "airline")
public class Airline extends Company {
	
	@JsonIgnore
	@Version
	private Long version;
	
	@JsonIgnore
	@OneToMany(mappedBy = "airline")
	@Cascade(CascadeType.ALL)
	private List<LuggageInfo> luggageInfos;
	
	@JsonIgnore
	@ManyToOne
	private Location location;
	
	@JsonIgnore
	@OneToMany(mappedBy = "airline")
	@Cascade(CascadeType.ALL)
	private List<FlightConfiguration> configurations;
	
	@JsonIgnore
	@OneToMany(mappedBy = "airline")
	@Cascade(CascadeType.ALL)
	private List<FlightSeatCategory> categories;
	
	@JsonIgnore
	@OneToMany(mappedBy = "airline")
	private List<Flight> flights;
	
	@JsonIgnore
	@OneToMany(mappedBy = "airline")
	private List<CompanyAdmin> admins;
	
	@JsonIgnore
	@OneToMany(mappedBy = "airline")
	private List<AirlineRating> ratings;
	
	public List<LuggageInfo> getLuggageInfos() { return luggageInfos; }
	
	public Location getLocation() { return this.location; }

	public List<FlightConfiguration> getConfigurations() { return this.configurations; }

	public List<FlightSeatCategory> getCategories() { return this.categories; }

	public void setLocation(Location location) { this.location = location; }
	
	public List<CompanyAdmin> getAdmins() { return this.admins; }
	
	public List<Flight> getFlights() { return this.flights; }
	
}
