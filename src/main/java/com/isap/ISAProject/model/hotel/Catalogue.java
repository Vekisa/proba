package com.isap.ISAProject.model.hotel;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.Valid;

@Entity
//@Table(name = "catalogue")
public class Catalogue {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToOne
	private Hotel hotel;
	
	@OneToMany(mappedBy="catalogue")
	private List<RoomType> roomType;
	
	@Version
	private Long version;
	
	public Catalogue() {
		roomType = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public List<RoomType> getRoomType() {
		return roomType;
	}

	public void setRoomType(List<RoomType> roomType) {
		this.roomType = roomType;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void copyFieldsFrom(@Valid Catalogue catalogue) {
		//Proveriti sta raditi
	}
	
	public void add(@Valid RoomType roomType) {
		this.getRoomType().add(roomType);
		roomType.setCatalogue(this);
	}
}
