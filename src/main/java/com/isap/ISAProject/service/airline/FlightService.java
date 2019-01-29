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

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.airline.SeatState;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.AirlineRepository;
import com.isap.ISAProject.repository.airline.FlightConfigurationRepository;
import com.isap.ISAProject.repository.airline.FlightRepository;
import com.isap.ISAProject.repository.airline.LocationRepository;
import com.isap.ISAProject.serviceInterface.airline.FlightServiceInterface;

@Service
public class FlightService implements FlightServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FlightRepository repository;

	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private FlightConfigurationRepository configurationRepository;

	@Autowired
	private AirlineRepository airlineRepository;
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Flight> findAll(Pageable pageable) {
		logger.info("> fetch flights at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Flight> flights = repository.findAll(pageable);
		logger.info("< flights fetched");
		return flights.getContent();
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Flight findById(Long id) {
		logger.info("> fetch flight with id {}", id);
		Optional<Flight> flight = repository.findById(id);
		logger.info("< flight fetched");
		if(flight.isPresent()) return flight.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested flight doesn't exist.");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Flight createFlight(Long airlineId, Flight flight) {
		logger.info("> creating flight for airline with id {}", airlineId);
		Airline airline = this.findAirline(airlineId);
		flight.setAirline(airline);
		flight.setStartDestination(airline.getLocation());
		repository.save(flight);
		logger.info("< created flight");
		return flight;
	}

	private Airline findAirline(Long airlineId) {
		logger.info("> fetching airline with id {}", airlineId);
		Optional<Airline> airline = airlineRepository.findById(airlineId);
		logger.info("< airline fetched");
		if(airline.isPresent()) return airline.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested airline doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<FlightSeat> getSeatsForFlight(Long flightId) {
		logger.info("> fetching seats for airline with id {}", flightId);
		Flight flight = this.findById(flightId);
		List<FlightSeat> list = flight.getSeats();
		logger.info("< seats fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Ticket> getTicketsForFlight(Long flightId) {
		logger.info("> fetching tickets for flight with id {}", flightId);
		List<Ticket> list = repository.findTicketsForFlightWithId(flightId);
		logger.info("< tickets fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested tickets do not exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Flight setConfigurationToFlight(Long configurationId, Long flightId) {
		logger.info("> setting configuration to flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		Optional<FlightConfiguration> configuration = configurationRepository.findById(configurationId);
		if(!configuration.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested configuration doesn't exist.");
		setConfigurationToFlight(configuration.get(), flight);
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
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public FlightConfiguration getConfigurationOfFlight(Long flightId) {
		logger.info("> fetching configuration of flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		FlightConfiguration configuration = flight.getConfiguration();
		logger.info("< configuration fetched");
		if(configuration != null) return configuration;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Request configuration doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Flight setFinishDestinationForFlight(Long destinationId, Long flightId) {
		logger.info("> setting finish destination for flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		Optional<Location> destination = locationRepository.findById(destinationId);
		if(!destination.isPresent()) throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested location doesn't exist.");
		flight.setFinishDestination(destination.get());
		destination.get().getFlightsToHere().add(flight);
		repository.save(flight);
		logger.info("< finish destination set");
		return flight;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Location getStartDestinationOfFlight(Long flightId) {
		logger.info("> fetching starting destination of flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		Location destination = flight.getStartDestination();
		logger.info("< starting destination fetched");
		if(destination != null) return destination;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested destination not found.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Location getFinishDestinationOfFlight(Long flightId) {
		logger.info("> fetching finish destination of flight with id {}", flightId);
		Flight flight = this.findById(flightId);
		Location destination = flight.getFinishDestination();
		logger.info("< finish destination fetched");
		if(destination != null) return destination;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested destination not found.");
	}

}
