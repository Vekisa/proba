package com.isap.ISAProject.model.rating;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.user.RegisteredUser;

@Entity
public class RoomRating {

	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@Version
	private Long version;
	
	@JsonIgnore
	@ManyToOne
	private Room room;
	
	@JsonIgnore
	@ManyToOne
	private RegisteredUser user;

	@Column(nullable = false)
	private int rating;
	
	public RoomRating() { }
	
	public RoomRating(RegisteredUser user, Room room) {
		this.user = user;
		this.room = room;
		this.setRating(0);
	}

	public Room getRoom() { return room; }

	public void setRoom(Room room) { this.room = room; }

	public RegisteredUser getUser() { return user; }

	public void setUser(RegisteredUser user) { this.user = user; }
	
	public void setRating(int rating) { this.rating = rating; }
	
}
