package com.isap.ISAProject.repository.airline;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;

@Transactional(propagation = Propagation.MANDATORY)
public interface PassengerRepository extends PagingAndSortingRepository<Passenger, Long> {

	@Query("SELECT f FROM Flight f JOIN FETCH f.seats s JOIN FETCH s.passenger p WHERE p.id = :id")
	List<Flight> getFlightsForPassenger(@Param("id") Long id);

	@Query("SELECT t FROM Ticket t JOIN FETCH t.seats s JOIN FETCH s.passenger p WHERE p.id = :id")
	List<Ticket> getTicketsForPassenger(@Param("id") Long id);

}
