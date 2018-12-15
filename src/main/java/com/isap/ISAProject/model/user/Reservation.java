package com.isap.ISAProject.model.user;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.rentacar.VehicleReservation;

public class Reservation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private Ticket ticket;
	private VehicleReservation vehicleReservation;
	private RoomReservation roomReservation;
	private double price;
	private Date beginDate;
	private Date endDate;
	
	
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
	

}
