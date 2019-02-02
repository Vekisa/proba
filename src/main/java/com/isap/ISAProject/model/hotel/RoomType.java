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
@Table(name = "room_type")
public class RoomType {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private double pricePerNight;
	
	@JsonIgnore
	@OneToMany(mappedBy="roomType")
	private List<Room> rooms;
	
	@JsonIgnore
	@ManyToOne
	private Catalogue catalogue;
	
	@JsonIgnore
	@Version
	private Long version;
	
	public RoomType() {
		rooms = new ArrayList<>();
	}

	public Long getId() { return id; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }

	public double getPricePerNight() { return pricePerNight; }

	public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

	public Catalogue getCatalogue() { return catalogue; }

	public void setCatalogue(Catalogue catalogue) { this.catalogue = catalogue; }
	
	public List<Room> getRooms() { return rooms; }

}
