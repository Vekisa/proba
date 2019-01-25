package com.isap.ISAProject.service.airline;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.SeatState;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.TicketRepository;
import com.isap.ISAProject.serviceInterface.airline.TicketServiceInterface;

@Service
public class TicketService implements TicketServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TicketRepository repository;
	
	@Override
	public List<Ticket> findAll(Pageable pageable) {
		logger.info("> fetch tickets at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Ticket> tickets = repository.findAll(pageable);
		logger.info("< tickets fetched");
		return tickets.getContent();
	}

	@Override
	public Ticket findById(Long id) {
		logger.info("> fetch ticket with id {}", id);
		Optional<Ticket> ticket = repository.findById(id);
		logger.info("< ticket fetched");
		if(ticket.isPresent()) return ticket.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested ticket doesn't exist.");
	}

	@Override
	public Ticket saveTicket(Ticket ticket) {
		logger.info("> saving ticket");
		repository.save(ticket);
		logger.info("< ticket saved");
		return ticket;
	}

	@Override
	public void deleteTicket(Ticket ticket) {
		logger.info("> deleting ticket with id {}", ticket.getId());
		// TODO : Kada je moguce obrisati kartu?
		repository.delete(ticket);
		logger.info("< ticket deleted");
	}

	@Override
	public FlightSeat addSeatToTicket(Long seatId, Ticket ticket) {
		logger.info("> adding seat to ticket with id {}", ticket.getId());
		FlightSeat seat = repository.findSeatById(seatId);
		if(seat == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested seat doesn't exist.");
		if(seat.isTaken()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested seat is taken.");
		ticket.getSeats().add(seat);
		ticket.setPrice(ticket.getPrice() + seat.getPrice());
		seat.setTicket(ticket);
		seat.setState(SeatState.TAKEN);
		logger.info("< seat added");
		return null;
	}
	
	public FlightSeat removeSeatFromTicket(Long seatId, Ticket ticket) {
		logger.info("> removing seat from ticket with id {}", ticket.getId());
		FlightSeat seat = repository.findSeatById(seatId);
		if(seat == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested seat doesn't exist.");
		if(!ticket.getSeats().contains(seat)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested seat is not of this ticket.");
		ticket.getSeats().remove(seat);
		ticket.setPrice(ticket.getPrice() - seat.getPrice());
		seat.setTicket(null);
		seat.setState(SeatState.FREE);
		logger.info("< seat removed");
		return seat;
	}

	@Override
	public List<FlightSeat> getSeatsOfTicket(Ticket ticket) {
		logger.info("> fetching seats for airline with id {}", ticket.getId());
		List<FlightSeat> list = ticket.getSeats();
		logger.info("< seats fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}

	@Override
	public Flight getFlightOfTicket(Ticket ticket) {
		logger.info("> fetching ticket of seat with id {}", ticket.getId());
		Flight flight = repository.findFlightForTicketWithId(ticket.getId());
		logger.info("< ticket fetched");
		if(flight != null) return flight;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Request flight doesn't exist.");
	}

}
