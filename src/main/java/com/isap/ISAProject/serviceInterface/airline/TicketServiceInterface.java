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
	
	void deleteTicket(Ticket ticket);
	
	FlightSeat addSeatToTicket(Long seatId, Ticket ticket);
	
	FlightSeat removeSeatFromTicket(Long seatId, Ticket ticket);
	
	List<FlightSeat> getSeatsOfTicket(Ticket ticket);
	
	Flight getFlightOfTicket(Ticket ticket);
	
}
