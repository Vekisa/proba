package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSegment;

public interface FlightConfigurationServiceInterface {

	List<FlightConfiguration> findAll(Pageable pageable);
	
	FlightConfiguration findById(Long id);
	
	void deleteConfiguration(FlightConfiguration configuration);
	
	List<FlightSegment> getSegmentsForConfiguration(FlightConfiguration configuration);
	
	FlightSegment createSegmentForConfiguration(FlightSegment segment, FlightConfiguration configuration);
	
	Airline getAirlineForConfiguration(FlightConfiguration configuration);
	
}
