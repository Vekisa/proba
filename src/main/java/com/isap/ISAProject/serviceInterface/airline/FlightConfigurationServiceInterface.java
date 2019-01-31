package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSegment;

public interface FlightConfigurationServiceInterface {

	List<FlightConfiguration> findAll(Pageable pageable);
	
	FlightConfiguration findById(Long id);
	
	void deleteConfiguration(Long configurationId);
	
	List<FlightSegment> getSegmentsForConfiguration(Long configurationId);
	
	Airline getAirlineForConfiguration(Long configurationId);

	FlightSegment createSegmentForConfiguration(FlightSegment segment, Long configurationId, Long categoryId);
	
}
