package com.isap.ISAProject.repository.airline;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.airline.Airline;

public interface AirlineRepository extends PagingAndSortingRepository<Airline, Long>{
	
}
