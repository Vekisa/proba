package com.isap.ISAProject.repository.airline;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Ticket;

public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {

	@Query("SELECT s FROM FlightSeat s WHERE s.id = :seatId")
	FlightSeat findSeatById(@Param("seatId") Long seatId);

	@Query("SELECT f FROM Flight f JOIN FETCH f.seats s JOIN FETCH s.ticket t WHERE t.id = :ticketId")
	Flight findFlightForTicketWithId(@Param("ticketId") Long ticketId);

}
