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

import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.model.airline.SeatState;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.FlightRepository;
import com.isap.ISAProject.serviceInterface.airline.FlightServiceInterface;

@Service
public class FlightService implements FlightServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FlightRepository repository;

	@Override
	public List<Flight> findAll(Pageable pageable) {
		logger.info("> fetch flights at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Flight> flights = repository.findAll(pageable);
		logger.info("< flights fetched");
		return flights.getContent();
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	public Flight findById(Long id) {
		logger.info("> fetch flight with id {}", id);
		Optional<Flight> flight = repository.findById(id);
		logger.info("< flight fetched");
		if(flight.isPresent()) return flight.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested flight doesn't exist.");
	}

	@Override
	public Flight updateFlight(Long oldFlightId, Flight newFlight) {
		logger.info("> updating flight with id {}", oldFlightId);
		Flight oldFlight = this.findById(oldFlightId);
		oldFlight.setArrivalTime(newFlight.getArrivalTime());
		oldFlight.setDepartureTime(newFlight.getDepartureTime());
		oldFlight.setFlightLength(newFlight.getFlightLength());
		oldFlight.setBasePrice(newFlight.getBasePrice());
		oldFlight.setTransfers(newFlight.getTransfers());
		repository.save(oldFlight);
		logger.info("< flight updated");
		return oldFlight;
	}

	@Override
	public void deleteFlight(Long flightId) {
		logger.info("> deleting flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		this.checkAndRemoveAllSeats(flight);
		repository.delete(flight);
		logger.info("< flight deleted");
	}

	private void checkAndRemoveAllSeats(Flight flight) {
		if(this.noSeatsAreTakenForFlight(flight)) {
			this.removeAllSeatsForFlight(flight);
			return;
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some flight seats are taken.");
	}

	private void removeAllSeatsForFlight(Flight flight) {
		for(FlightSeat fs : flight.getSeats()) 
			fs.setFlight(null);
		flight.getSeats().clear();
	}

	private boolean noSeatsAreTakenForFlight(Flight flight) {
		for(FlightSeat fs : flight.getSeats())
			if(fs.getState().equals(SeatState.TAKEN)) return false;
		return true;
	}

	@Override
	public List<FlightSeat> getSeatsForFlight(Long flightId) {
		logger.info("> fetching seats for airline with id {}", flightId);
		Flight flight = this.findById(flightId);
		List<FlightSeat> list = flight.getSeats();
		logger.info("< seats fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}

	@Override
	public Flight addSeatToRowForFlight(int row, Long flightId) {
		logger.info("> adding one seat to row {} on flight with id {}", row, flightId);
		Flight flight = this.findById(flightId);
		this.addSeatToRow(row, flight);
		repository.save(flight);
		logger.info("< seat added");
		return flight;
	}

	private void addSeatToRow(int row, Flight flight) {
		List<FlightSeat> seats = flight.getSeats();
		for(int i = 0;  i < seats.size(); i++)
			if(seats.get(i+1).getRow() > row) {
				seats.add(i, new FlightSeat(i, seats.get(i).getColumn()+1));
				return;
			}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Row number is not valid.");
	}

	@Override
	public List<Ticket> getTicketsForFlight(Long flightId) {
		logger.info("> fetching tickets for flight with id {}", flightId);
		List<Ticket> list = repository.findTicketsForFlightWithId(flightId);
		logger.info("< tickets fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Request tickets do not exist.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public Flight setConfigurationToFlight(Long configurationId, Long flightId) {
		logger.info("> setting configuration to flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		FlightConfiguration configuration = repository.findConfigurationById(configurationId);
		setConfigurationToFlight(configuration, flight);
		repository.save(flight);
		logger.info("< configuration set");
		return flight;
	}

	private void setConfigurationToFlight(FlightConfiguration configuration, Flight flight) {
		if(flight.getConfiguration() != null) checkAndRemoveAllSeats(flight);
		flight.setConfiguration(configuration);
		for(FlightSegment fs : configuration.getSegments())
			for(int i = fs.getStartRow(); i <= fs.getEndRow(); i++)
				for(int j = 1; j <= fs.getColumns(); j++) {
					FlightSeat seat = new FlightSeat(i, j);
					seat.setFlight(flight);
					seat.setPrice(flight.getBasePrice() + fs.getCategory().getPrice());
					seat.setCategory(fs.getCategory());
					flight.getSeats().add(seat);
				}
	}

	@Override
	public FlightConfiguration getConfigurationOfFlight(Long flightId) {
		logger.info("> fetching configuration of flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		FlightConfiguration configuration = flight.getConfiguration();
		logger.info("< configuration fetched");
		if(configuration != null) return configuration;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Request configuration doesn't exist.");
	}

	@Override
	public Flight setFinishDestinationForFlight(Long destinationId, Long flightId) {
		logger.info("> setting finish destination for flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		Destination destination = repository.findDestinationById(destinationId);
		flight.setFinishDestination(destination);
		destination.getFlightsToHere().add(flight);
		repository.save(flight);
		logger.info("< finish destination set");
		return flight;
	}

	@Override
	public Destination getStartDestinationOfFlight(Long flightId) {
		logger.info("> fetching starting destination of flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		Destination destination = flight.getStartDestination();
		logger.info("< starting destination fetched");
		if(destination != null) return destination;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested destination not found.");
	}

	@Override
	public Destination getFinishDestinationOfFlight(Long flightId) {
		logger.info("> fetching finish destination of flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		Destination destination = flight.getFinishDestination();
		logger.info("< finish destination fetched");
		if(destination != null) return destination;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested destination not found.");
	}

}
