package com.isap.ISAProject.model.hotel;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Catalogue {
	
	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@OneToMany(mappedBy = "catalogue")
	private List<Hotel> hotels;
	
	@JsonIgnore
	@OneToMany(mappedBy="catalogue")
	private List<RoomType> roomTypes;
	
	@JsonIgnore
	@Version
	private Long version;
	
	public Catalogue() {
		roomTypes = new ArrayList<>();
		hotels = new ArrayList<>();
	}

	public Long getId() { return id; }

	public void setId(Long id) { this.id = id; }

	public List<Hotel> getHotels() { return this.hotels; }

	public List<RoomType> getRoomTypes() { return roomTypes; }
	
}
