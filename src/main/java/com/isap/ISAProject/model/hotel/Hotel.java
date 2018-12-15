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

import com.isap.ISAProject.model.Company;

@Entity
@Table(name = "hotel")
public class Hotel extends Company {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

}
