package com.isap.ISAProject.model.airline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ticket")
public class Ticket {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	private Flight flight;
	
	@Column(nullable = false)
	private int discount;
	
	@OneToOne
	private FlightSeat seat;
	
	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public FlightSeat getSeat() {
		return seat;
	}

	public void setSeat(FlightSeat seat) {
		this.seat = seat;
	}

	public double getPrice() {
		// TODO : implement
		return 0;
	}
	
}
