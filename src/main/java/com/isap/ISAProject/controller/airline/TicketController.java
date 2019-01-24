package com.isap.ISAProject.controller.airline;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.SeatState;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.TicketRepository;

@RestController
@RequestMapping("/tickets")
public class TicketController {

	@Autowired
	TicketRepository repository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<Ticket>>> getAllTickets(Pageable pageable) {
		Page<Ticket> tickets = repository.findAll(pageable);
		if(tickets.hasContent()) {
			return new ResponseEntity<List<Resource<Ticket>>>(HATEOASImplementor.createTicketsList(tickets.getContent()), HttpStatus.OK);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Ticket>> getTicketWithId(@PathVariable("id") Long ticketId) {
		Optional<Ticket> ticket = repository.findById(ticketId);
		if(ticket.isPresent()) {
			return new ResponseEntity<Resource<Ticket>>(HATEOASImplementor.createTicket(ticket.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Ticket>> createNewTicket() {
		Ticket ticket = new Ticket();
		repository.save(ticket);
		return new ResponseEntity<Resource<Ticket>>(HATEOASImplementor.createTicket(ticket), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTicketWithId(@PathVariable("id") Long ticketId) {
		if(!repository.findById(ticketId).isPresent()) return ResponseEntity.notFound().build();
		repository.deleteById(ticketId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/seats", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<FlightSeat>> addSeatToTicketWithId(@PathVariable("id") Long ticketId, @RequestParam("seatId") Long seatId) {
		Optional<Ticket> ticket = repository.findById(ticketId);
		if(ticket.isPresent()) {
			FlightSeat seat = repository.findSeatById(seatId);
			if(seat == null) return ResponseEntity.noContent().build();
			if(seat.getState().equals(SeatState.TAKEN)) return ResponseEntity.badRequest().build();
			ticket.get().add(seat);
			repository.save(ticket.get());
			return new ResponseEntity<Resource<FlightSeat>>(HATEOASImplementor.createFlightSeat(seat), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/seats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<FlightSeat>>> getSeatsForTicketWithId(@PathVariable("id") Long ticketId) {
		Optional<Ticket> ticket = repository.findById(ticketId);
		if(ticket.isPresent()) {
			List<FlightSeat> list = ticket.get().getSeats();
			if(list.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementor.createFlightSeatsList(list), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/flight", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Flight>> getFlightForTicketWithId(@PathVariable("id") Long ticketId) {
		Optional<Ticket> ticket = repository.findById(ticketId);
		if(ticket.isPresent()) {
			Flight flight = repository.findFlightForTicketWithId(ticketId);
			if(flight != null) {
				return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(flight), HttpStatus.OK);
			} else {
				return ResponseEntity.noContent().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
