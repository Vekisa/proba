package com.isap.ISAProject.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.rentacar.VehicleReservation;

@Entity
@Table(name = "reservations")
@EntityListeners(AuditingEntityListener.class)
public class Reservation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Cascade(CascadeType.ALL)
	@OneToOne
	private Ticket ticket;
	
	@Cascade(CascadeType.ALL)
	@OneToOne(orphanRemoval = true)
	private VehicleReservation vehicleReservation;
	
	@Cascade(CascadeType.ALL)
	@OneToOne(orphanRemoval = true)
	private RoomReservation roomReservation;
	
	@Column(nullable = false)
	private double price;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date beginDate;
	
	@Column(nullable = false)
	private Date endDate;
	
	@JsonIgnore
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy = "reservation")
	private List<ConfirmedReservation> confirmedReservations;
	
	@JsonIgnore
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy = "reservation", orphanRemoval = true)
	private List<PendingReservation> pendingReservations;
	
	public Reservation() {
		confirmedReservations = new ArrayList<>();
		pendingReservations = new ArrayList<>();
	}
	
	public Ticket getTicket() { return ticket; }
	
	public void setTicket(Ticket ticket) { this.ticket = ticket; }
	
	public VehicleReservation getVehicleReservation() { return vehicleReservation; }
	
	public void setVehicleReservation(VehicleReservation vehicleReservation) { this.vehicleReservation = vehicleReservation; }
	
	public RoomReservation getRoomReservation() { return roomReservation; }
	
	public void setRoomReservation(RoomReservation roomReservation) { this.roomReservation = roomReservation; }
	
	public double getPrice() { return price; }
	
	public void setPrice(double price) { this.price = price; }
	
	public Date getBeginDate() { return beginDate; }
	
	public void setBeginDate(Date beginDate) { this.beginDate = beginDate; }
	
	public Date getEndDate() { return endDate; }
	
	public void setEndDate(Date endDate) { this.endDate = endDate; }

	public Long getId() { return this.id; }

	public List<ConfirmedReservation> getConfirmedReservations() { return confirmedReservations; }

	public List<PendingReservation> getPendingReservations() { return pendingReservations; }
	
}
