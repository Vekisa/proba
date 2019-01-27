package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Ticket;

public interface TicketServiceInterface {

	List<Ticket> findAll(Pageable pageable);
	
	Ticket findById(Long id);
	
	Ticket saveTicket(Ticket ticket);
	
	void deleteTicket(Long ticketId);
	
	FlightSeat addSeatToTicket(Long seatId, Long ticketId);
	
	FlightSeat removeSeatFromTicket(Long seatId, Long ticketId);
	
	List<FlightSeat> getSeatsOfTicket(Long ticketId);
	
	Flight getFlightOfTicket(Long ticketId);

	Ticket addMultipleSeatsToTicket(Long ticketId, List<Long> seats);
	
}
