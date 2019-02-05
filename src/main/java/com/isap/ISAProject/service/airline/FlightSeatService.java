package com.isap.ISAProject.service.airline;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.FlightSeatCategoryRepository;
import com.isap.ISAProject.repository.airline.FlightSeatsRepository;
import com.isap.ISAProject.serviceInterface.airline.FlightSeatServiceInterface;

@Service
public class FlightSeatService implements FlightSeatServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FlightSeatsRepository repository;
	
	@Autowired
	private FlightSeatCategoryRepository categoriesRepository;
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<FlightSeat> findAll(Pageable pageable) {
		logger.info("> fetch seats at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<FlightSeat> seats = repository.findAll(pageable);
		logger.info("< seats fetched");
		return seats.getContent();
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public FlightSeat findById(Long id) {
		logger.info("> fetch seat with id {}", id);
		Optional<FlightSeat> seat = repository.findById(id);
		logger.info("< seat fetched");
		if(seat.isPresent()) return seat.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested seat doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void deleteSeat(Long flightSeatId) {
		logger.info("> deleting seat with id {}", flightSeatId);
		FlightSeat seat = this.findById(flightSeatId);
		if(seat.isTaken())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested seat is taken and can't be deleted.");
		repository.delete(seat);
		logger.info("< seat deleted");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public FlightSeat setLuggageInfoForSeat(Long flightSeatId, Long luggageInfoId) {
		logger.info("> setting luggage info to seat with id {}", flightSeatId);
		LuggageInfo luggageInfo = this.findLuggageInfoById(luggageInfoId);
		FlightSeat seat = this.findById(flightSeatId);
		if(!seat.getFlight().getAirline().equals(luggageInfo.getAirline()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Luggage info doesn't belong to the same airline as the flight of this seat.");
		setLuggageInfoForSeat(luggageInfo, seat);
		luggageInfo.getSeats().add(seat);
		repository.save(seat);
		logger.info("< luggage info set");
		return seat;
	}
	
	private LuggageInfo findLuggageInfoById(Long id) {
		logger.info("> fetching luggage info with id {}", id);
		LuggageInfo luggageInfo = repository.findLuggageInfoWithId(id);
		logger.info("< luggage info fetched");
		if(luggageInfo != null) return luggageInfo;
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested luggage info doesn't exist.");
	}
	
	private void setLuggageInfoForSeat(LuggageInfo luggageInfo, FlightSeat seat) {
		if(seat.getLuggageInfo() != null) seat.setPrice(seat.getPrice() - seat.getLuggageInfo().getPrice());
		seat.setLuggageInfo(luggageInfo);
		seat.setPrice(seat.getPrice() + luggageInfo.getPrice());
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public LuggageInfo getLuggageInfoOfSeat(Long seatId) {
		logger.info("> fetching luggage info of seat with id {}", seatId);
		FlightSeat seat = this.findById(seatId);
		LuggageInfo luggage = seat.getLuggageInfo();
		logger.info("< luggage info fetched");
		if(luggage != null) return luggage;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requesteq luggage info doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public FlightSeat setPassengerToSeat(Long seatId, Long passengerId) {
		logger.info("> setting passenger to seat with id {}", seatId);
		Passenger passenger = this.findPassengerById(passengerId);
		FlightSeat seat = this.findById(seatId);
		seat.setPassenger(passenger);
		passenger.getFlightSeats().add(seat);
		repository.save(seat);
		logger.info("< passenger set");
		return seat;
	}
	
	private Passenger findPassengerById(Long id) {
		logger.info("> fetching passenger with id {}", id);
		Passenger passenger = repository.findPassengerById(id);
		logger.info("< passenger fetched");
		if(passenger != null) return passenger; 
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested passenger doesn't exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Passenger getPassengerOfSeat(Long seatId) {
		logger.info("> fetching passenger of seat with id {}", seatId);
		FlightSeat seat = this.findById(seatId);
		Passenger passenger = seat.getPassenger();
		logger.info("< passenger fetched");
		if(passenger != null) return passenger;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested passenger doesn't exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public FlightSeatCategory getCategoryOfSeat(Long seatId) {
		logger.info("> fetching category of seat with id {}", seatId);
		FlightSeat seat = this.findById(seatId);
		FlightSeatCategory category = seat.getCategory();
		logger.info("< category fetched");
		if(category != null) return category;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested category doesn't exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Flight getFlightOfSeat(Long seatId) {
		logger.info("> fetching flight of seat with id {}", seatId);
		FlightSeat seat = this.findById(seatId);
		Flight flight = seat.getFlight();
		logger.info("< flight fetched");
		if(flight != null) return flight;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flight doesn't exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Ticket getTicketOfSeat(Long seatId) {
		logger.info("> fetching ticket of seat with id {}", seatId);
		FlightSeat seat = this.findById(seatId);
		Ticket ticket = seat.getTicket();
		logger.info("< ticket fetched");
		if(ticket != null) return ticket;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested ticket doesn't exist.");
	}

	public FlightSeat setCategoryOfSeat(Long seatId, Long id) {
		logger.info("> setting category of seat with id {}", seatId);
		FlightSeat seat = this.findById(seatId);
		Optional<FlightSeatCategory> category = categoriesRepository.findById(id);
		if(category.isPresent()) {
			seat.setCategory(category.get());
			repository.save(seat);
			return seat;
		}
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested category doesn't exist.");
	}

	public FlightSeat updateFlightSeat(Long id, @Valid FlightSeat newSeat) {
		logger.info("> updating seat with id {}", id);
		FlightSeat oldSeat = this.findById(id);
		if(oldSeat.isTaken())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested seat is taken.");
		oldSeat.setPrice(newSeat.getPrice());
		repository.save(oldSeat);
		logger.info("< seat updated");
		return oldSeat;
	}

}
