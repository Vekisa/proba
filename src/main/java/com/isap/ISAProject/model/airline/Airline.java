package com.isap.ISAProject.model.airline;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.isap.ISAProject.model.Company;

@Entity
@Table(name = "airline")
public class Airline extends Company {
	
	@OneToMany(mappedBy = "airline")
	private List<LuggageInfo> luggageInfos;
	
	@OneToMany(mappedBy = "airline")
	private List<Destination> destinations;
	
	@OneToMany(mappedBy = "airline")
	private List<FlightConfiguration> configurations;
	
	public Map<Flight, Integer> getGraphForFlights(Date beginDate, Date endDate) {
		Map<Flight, Integer> result = new HashMap<Flight, Integer>();
		for(Destination d : this.destinations)
			for(Flight f : d.getFlights())
				if((f.getDepartureTime().after(beginDate)) && (f.getArrivalTime().before(endDate)))
					if(result.containsKey(f))
						result.put(f, result.get(f) + 1);
					else
						result.put(f, 1);
		return result;
	}
	
	public Map<Destination, Integer> getGraphForDestinations(Date beginDate, Date endDate) {
		Map<Destination, Integer> result = new HashMap<Destination, Integer>();
		/*for(Destination d : this.destinations) // TODO : Da li je ovo stvarno potrebno?
			if()*/
		return result;
	}
	
	public List<LuggageInfo> getLuggageInfos() {
		return luggageInfos;
	}

	public List<Destination> getDestinations() {
		return destinations;
	}

	public List<FlightConfiguration> getConfigurations() {
		return configurations;
	}
	
}
