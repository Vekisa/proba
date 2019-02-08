package com.isap.ISAProject.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "confirmed_reservations")
public class ConfirmedReservation {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@ManyToOne
	private RegisteredUser user;
	
	@ManyToOne
	private Reservation reservation;
	
	@Column(nullable = false)
	private Date confirmationTime;
	
	public ConfirmedReservation() { }
	
	public ConfirmedReservation(RegisteredUser user, Reservation reservation) {
		this.user = user;
		this.reservation = reservation;
		this.confirmationTime = new Date();
	}

	public RegisteredUser getUser() { return user; }

	public Reservation getReservation() { return reservation; }

	public Date getConfirmationTime() { return confirmationTime; }

	public void setReservation(Reservation reservation) { this.reservation = reservation; }
	
}
