package com.isap.ISAProject.repository.airline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.Ticket;

@Transactional(propagation = Propagation.MANDATORY)
public interface FlightRepository extends PagingAndSortingRepository<Flight, Long>, JpaSpecificationExecutor<Flight> {

	@Query("SELECT t FROM Ticket t INNER JOIN FlightSeat s ON t.id = s.ticket INNER JOIN Flight f ON f.id = s.flight WHERE f.id = :flightId")
	List<Ticket> findTicketsForFlightWithId(@Param("flightId") Long flightId);
	
}
