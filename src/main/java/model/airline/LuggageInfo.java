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
@Table(name = "luggage_info")
public class LuggageInfo {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private double minWeight;
	
	@Column(nullable = false)
	private double maxWeight;
	
	@Column(nullable = false)
	private double price;
	
	@ManyToOne
	private Airline airline;
	
	@OneToMany(mappedBy = "luggageInfo")
	private List<FlightSeatCategory> categories;
	
	public double getMinWeight() {
		return minWeight;
	}
	
	public void setMinWeight(double minWeight) {
		this.minWeight = minWeight;
	}

	public double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
}
