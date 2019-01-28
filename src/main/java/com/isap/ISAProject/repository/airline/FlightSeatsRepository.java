package com.isap.ISAProject.repository.airline;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.model.airline.Passenger;

@Transactional(propagation = Propagation.MANDATORY)
public interface FlightSeatsRepository extends PagingAndSortingRepository<FlightSeat, Long> {

	@Query("SELECT l FROM LuggageInfo l where l.id = :luggageId")
	LuggageInfo findLuggageInfoWithId(@Param("luggageId") Long luggageId);

	@Query("SELECT p from Passenger p where p.id = :passengerId")
	Passenger findPassengerById(@Param("passengerId") Long passengerId);
	
}
