package model.airline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "flight_seat")
public class FlightSeat {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private SeatState state;
	
	@ManyToOne
	private Flight flight;
	
	@ManyToOne
	private FlightSeatCategory category;
	
	@OneToOne
	private Ticket ticket;
	
	public SeatState getState() {
		return state;
	}
	
	public void setState(SeatState state) {
		this.state = state;
	}
	
	public Flight getFlight() {
		return flight;
	}
	
	public void setFlight(Flight flight) {
		this.flight = flight;
	}
	
	public FlightSeatCategory getCategory() {
		return category;
	}
	
	public void setCategory(FlightSeatCategory category) {
		this.category = category;
	}
	
}
