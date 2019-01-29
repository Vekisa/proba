package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Ticket;

public interface FlightServiceInterface {

	List<Flight> findAll(Pageable pageable);
	
	Flight findById(Long id);
	
	Flight updateFlight(Long oldFlightId, Flight newFlight);
	
	void deleteFlight(Long flightId);
	
	List<FlightSeat> getSeatsForFlight(Long flightId);
	
	Flight addSeatToRowForFlight(int row, Long flightId);
	
	List<Ticket> getTicketsForFlight(Long flightId);
	
	Flight setConfigurationToFlight(Long configurationId, Long flightId);
	
	FlightConfiguration getConfigurationOfFlight(Long flightId);
	
	Flight setFinishDestinationForFlight(Long destinationId, Long flightId);
	
	Location getStartDestinationOfFlight(Long flightId);
	
	Location getFinishDestinationOfFlight(Long flightId);

	Flight createFlight(Long airlineId, Flight flight);
	
}
