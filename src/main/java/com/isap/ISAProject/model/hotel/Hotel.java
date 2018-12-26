package com.isap.ISAProject.model.hotel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import com.isap.ISAProject.model.Company;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;

@Entity
@Table(name = "hotel")
public class Hotel extends Company {
	
	@OneToMany(mappedBy="hotel")
	private List<Floor> floor;
	
	@OneToMany(mappedBy="hotel")
	private List<ExtraOption> extraOption;
	
	@OneToOne
	private Catalogue catalogue;
	
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
}
