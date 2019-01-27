package com.isap.ISAProject.repository.airline;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.FlightConfiguration;

@Transactional(propagation = Propagation.MANDATORY)
public interface FlightConfigurationRepository extends PagingAndSortingRepository<FlightConfiguration, Long> {

}
