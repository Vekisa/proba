package com.isap.ISAProject.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.user.Reservation;

@Transactional(propagation = Propagation.MANDATORY)
public interface ReservationRepository extends PagingAndSortingRepository<Reservation, Long> {

	@Query(value = "select * from isap.flight where id in (select distinct(flight_id) from isap.flight_seat where ticket_id = ?1)", nativeQuery = true)
	public Flight findFlightOfTicketWithId(Long id);
	
}
