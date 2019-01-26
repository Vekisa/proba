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
	
	FlightSeatCategory updateFlightSeatCategory(Long oldCategoryId, FlightSeatCategory newCategory);
	
	void deleteFlightSeatCategory(Long categoryId);
	
	List<FlightSeat> getSeatsInCategory(Long categoryId);
	
	List<FlightSegment> getSegmentsOfCategory(Long categoryId);
	
	Airline getAirlineOfCategory(Long categoryId);
	
}
