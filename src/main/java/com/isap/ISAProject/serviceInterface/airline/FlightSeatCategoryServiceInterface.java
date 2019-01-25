package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.FlightSegment;

public interface FlightSeatCategoryServiceInterface {

	List<FlightSeatCategory> findAll(Pageable pageable);
	
	FlightSeatCategory findById(Long id);
	
	FlightSeatCategory updateFlightSeatCategory(FlightSeatCategory oldCategory, FlightSeatCategory newCategory);
	
	void deleteFlightSeatCategory(FlightSeatCategory category);
	
	List<FlightSeat> getSeatsInCategory(FlightSeatCategory category);
	
	List<FlightSegment> getSegmentsOfCategory(FlightSeatCategory category);
	
	Airline getAirlineOfCategory(FlightSeatCategory category);
	
}
