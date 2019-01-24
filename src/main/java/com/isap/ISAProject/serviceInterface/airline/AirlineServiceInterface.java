package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.LuggageInfo;

public interface AirlineServiceInterface {

	List<Airline> findAll(Pageable pageable);
	
	Airline findById(Long id);
	
	Airline saveAirline(Airline airline);
	
	Airline updateAirline(Airline oldAirline, Airline newAirline);
	
	void deleteAirline(Airline airline);
	
	List<LuggageInfo> getLuggageInfosForAirline(Airline airline);
	
	LuggageInfo addLuggageInfoToAirline(Airline airline, LuggageInfo luggageInfo);
	
	List<Destination> getDestinationsForAirline(Airline airline);
	
	Destination addDestinationToAirline(Airline airline, Destination destination);
	
	List<FlightConfiguration> getFlightConfigurationsForAirline(Airline airline);
	
	FlightConfiguration addFlightConfigurationToAirline(Airline airline, FlightConfiguration flightConfiguration);
	
	List<FlightSeatCategory> getFlightSeatCategoriesForAirline(Airline airline);
	
	FlightSeatCategory addFlightSeatCategoryToAirline(Airline airline, FlightSeatCategory flightSeatCategory);
	
}
