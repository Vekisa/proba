package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.Flight;

public interface DestinationServiceInterface {

	List<Destination> findAll(Pageable pageable);
	
	Destination findById(Long id);
	
	Destination saveDestination(Destination destination);
	
	Destination updateDestination(Destination oldDestination, Destination newDestination);
	
	void deleteDestination(Destination destination);
	
	Flight addFlightToDestination(Flight flight, Destination destination);
	
	List<Flight> getFlightsFromDestination(Destination destination);
	
	List<Flight> getFlightsToDestination(Destination destination);
	
	Airline getAirlineForDestination(Destination destination);
	
}
