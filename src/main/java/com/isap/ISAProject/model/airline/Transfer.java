package com.isap.ISAProject.model.airline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transfer")
public class Transfer {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	private Flight flight;
	
	@ManyToOne
	private Destination destination;
	
	@Column(nullable = false)
	private double durationOfTransfer;
	
	public void setDurationOfTransfer(double durationOfTransfer) {
		this.durationOfTransfer = durationOfTransfer;
	}
	
	public double getDurationOfTransfer() {
		return durationOfTransfer;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	
}
