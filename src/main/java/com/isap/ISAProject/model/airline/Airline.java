package com.isap.ISAProject.model.airline;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.Company;

@Entity
@Table(name = "airline")
public class Airline extends Company {
	
	@JsonIgnore
	@OneToMany(mappedBy = "airline")
	@Cascade(CascadeType.ALL)
	private List<LuggageInfo> luggageInfos;
	
	@JsonIgnore
	@OneToMany(mappedBy = "airline")
	@Cascade(CascadeType.ALL)
	private List<Destination> destinations;
	
	@JsonIgnore
	@OneToMany(mappedBy = "airline")
	@Cascade(CascadeType.ALL)
	private List<FlightConfiguration> configurations;
	
	@JsonIgnore
	@OneToMany(mappedBy = "airline")
	@Cascade(CascadeType.ALL)
	private List<FlightSeatCategory> categories;
	
	public List<LuggageInfo> getLuggageInfos() { return luggageInfos; }

	public List<Destination> getDestinations() { return this.destinations; }

	public List<FlightConfiguration> getConfigurations() { return this.configurations; }

	public List<FlightSeatCategory> getCategories() { return this.categories; }
	
}
