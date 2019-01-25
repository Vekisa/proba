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
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.FlightSeatsRepository;
import com.isap.ISAProject.serviceInterface.airline.FlightSeatServiceInterface;

@Service
public class FlightSeatService implements FlightSeatServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FlightSeatsRepository repository;
	
	@Override
	public List<FlightSeat> findAll(Pageable pageable) {
		logger.info("> fetch seats at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<FlightSeat> seats = repository.findAll(pageable);
		logger.info("< seats fetched");
		return seats.getContent();
	}

	@Override
	public FlightSeat findById(Long id) {
		logger.info("> fetch seat with id {}", id);
		Optional<FlightSeat> seat = repository.findById(id);
		logger.info("< seat fetched");
		if(seat.isPresent()) return seat.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested seat doesn't exist.");
	}

	@Override
	public void deleteSeat(FlightSeat seat) {
		logger.info("> deleting seat with id {}", seat.getId());
		// TODO : Kada je moguce brisati sediste?
		repository.delete(seat);
		logger.info("< seat deleted");
	}

	@Override
	public FlightSeat setLuggageInfoForSeat(FlightSeat seat, Long luggageInfoId) {
		logger.info("> setting luggage info to seat with id {}", seat.getId());
		LuggageInfo luggageInfo = repository.findLuggageInfoWithId(luggageInfoId);
		if(luggageInfo == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested luggage info doesn't exist.");
		setLuggageInfoForSeat(luggageInfo, seat);
		luggageInfo.getSeats().add(seat);
		repository.save(seat);
		logger.info("< luggage info set");
		return seat;
	}
	
	private void setLuggageInfoForSeat(LuggageInfo luggageInfo, FlightSeat seat) {
		if(seat.getLuggageInfo() != null) seat.setPrice(seat.getPrice() - seat.getLuggageInfo().getPrice());
		seat.setLuggageInfo(luggageInfo);
		seat.setPrice(seat.getPrice() + luggageInfo.getPrice());
	}

	@Override
	public LuggageInfo getLuggageInfoOfSeat(FlightSeat seat) {
		logger.info("> fetching luggage info of seat with id {}", seat.getId());
		LuggageInfo luggage = seat.getLuggageInfo();
		logger.info("< luggage info fetched");
		if(luggage != null) return luggage;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requesteq luggage info doesn't exist.");
	}

	@Override
	public FlightSeat setPassengerToSeat(FlightSeat seat, Long passengerId) {
		logger.info("> setting passenger to seat with id {}", seat.getId());
		Passenger passenger = repository.findPassengerById(passengerId);
		if(passenger == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested passenger doesn't exist.");
		seat.setPassenger(passenger);
		passenger.getFlightSeats().add(seat);
		repository.save(seat);
		logger.info("< passenger set");
		return seat;
	}

	@Override
	public Passenger getPassengerOfSeat(FlightSeat seat) {
		logger.info("> fetching passenger of seat with id {}", seat.getId());
		Passenger passenger = seat.getPassenger();
		logger.info("< passenger fetched");
		if(passenger != null) return passenger;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested passenger doesn't exist.");
	}

	@Override
	public FlightSeatCategory getCategoryOfSeat(FlightSeat seat) {
		logger.info("> fetching category of seat with id {}", seat.getId());
		FlightSeatCategory category = seat.getCategory();
		logger.info("< category fetched");
		if(category != null) return category;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested category doesn't exist.");
	}

	@Override
	public Flight getFlightOfSeat(FlightSeat seat) {
		logger.info("> fetching flight of seat with id {}", seat.getId());
		Flight flight = seat.getFlight();
		logger.info("< flight fetched");
		if(flight != null) return flight;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flight doesn't exist.");
	}

	@Override
	public Ticket getTicketOfSeat(FlightSeat seat) {
		logger.info("> fetching ticket of seat with id {}", seat.getId());
		Ticket ticket = seat.getTicket();
		logger.info("< ticket fetched");
		if(ticket != null) return ticket;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested ticket doesn't exist.");
	}

}
