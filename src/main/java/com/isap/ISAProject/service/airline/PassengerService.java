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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.PassengerRepository;
import com.isap.ISAProject.serviceInterface.airline.PassengerServiceInterface;

@Service
public class PassengerService implements PassengerServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PassengerRepository repository;
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Passenger> findAll(Pageable pageable) {
		logger.info("> fetch passengers at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Passenger> passengers = repository.findAll(pageable);
		logger.info("< passengers fetched");
		return passengers.getContent();
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Passenger findById(Long id) {
		logger.info("> fetch passenger with id {}", id);
		Optional<Passenger> passenger = repository.findById(id);
		logger.info("< passenger fetched");
		if(passenger.isPresent()) return passenger.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested passenger doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void deletePassenger(Long passengerId) {
		logger.info("> deleting passenger with id {}", passengerId);
		repository.deleteById(passengerId);
		logger.info("< passenger deleted");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Passenger updatePassenger(Long oldPassengerId, Passenger newPassenger) {
		logger.info("> updating passenger with id {}", oldPassengerId);
		Passenger oldPassenger = this.findById(oldPassengerId);
		oldPassenger.setFirstName(newPassenger.getFirstName());
		oldPassenger.setLastName(newPassenger.getLastName());
		oldPassenger.setPassportNumber(newPassenger.getPassportNumber());
		repository.save(oldPassenger);
		logger.info("< passenger updated");
		return oldPassenger;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<FlightSeat> getSeatsWithPassenger(Long passengerId) {
		logger.info("> fetching seats for airline with id {}", passengerId);
		Passenger passenger = this.findById(passengerId);
		List<FlightSeat> list = passenger.getFlightSeats();
		logger.info("< seats fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Flight> getFlightsWithPassenger(Long passengerId) {
		logger.info("> fetching seats for airline with id {}", passengerId);
		List<Flight> list = repository.getFlightsForPassenger(passengerId);
		logger.info("< seats fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flights do not exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Ticket> getTicketsOfPassenger(Long passengerId) {
		logger.info("> fetching tickets for airline with id {}", passengerId);
		List<Ticket> list = repository.getTicketsForPassenger(passengerId);
		logger.info("< tickets fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flights do not exist.");
	}

}
