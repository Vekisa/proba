package com.isap.ISAProject.model.hotel;

import java.util.ArrayList;
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
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.rating.HotelRating;
import com.isap.ISAProject.model.user.CompanyAdmin;

@Entity
@Table(name = "hotel")
public class Hotel extends Company {
	
	@JsonIgnore
	@OneToMany(mappedBy="hotel")
	@Cascade(CascadeType.ALL)
	private List<Floor> floors;
	
	@JsonIgnore
	@OneToMany(mappedBy="hotel")
	@Cascade(CascadeType.ALL)
	private List<ExtraOption> extraOptions;
	
	@JsonIgnore
	@ManyToOne
	private Catalogue catalogue;
	
	@JsonIgnore
	@ManyToOne
	private Location location;
	
	@JsonIgnore
	@Version
	private Long version;
	
	@JsonIgnore
	@OneToMany(mappedBy = "hotel")
	private List<CompanyAdmin> admins;
	
	@JsonIgnore
	@OneToMany(mappedBy = "hotel")
	private List<HotelRating> ratings;
	
	public Hotel() {
		floors = new ArrayList<>();
		extraOptions = new ArrayList<>();
	}

	public List<Floor> getFloors() { return this.floors; }

	public List<ExtraOption> getExtraOptions() { return this.extraOptions; }
	
	public Catalogue getCatalogue() { return catalogue; }

	public void setCatalogue(Catalogue catalogue) { this.catalogue = catalogue; }

	public void setLocation(Location location) { this.location = location; }
	
	public List<CompanyAdmin> getAdmins() { return this.admins; }

	public Location getLocation() { return this.location; }
}
