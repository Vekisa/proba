package com.isap.ISAProject.repository.airline;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.airline.FlightSegment;

public interface FlightSegmentRepository extends PagingAndSortingRepository<FlightSegment, Long> {

}
