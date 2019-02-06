package com.isap.ISAProject.service.airline;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.domain.security.CerberusUser;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.SeatState;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.repository.airline.TicketRepository;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;
import com.isap.ISAProject.repository.user.ReservationRepository;
import com.isap.ISAProject.service.user.RegisteredUserService;
import com.isap.ISAProject.service.user.UserService;
import com.isap.ISAProject.serviceInterface.airline.TicketServiceInterface;

@Service
public class TicketService implements TicketServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TicketRepository repository;
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RegisteredUserService regUserService;
	
	@Autowired
	private RegisteredUserRepository regUserRepository;
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Ticket> findAll(Pageable pageable) {
		logger.info("> fetch tickets at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Ticket> tickets = repository.findAll(pageable);
		logger.info("< tickets fetched");
		return tickets.getContent();
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Ticket findById(Long id) {
		logger.info("> fetch ticket with id {}", id);
		Optional<Ticket> ticket = repository.findById(id);
		logger.info("< ticket fetched");
		if(ticket.isPresent()) return ticket.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested ticket doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Reservation saveTicket(Ticket ticket) {
		logger.info("> saving ticket");
		CerberusUser user = userService.currentUser();
		if(user == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User ne postoji");	
		RegisteredUser regUser = regUserService.findById(user.getId());
		Reservation reservation = new Reservation();
		reservation.setTicket(ticket);
		reservation.setBeginDate(new Date());
		reservation.setEndDate(new Date());
		reservation.setPrice(0);
		repository.save(ticket);
		reservation.getConfirmedUsers().add(regUser);
		reservationRepository.save(reservation);
		regUser.getConfirmedReservations().add(reservation);
		regUserRepository.save(regUser);
		
		logger.info("< ticket saved");
		return reservation;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void deleteTicket(Long ticketId) {
		logger.info("> deleting ticket with id {}", ticketId);
		repository.deleteById(ticketId);
		logger.info("< ticket deleted");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public FlightSeat addSeatToTicket(Long seatId, Long ticketId) {
		logger.info("> adding seat to ticket with id {}", ticketId);
		Ticket ticket = this.findById(ticketId);
		FlightSeat seat = this.findSeatById(seatId);
		if(seat.isTaken()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested seat is taken.");
		ticket.getSeats().add(seat);
		ticket.setPrice(ticket.getPrice() + seat.getPrice());
		ticket.setNumberOfSeats(ticket.getNumberOfSeats() + 1);
		seat.setTicket(ticket);
		seat.setState(SeatState.TAKEN);
		Reservation reservation = ticket.getReservation();
		if(reservation != null)
			reservation.setPrice(reservation.getPrice() + seat.getPrice());
		logger.info("< seat added");
		return seat;
	}
	
	private FlightSeat findSeatById(Long id) {
		logger.info("> fetching seat with id {}", id);
		FlightSeat seat = repository.findSeatById(id);
		logger.info("< fetched seat");
		if(seat != null) return seat;
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested seat doesn't exist.");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public FlightSeat removeSeatFromTicket(Long seatId, Long ticketId) {
		logger.info("> removing seat from ticket with id {}", ticketId);
		Ticket ticket = this.findById(ticketId);
		FlightSeat seat = this.findSeatById(seatId);
		if(!ticket.getSeats().contains(seat)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested seat is not of this ticket.");
		ticket.getSeats().remove(seat);
		ticket.setPrice(ticket.getPrice() - seat.getPrice());
		seat.setTicket(null);
		seat.setState(SeatState.FREE);
		logger.info("< seat removed");
		return seat;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<FlightSeat> getSeatsOfTicket(Long ticketId) {
		logger.info("> fetching seats for airline with id {}", ticketId);
		Ticket ticket = this.findById(ticketId);
		List<FlightSeat> list = ticket.getSeats();
		logger.info("< seats fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Flight getFlightOfTicket(Long ticketId) {
		logger.info("> fetching ticket of seat with id {}", ticketId);
		Flight flight = repository.findFlightForTicketWithId(ticketId);
		logger.info("< ticket fetched");
		if(flight != null) return flight;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Request flight doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public Ticket addMultipleSeatsToTicket(Long ticketId, List<Long> seats) {
		logger.info("> adding multiple seats to ticket with id {}", ticketId);
		for(Long seatId : seats)
			this.addSeatToTicket(seatId, ticketId);
		logger.info("< seats added");
		return this.findById(ticketId);
	}

}
