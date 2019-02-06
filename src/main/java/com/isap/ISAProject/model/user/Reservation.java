package com.isap.ISAProject.model.user;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
	
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	@ManyToMany
	@JoinTable(name = "confirmed_reservations",
	   joinColumns = { @JoinColumn(name = "reservation_id") },
    inverseJoinColumns = { @JoinColumn(name = "user_id")} )
	private List<RegisteredUser> confirmedUsers;
	
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	@ManyToMany
	@JoinTable(name = "rated_reservations",
	   joinColumns = { @JoinColumn(name = "reservation_d") },
    inverseJoinColumns = { @JoinColumn(name = "user_id")} )
	private List<RegisteredUser> usersThatRated;
	
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	@ManyToMany
	@JoinTable(name = "invited_reservations",
	   joinColumns = { @JoinColumn(name = "reservation_id") },
    inverseJoinColumns = { @JoinColumn(name = "user_id")} )
	private List<RegisteredUser> invitedUsers;
	
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
	
	public List<RegisteredUser> getConfirmedUsers() { return confirmedUsers; }
	
	public List<RegisteredUser> getUsersThatRated() { return usersThatRated; }
	
	public List<RegisteredUser> getInvitedUsers() { return invitedUsers; }

	public Long getId() { return this.id; }
	
}
