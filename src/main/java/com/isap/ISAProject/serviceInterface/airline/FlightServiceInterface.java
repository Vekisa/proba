package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Ticket;

public interface FlightServiceInterface {

	List<Flight> findAll(Pageable pageable);
	
	Flight findById(Long id);
	
	Flight updateFlight(Flight oldFlight, Flight newFlight);
	
	void deleteFlight(Flight flight);
	
	List<FlightSeat> getSeatsForFlight(Flight flight);
	
	Flight addSeatToRowForFlight(int row, Flight flight);
	
	List<Ticket> getTicketsForFlight(Flight flight);
	
	Flight setConfigurationToFlight(Long configurationId, Flight flight);
	
	FlightConfiguration getConfigurationOfFlight(Flight flight);
	
	Flight setFinishDestinationForFlight(Long destinationId, Flight flight);
	
	Destination getStartDestinationOfFlight(Flight flight);
	
	Destination getFinishDestinationOfFlight(Flight flight);
	
}
