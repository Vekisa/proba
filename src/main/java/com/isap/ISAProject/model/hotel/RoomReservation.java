package com.isap.ISAProject.model.hotel;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.Valid;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.user.Reservation;

@Entity
@Table(name = "room_reservation")
public class RoomReservation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private Date beginDate;
	
	@Column(nullable = false)
	private Date endDate;
	
	@Column(nullable = false)
	private int numberOfNights;

	@Column(nullable = false)
	private int numberOfGuests;
	
	@Column(nullable = false)
	private int numberOfRooms;
	
	@Column(nullable = false)
	private double price;
	
	@JsonIgnore
	@ManyToOne
	private Room room;
	
	@JsonIgnore
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	@ManyToMany
	@JoinTable(name = "options_for_rooms",
			   joinColumns = { @JoinColumn(name = "reservation_id") },
			   inverseJoinColumns = { @JoinColumn(name = "option_id")} )
	private List<ExtraOption> extraOptions;
	
	@JsonIgnore
	@OneToOne
	private Reservation reservation;
	
	@JsonIgnore
	@Version
	private Long version;
	
	public RoomReservation() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getNumberOfNights() {
		return numberOfNights;
	}

	public void setNumberOfNights(int numberOfNights) {
		this.numberOfNights = numberOfNights;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public List<ExtraOption> getExtraOptions() {
		return extraOptions;
	}

	public void setExtraOptions(List<ExtraOption> extraOptions) {
		this.extraOptions = extraOptions;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void copyFieldsFrom(@Valid RoomReservation newRoomReservation) {
		this.setNumberOfGuests(newRoomReservation.getNumberOfGuests());
		this.setNumberOfNights(newRoomReservation.getNumberOfNights());
		this.setNumberOfRooms(newRoomReservation.getNumberOfRooms());
		this.setBeginDate(newRoomReservation.getBeginDate());
		this.setEndDate(newRoomReservation.getEndDate());
	}

	public void add(@Valid ExtraOption extraOption) {
		this.getExtraOptions().add(extraOption);
		//extraOption.setRoomReservation(this);
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "RoomReservation [id=" + id + "]";
	}
}
