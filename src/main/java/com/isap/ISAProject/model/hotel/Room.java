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

import com.isap.ISAProject.model.RatableEntity;

@Entity
@Table(name = "room")
public class Room extends RatableEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private int numberOfBeds;
	
	@ManyToOne
	private Floor floor;
	
	@ManyToOne
	private RoomType roomType;
	
	@OneToMany(mappedBy="room")
	private List<RoomReservation> roomReservation;
	
	public Room() {
		roomReservation = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumberOfBeds() {
		return numberOfBeds;
	}

	public void setNumberOfBeds(int numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}


}
