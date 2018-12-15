package model.airline;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "flight_seat_category")
public class FlightSeatCategory {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	private Flight flight;
	
	@Column(nullable = false)
	private double price;
	
	@ManyToOne
	private LuggageInfo luggageInfo;
	
	@OneToMany(mappedBy = "category")
	private List<FlightSeat> seats;
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public LuggageInfo getLuggageInfo() {
		return luggageInfo;
	}
	
	public void setLuggageInfo(LuggageInfo luggageInfo) {
		this.luggageInfo = luggageInfo;
	}
	
}
