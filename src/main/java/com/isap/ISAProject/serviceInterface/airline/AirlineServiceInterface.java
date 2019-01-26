package com.isap.ISAProject.serviceInterface.airline;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.LuggageInfo;

public interface AirlineServiceInterface {

	List<Airline> findAll(Pageable pageable);
	
	Airline findById(Long id);
	
	Airline saveAirline(Airline airline);
	
	Airline updateAirline(Long oldAirlineId, Airline newAirline);
	
	void deleteAirline(Long airlineId);
	
	List<LuggageInfo> getLuggageInfosForAirline(Long airlineId);
	
	LuggageInfo addLuggageInfoToAirline(Long airlineId, LuggageInfo luggageInfo);
	
	List<Destination> getDestinationsForAirline(Long airlineId);
	
	Destination addDestinationToAirline(Long airlineId, Destination destination);
	
	List<FlightConfiguration> getFlightConfigurationsForAirline(Long airlineId);
	
	FlightConfiguration addFlightConfigurationToAirline(Long airlineId, FlightConfiguration flightConfiguration);
	
	List<FlightSeatCategory> getFlightSeatCategoriesForAirline(Long airlineId);
	
	FlightSeatCategory addFlightSeatCategoryToAirline(Long airlineId, FlightSeatCategory flightSeatCategory);
	
	Map<Destination, Integer> getGraphForDestinations(Date beginDate, Date endDate);
	
	Map<Flight, Integer> getGraphForFlights(Date beginDate, Date endDate);
	
}
