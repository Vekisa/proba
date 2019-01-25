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
	public Flight findById(Long id) {
		logger.info("> fetch flight with id {}", id);
		Optional<Flight> flight = repository.findById(id);
		logger.info("< flight fetched");
		if(flight.isPresent()) return flight.get();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested flight doesn't exist.");
	}

	@Override
	public Flight updateFlight(Flight oldFlight, Flight newFlight) {
		logger.info("> updating flight with id {}", oldFlight.getId());
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
	public void deleteFlight(Flight flight) {
		logger.info("> deleting flight with id {}", flight.getId());
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
	public List<FlightSeat> getSeatsForFlight(Flight flight) {
		logger.info("> fetching seats for airline with id {}", flight.getId());
		List<FlightSeat> list = flight.getSeats();
		logger.info("< seats fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}

	@Override
	public Flight addSeatToRowForFlight(int row, Flight flight) {
		logger.info("> adding one seat to row {} on flight with id {}", row, flight.getId());
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
	public List<Ticket> getTicketsForFlight(Flight flight) {
		logger.info("> fetching tickets for flight with id {}", flight.getId());
		List<Ticket> list = repository.findTicketsForFlightWithId(flight.getId());
		logger.info("< tickets fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Request tickets do not exist.");
	}

	@Override
	public Flight setConfigurationToFlight(Long configurationId, Flight flight) {
		logger.info("> setting configuration to flight with id {}", flight.getId());
		FlightConfiguration configuration = repository.findConfigurationById(configurationId);
		setConfigurationToFlight(configuration, flight);
		repository.save(flight);
		logger.info("< configuration set");
		return null;
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
	public FlightConfiguration getConfigurationOfFlight(Flight flight) {
		logger.info("> fetching configuration of flight with id {}", flight.getId());
		FlightConfiguration configuration = flight.getConfiguration();
		logger.info("< configuration fetched");
		if(configuration != null) return configuration;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Request configuration doesn't exist.");
	}

	@Override
	public Flight setFinishDestinationForFlight(Long destinationId, Flight flight) {
		logger.info("> setting finish destination for flight with id {}", flight.getId());
		Destination destination = repository.findDestinationById(destinationId);
		flight.setFinishDestination(destination);
		destination.getFlightsToHere().add(flight);
		repository.save(flight);
		logger.info("< finish destination set");
		return flight;
	}

	@Override
	public Destination getStartDestinationOfFlight(Flight flight) {
		logger.info("> fetching starting destination of flight with id {}", flight.getId());
		Destination destination = flight.getStartDestination();
		logger.info("< starting destination fetched");
		if(destination != null) return destination;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested destination not found.");
	}

	@Override
	public Destination getFinishDestinationOfFlight(Flight flight) {
		logger.info("> fetching finish destination of flight with id {}", flight.getId());
		Destination destination = flight.getFinishDestination();
		logger.info("< finish destination fetched");
		if(destination != null) return destination;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested destination not found.");
	}

}
