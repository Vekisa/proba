package com.isap.ISAProject.model.hotel;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.Valid;

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
	
	@ManyToOne
	private Room room;
	
	@OneToMany(mappedBy = "roomReservation")
	private List<ExtraOption> extraOptions;
	
	@OneToOne
	private Reservation reservation;
	
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
		extraOption.setRoomReservation(this);
	}
}
