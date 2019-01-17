package com.isap.ISAProject.model.airline;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "flight")
public class Flight {

	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonIgnore
	@ManyToOne
	private Destination startDestination;
	
	@JsonIgnore
	@ManyToOne
	private Destination finishDestination;
	
	@JsonIgnore
	@OneToMany(mappedBy = "flight", orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	private List<FlightSeat> seats;
	
	@JsonIgnore
	@ManyToOne
	private FlightConfiguration configuration;
	
	@Column(nullable = false)
	private String transfers;
	
	@Column(nullable = false)
	private boolean finished;
	
	@Column(nullable = false)
	private Date departureTime;
	
	@Column(nullable = false)
	private Date arrivalTime;
	
	@Column(nullable = false)
	private double flightLength;
	
	@Column(nullable = false)
	private double basePrice;

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getFlightLength() {
		return flightLength;
	}

	public void setFlightLength(double flightLength) {
		this.flightLength = flightLength;
	}

	public double getMinPrice() {
		return basePrice;
	}

	public void setMinPrice(double minPrice) {
		this.basePrice = minPrice;
	}

	public String getTransfers() {
		return transfers;
	}

	public void setTransfers(String transfers) {
		this.transfers = transfers;
	}

	public void setStartDestination(Destination destination) {
		this.startDestination = destination;
	}
	
	public void setFinishDestination(Destination destination) {
		this.finishDestination = destination;
	}
	
	/**
	 * Postavlja konfiguraciju na prosleđenu, kreirajući sva sedišta i dodeljujući im cenu u zavisnsti od pripadnosti segmentu. Ukoliko se konfiguracija briše,
	 * ili menja, briše prethodno kreirana sedišta (u slučaju da nisu rezervisana).
	 * @param configuration
	 */
	public void setConfiguration(FlightConfiguration configuration) {
		if(this.configuration != null) 
			if(!this.checkAndRemoveAllSeats()) return;
		this.configuration = configuration;
		for(FlightSegment fs : configuration.getSegments())
			for(int i = fs.getStartRow(); i <= fs.getEndRow(); i++)
				for(int j = 1; j <= fs.getColumns(); j++) {
					FlightSeat seat = new FlightSeat(i, j);
					seat.setFlight(this);
					seat.setPrice(this.basePrice + fs.getCategory().getPrice());
					seat.setCategory(fs.getCategory());
					this.seats.add(seat);
				}
	}
	
	private boolean checkAndRemoveAllSeats() {
		if(this.noSeatsAreTaken()) {
			this.removeAllSeats();
			return true;
		} else {
			return false;
		}
	}
	
	private void removeAllSeats() {
		for(FlightSeat fs : this.getSeats())
			fs.setFlight(null);
		this.seats.clear();
	}

	private boolean noSeatsAreTaken() {
		for(FlightSeat fs : this.getSeats())
			if(fs.getState().equals(SeatState.TAKEN)) return false;
		return true;
	}

	public  List<FlightSeat> getSeats() {
		return this.seats;
	}

	public void copyFieldsFrom(@Valid Flight newFlight) {
		this.setArrivalTime(newFlight.getArrivalTime());
		this.setDepartureTime(newFlight.getDepartureTime());
		this.setFlightLength(newFlight.getFlightLength());
		this.setMinPrice(newFlight.getMinPrice());
		this.setTransfers(newFlight.getTransfers());
	}

	public void addSeatToRow(int row) {
		List<FlightSeat> seats = this.getSeats();
		for(int i = 0;  i < seats.size(); i++)
			if(seats.get(i+1).getRow() > row)
				seats.add(i, new FlightSeat(i, seats.get(i).getColumn()+1));
	}

	public Long getId() { return this.id; }

	public Destination getStartDestination() {
		return startDestination;
	}

	public Destination getFinishDestination() {
		return finishDestination;
	}

	public FlightConfiguration getConfiguration() {
		return configuration;
	}
	
}
