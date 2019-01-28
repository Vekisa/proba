package com.isap.ISAProject.repository.airline;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.Airline;

@Transactional(propagation = Propagation.MANDATORY)
public interface AirlineRepository extends PagingAndSortingRepository<Airline, Long>{
	
}
