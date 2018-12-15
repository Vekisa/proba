package model.airline;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "destination")
public class Destination {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	private Airline airline;
	
	@OneToMany(mappedBy = "destination")
	private List<Transfer> transfers;
	
	@OneToMany(mappedBy = "destination")
	private List<Flight> flights;

	public List<Flight> getFlights() {
		return flights;
	}
	
}
