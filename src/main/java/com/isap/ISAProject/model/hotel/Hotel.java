package com.isap.ISAProject.model.hotel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.Valid;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.Company;
import com.isap.ISAProject.model.airline.Destination;

@Entity
@Table(name = "hotel")
public class Hotel extends Company {
	
	@JsonIgnore
	@OneToMany(mappedBy="hotel")
	@Cascade(CascadeType.ALL)
	private List<Floor> floor;
	
	@JsonIgnore
	@OneToMany(mappedBy="hotel")
	private List<ExtraOption> extraOption;
	
	@JsonIgnore
	@OneToOne
	private Catalogue catalogue;
	
	@JsonIgnore
	@ManyToOne
	private Destination location;
	
	@Version
	private Long version;
	
	public Hotel() {
		floor = new ArrayList<>();
		extraOption = new ArrayList<>();
	}
	
	public Map<Room, Integer> getGraph(Date beginDate, Date endDate) {
	      // TODO: implement
	      return null;
	   }

	public List<Floor> getFloor() {
		return floor;
	}

	public void setFloor(List<Floor> floor) {
		this.floor = floor;
	}

	public List<ExtraOption> getExtraOption() {
		return extraOption;
	}

	public void setExtraOption(List<ExtraOption> extraOption) {
		this.extraOption = extraOption;
	}
	
	public Catalogue getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(Catalogue catalogue) {
		this.catalogue = catalogue;
		catalogue.setHotel(this);
	}

	public void copyFieldsFrom(@Valid Hotel newHotel) {
		this.setName(newHotel.getName());
		this.setAddress(newHotel.getAddress());
		this.setDescription(newHotel.getDescription());
	}
	
	public void add(@Valid Floor floor) {
		this.getFloor().add(floor);
		floor.setHotel(this);
	}
	
	public void add(@Valid ExtraOption extraOption) {
		this.getExtraOption().add(extraOption);
		extraOption.setHotel(this);
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void setLocation(Destination location) { this.location = location; }
}
