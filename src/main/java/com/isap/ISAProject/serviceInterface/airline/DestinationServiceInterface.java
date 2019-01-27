package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.controller.airline.Coordinates;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.rentacar.BranchOffice;

public interface DestinationServiceInterface {

	List<Destination> findAll(Pageable pageable);
	
	Destination findById(Long id);
	
	Destination saveDestination(Destination destination);
	
	Destination updateDestination(Long oldDestinationId, Destination newDestination);
	
	void deleteDestination(Long destinationId);
	
	Flight addFlightToDestination(Flight flight, Long destinationId);
	
	List<Flight> getFlightsFromDestination(Long destinationId);
	
	List<Flight> getFlightsToDestination(Long destinationId);

	Coordinates getCoordinatesForCity(String city);

	List<Airline> getAirlinesOnLocation(Long id);

	List<BranchOffice> getBranchOfficesOnLocation(Long id);

	List<Hotel> getHotelsOnLocation(Long id);
	
}
