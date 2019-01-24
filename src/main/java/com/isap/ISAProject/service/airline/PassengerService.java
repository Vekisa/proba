package com.isap.ISAProject.service.airline;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.PassengerRepository;
import com.isap.ISAProject.serviceInterface.airline.PassengerServiceInterface;

public class PassengerService implements PassengerServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PassengerRepository repository;
	
	@Override
	public List<Passenger> findAll(Pageable pageable) {
		logger.info("> fetch passengers at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Passenger> passengers = repository.findAll(pageable);
		logger.info("< passengers fetched");
		return passengers.getContent();
	}

	@Override
	public Passenger findById(Long id) {
		logger.info("> fetch passenger with id {}", id);
		Optional<Passenger> passenger = repository.findById(id);
		logger.info("< passenger fetched");
		if(passenger.isPresent()) return passenger.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested passenger doesn't exist.");
	}

	@Override
	public void deletePassenger(Passenger passenger) {
		logger.info("> deleting passenger with id {}", passenger.getId());
		// TODO : Kada je moguce brisati putnika?
		repository.delete(passenger);
		logger.info("< passenger deleted");
	}

	@Override
	public Passenger updatePassenger(Passenger oldPassenger, Passenger newPassenger) {
		logger.info("> updating passenger with id {}", oldPassenger.getId());
		oldPassenger.setFirstName(newPassenger.getFirstName());
		oldPassenger.setLastName(newPassenger.getLastName());
		oldPassenger.setPassportNumber(newPassenger.getPassportNumber());
		repository.save(oldPassenger);
		logger.info("< passenger updated");
		return oldPassenger;
	}

	@Override
	public List<FlightSeat> getSeatsWithPassenger(Passenger passenger) {
		logger.info("> fetching seats for airline with id {}", passenger.getId());
		List<FlightSeat> list = passenger.getFlightSeats();
		logger.info("< seats fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}

	@Override
	public List<Flight> getFlightsWithPassenger(Passenger passenger) {
		logger.info("> fetching seats for airline with id {}", passenger.getId());
		List<Flight> list = repository.getFlightsForPassenger(passenger.getId());
		logger.info("< seats fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flights do not exist.");
	}

	@Override
	public List<Ticket> getTicketsOfPassenger(Passenger passenger) {
		logger.info("> fetching tickets for airline with id {}", passenger.getId());
		List<Ticket> list = repository.getTicketsForPassenger(passenger.getId());
		logger.info("< tickets fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flights do not exist.");
	}

}
