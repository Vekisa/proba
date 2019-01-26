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
import javax.validation.Valid;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
	@Cascade(CascadeType.ALL)
	private List<RoomReservation> roomReservation;
	
	@Version
	private Long version;
	
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

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public List<RoomReservation> getRoomReservation() {
		return roomReservation;
	}

	public void setRoomReservation(List<RoomReservation> roomReservation) {
		this.roomReservation = roomReservation;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void copyFieldsFrom(@Valid Room newRoom) {
		this.setNumberOfBeds(newRoom.getNumberOfBeds());
	}
	
	public void add(@Valid RoomReservation roomReservation) {
		this.getRoomReservation().add(roomReservation);
		roomReservation.setRoom(this);
	}
}
