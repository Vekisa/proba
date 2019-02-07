package com.isap.ISAProject.model.hotel;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "floor")
public class Floor {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private int number;
	
	@ManyToOne
	private Hotel hotel;
	
	@JsonIgnore
	@OneToMany(mappedBy="floor")
	private List<Room> rooms;
	
	@JsonIgnore
	@Version
	private Long version;
	
	public Floor() {
		rooms = new ArrayList<>();
	}

	public Long getId() { return id; }

	public int getNumber() { return number; }

	public void setNumber(int number) { this.number = number; }

	public List<Room> getRooms() { return this.rooms; }

	public Hotel getHotel() { return hotel; }

	public void setHotel(Hotel hotel) { this.hotel = hotel; }

}
