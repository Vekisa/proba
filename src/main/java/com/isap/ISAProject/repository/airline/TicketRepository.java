package com.isap.ISAProject.repository.airline;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Ticket;

@Transactional(propagation = Propagation.MANDATORY)
public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {

	@Query("SELECT s FROM FlightSeat s WHERE s.id = :seatId")
	FlightSeat findSeatById(@Param("seatId") Long seatId);

	@Query("SELECT f FROM Flight f JOIN FETCH f.seats s JOIN FETCH s.ticket t WHERE t.id = :ticketId")
	Flight findFlightForTicketWithId(@Param("ticketId") Long ticketId);

	@Modifying
	@Query(value = "update airline set rating = (select avg(rating) from airline_rating where rating > 0 && airline_id = ?1) where id = ?1", nativeQuery = true)
	void updateAirlineRating(Long airlineId);
	
	@Modifying
	@Query(value = "update flight set rating = (select avg(rating) from flight_rating where rating > 0 && flight_id = ?1) where id = ?1", nativeQuery = true)
	void updateFlightRating(Long airlineId);

}
