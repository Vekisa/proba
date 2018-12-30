package com.isap.ISAProject.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.rentacar.VehicleReservation;

@Entity
@Table(name = "reservations")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"beginDate", "endDate"}, 
        allowGetters = true)
public class Reservation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToOne
	private Ticket ticket;
	
	@OneToOne
	private VehicleReservation vehicleReservation;
	
	@OneToOne
	private RoomReservation roomReservation;
	
	@Column(nullable = false)
	private double price;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginDate;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	@ManyToOne
	private RegisteredUser registeredUser;
	
	public Ticket getTicket() {
		return ticket;
	}
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	public VehicleReservation getVehicleReservation() {
		return vehicleReservation;
	}
	public void setVehicleReservation(VehicleReservation vehicleReservation) {
		this.vehicleReservation = vehicleReservation;
	}
	public RoomReservation getRoomReservation() {
		return roomReservation;
	}
	public void setRoomReservation(RoomReservation roomReservation) {
		this.roomReservation = roomReservation;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
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
	public RegisteredUser getRegisteredUser() {
		return registeredUser;
	}
	public void setRegisteredUser(RegisteredUser registeredUser) {
		this.registeredUser = registeredUser;	
	}
	
	public void copyFieldsFrom(@Valid Reservation newReservation) {
		this.setPrice(newReservation.getPrice());
		this.setBeginDate(newReservation.getBeginDate());
		this.setEndDate(newReservation.getEndDate());
	}
}
