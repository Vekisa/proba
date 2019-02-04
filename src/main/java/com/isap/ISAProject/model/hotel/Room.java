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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.RatableEntity;
import com.isap.ISAProject.model.rating.RoomRating;

@Entity
@Table(name = "room")
public class Room extends RatableEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private int numberOfBeds;
	
	@JsonIgnore
	@ManyToOne
	private Floor floor;
	
	@JsonIgnore
	@ManyToOne
	private RoomType roomType;
	
	@JsonIgnore
	@OneToMany(mappedBy="room")
	@Cascade(CascadeType.ALL)
	private List<RoomReservation> roomReservations;
	
	@JsonIgnore
	@Version
	private Long version;
	
	@JsonIgnore
	@OneToMany(mappedBy = "room")
	private List<RoomRating> ratings;
	
	public Room() {
		roomReservations = new ArrayList<>();
	}

	public Long getId() { return id; }

	public int getNumberOfBeds() { return numberOfBeds; }

	public void setNumberOfBeds(int numberOfBeds) { this.numberOfBeds = numberOfBeds; }

	public Floor getFloor() { return floor; }

	public void setFloor(Floor floor) { this.floor = floor; }

	public RoomType getRoomType() { return roomType; }

	public void setRoomType(RoomType roomType) { this.roomType = roomType; }

	public List<RoomReservation> getRoomReservations() { return roomReservations; }

	@Override
	public String toString() {
		return "Room [id=" + id + "]";
	}
	
}
