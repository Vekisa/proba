package com.isap.ISAProject.repository.airline;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.airline.Airline;

public interface AirlineRepository extends PagingAndSortingRepository<Airline, Long>{

	/* TODO : Custom Query @Query("SELECT d1 FROM isap.destination d2 WHERE ")
	Page<Destination> getDestinationsForAirline(@Param("pageable") Pageable pageable);*/
	
}
