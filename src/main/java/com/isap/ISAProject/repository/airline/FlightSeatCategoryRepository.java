package com.isap.ISAProject.repository.airline;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.airline.FlightSeatCategory;

public interface FlightSeatCategoryRepository extends PagingAndSortingRepository<FlightSeatCategory, Long> {

}
