package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Coordinates;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.rentacar.BranchOffice;

public interface LocationServiceInterface {

	List<Location> findAll(Pageable pageable);
	
	Location findById(Long id);
	
	Location saveDestination(Location destination);
	
	Location updateDestination(Long oldDestinationId, Location newDestination);
	
	void deleteDestination(Long destinationId);
	
	Flight addFlightToDestination(Flight flight, Long destinationId);
	
	List<Flight> getFlightsFromDestination(Long destinationId);
	
	List<Flight> getFlightsToDestination(Long destinationId);

	List<Airline> getAirlinesOnLocation(Long id);

	List<BranchOffice> getBranchOfficesOnLocation(Long id);

	List<Hotel> getHotelsOnLocation(Long id);

	Coordinates getCoordinatesForCity(Long id);
	
}
