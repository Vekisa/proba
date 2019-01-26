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

import com.isap.ISAProject.exception.ResourceNotFoundException;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.repository.airline.DestinationRepository;
import com.isap.ISAProject.serviceInterface.airline.DestinationServiceInterface;

@Service
public class DestinationService implements DestinationServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DestinationRepository repository;

	@Override
	public List<Destination> findAll(Pageable pageable) {
		logger.info("> fetch destinations at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Destination> destinations = repository.findAll(pageable);
		logger.info("< destinations fetched");
		return destinations.getContent();
	}

	@Override
	public Destination findById(Long id) {
		logger.info("> fetch destination with id {}", id);
		Optional<Destination> destination = repository.findById(id);
		logger.info("< destination fetched");
		if(destination.isPresent()) return destination.get();
		throw new ResourceNotFoundException("Destination with ID : " + id + " doesn't exist");
	}

	@Override
	public Destination saveDestination(Destination destination) {
		logger.info("> saving destination");
		repository.save(destination);
		logger.info("< destination saved");
		return destination;
	}

	@Override
	public Destination updateDestination(Long oldDestinationId, Destination newDestination) {
		logger.info("> updating destination with id {}", oldDestinationId);
		Destination oldDestination = this.findById(oldDestinationId);
		oldDestination.setName(newDestination.getName());
		repository.save(oldDestination);
		logger.info("< destination updated");
		return oldDestination;
	}

	@Override
	public void deleteDestination(Long destinationId) {
		logger.info("> deleting destination with id {}", destinationId);
		repository.deleteById(destinationId);
		logger.info("< destination deleted");
	}

	@Override
	public Flight addFlightToDestination(Flight flight, Long destinationId) {
		logger.info("> adding finish destination to flight with id {}", flight.getId());
		Destination destination = this.findById(destinationId);
		destination.getFlightsToHere().add(flight);
		flight.setFinishDestination(destination);
		repository.save(destination);
		logger.info("< finish destination added");
		return flight;
	}

	@Override
	public List<Flight> getFlightsFromDestination(Long destinationId) {
		logger.info("> fetching flights from destination with id {}", destinationId);
		Destination destination = this.findById(destinationId);
		List<Flight> list = destination.getFlightsFromHere();
		logger.info("< flights from destination fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flights do not exist.");
	}

	@Override
	public List<Flight> getFlightsToDestination(Long destinationId) {
		logger.info("> fetching flights to destination with id {}", destinationId);
		Destination destination = this.findById(destinationId);
		List<Flight> list = destination.getFlightsToHere();
		logger.info("< flights to destination fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flights do not exist.");
	}

	@Override
	public Airline getAirlineForDestination(Long destinationId) {
		logger.info("> fetching airline for destination with id {}", destinationId);
		Destination destination = this.findById(destinationId);
		Airline airline = destination.getAirline();
		logger.info("< airline fetched");
		if(airline != null) return airline;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested airline does not exist.");
	}

}
