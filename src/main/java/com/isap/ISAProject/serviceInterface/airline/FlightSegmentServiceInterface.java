package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.FlightSegment;

public interface FlightSegmentServiceInterface {

	List<FlightSegment> findAll(Pageable pageable);
	
	FlightSegment findById(Long id);
	
	FlightSegment updateSegment(Long oldSegmentId, FlightSegment newSegment);
	
	void deleteSegment(Long segmentId);
	
	FlightConfiguration getConfigurationForSegment(Long segmentId);
	
	FlightSeatCategory getCategoryOfSegment(Long segmentId);
	
}
