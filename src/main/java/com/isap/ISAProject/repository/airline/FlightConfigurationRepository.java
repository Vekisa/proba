package com.isap.ISAProject.repository.airline;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.airline.FlightConfiguration;

public interface FlightConfigurationRepository extends PagingAndSortingRepository<FlightConfiguration, Long> {

}
