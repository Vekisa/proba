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
import javax.validation.Valid;

@Entity
@Table(name = "floor")
public class Floor {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private int number;
	
	@Column(nullable = false)
	private int numberOfRows;
	
	@Column(nullable = false)
	private int numberOfColumns;
	
	@ManyToOne
	private Hotel hotel;
	
	@OneToMany(mappedBy="floor")
	private List<Room> room;
	
	public Floor() {
		room = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	public List<Room> getRoom() {
		return room;
	}

	public void setRoom(List<Room> room) {
		this.room = room;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	
	public void copyFieldsFrom(@Valid Floor newFloor) {
		this.setNumber(newFloor.getNumber());
		this.setNumberOfColumns(newFloor.getNumberOfColumns());
		this.setNumberOfRows(newFloor.getNumberOfRows());
	}
	
	public void add(@Valid Room room) {
		this.getRoom().add(room);
		room.setFloor(this);
	}
}
