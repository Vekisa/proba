package com.isap.ISAProject.service.airline;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.repository.airline.AirlineRepository;
import com.isap.ISAProject.repository.airline.DestinationRepository;
import com.isap.ISAProject.serviceInterface.airline.AirlineServiceInterface;

@Service
public class AirlineService implements AirlineServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AirlineRepository repository;

	@Autowired
	private DestinationRepository destinationRepository;
	
	@Override
	public List<Airline> findAll(Pageable pageable) {
		logger.info("> fetch airlines at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Airline> airlines = repository.findAll(pageable);
		logger.info("< airlines fetched");
		return airlines.getContent();
	}

	@Override
	public Airline findById(Long id) {
		logger.info("> fetch airline with id {}", id);
		Optional<Airline> airline = repository.findById(id);
		logger.info("< airline fetched");
		if(airline.isPresent()) return airline.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested airline doesn't exist.");
	}

	private Destination findDestination(Long id) {
		logger.info("> fetching destination with id {}", id);
		Optional<Destination> destination = destinationRepository.findById(id);
		logger.info("< destination fetched");
		if(destination.isPresent()) return destination.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested destination doesn't exist.");
	}
	
	@Override
	public Airline saveAirline(Airline airline, Long id) {
		logger.info("> saving airline");
		Destination location = this.findDestination(id);
		airline.setLocation(location);
		location.getAirlines().add(airline);
		destinationRepository.save(location);
		logger.info("< airline saved");
		return airline;
	}

	@Override
	public Airline updateAirline(Long oldAirlineId, Airline newAirline) {
		logger.info("> updating airline with id {}", oldAirlineId);
		Airline oldAirline = this.findById(oldAirlineId);
		oldAirline.setName(newAirline.getName());
		oldAirline.setAddress(newAirline.getAddress());
		oldAirline.setDescription(newAirline.getDescription());
		repository.save(oldAirline);
		logger.info("< airline updated");
		return oldAirline;
	}

	@Override
	public void deleteAirline(Long airlineId) {
		logger.info("> deleting airline with id {}", airlineId);
		repository.deleteById(airlineId);
		logger.info("< airline deleted");
	}

	@Override
	public List<LuggageInfo> getLuggageInfosForAirline(Long airlineId) {
		logger.info("> fetching luggage infos for airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		List<LuggageInfo> list = airline.getLuggageInfos();
		logger.info("< luggage infos fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested luggage infos do not exist.");
	}

	@Override
	public LuggageInfo addLuggageInfoToAirline(Long airlineId, LuggageInfo luggageInfo) {
		logger.info("> adding luggage info to airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		airline.getLuggageInfos().add(luggageInfo);
		luggageInfo.setAirline(airline);
		repository.save(airline);
		logger.info("< luggage info added");
		return luggageInfo;
	}

	@Override
	public List<Destination> getDestinationsForAirline(Long airlineId) {
		logger.info("> fetching destinations for airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		List<Destination> list = airline.getDestinations();
		logger.info("< destinations fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested destinations do not exist.");
	}

	@Override
	public Destination addDestinationToAirline(Long airlineId, Destination destination) {
		logger.info("> adding destination to airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		airline.getDestinations().add(destination);
		destination.setAirline(airline);
		repository.save(airline);
		logger.info("< destination added");
		return destination;
	}

	@Override
	public List<FlightConfiguration> getFlightConfigurationsForAirline(Long airlineId) {
		logger.info("> fetching flight configurations for airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		List<FlightConfiguration> list = airline.getConfigurations();
		logger.info("< flight configurations fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flight configurations do not exist.");
	}

	@Override
	public FlightConfiguration addFlightConfigurationToAirline(Long airlineId, FlightConfiguration flightConfiguration) {
		logger.info("> adding flight configuration to airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		airline.getConfigurations().add(flightConfiguration);
		flightConfiguration.setAirline(airline);
		repository.save(airline);
		logger.info("< flight configuration added");
		return flightConfiguration;
	}

	@Override
	public List<FlightSeatCategory> getFlightSeatCategoriesForAirline(Long airlineId) {
		logger.info("> fetching flight seat categories for airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		List<FlightSeatCategory> list = airline.getCategories();
		logger.info("< flight seat categories fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flight seat categories do not exist.");
	}

	@Override
	public FlightSeatCategory addFlightSeatCategoryToAirline(Long airlineId, FlightSeatCategory flightSeatCategory) {
		logger.info("> adding flight seat category to airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		airline.getCategories().add(flightSeatCategory);
		flightSeatCategory.setAirline(airline);
		repository.save(airline);
		logger.info("< flight seat categories added");
		return flightSeatCategory;
	}

	@Override
	public Map<Destination, Integer> getGraphForDestinations(Date beginDate, Date endDate) {
		// TODO : implement
		return null;
	}

	@Override
	public Map<Flight, Integer> getGraphForFlights(Date beginDate, Date endDate) {
		// TODO : implement
		return null;
	}

	@Override
	public Airline changeLocationOfAirline(Long airlineId, Long id) {
		logger.info("> changing location of airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		Destination destination = this.findDestination(id);
		destination.getAirlines().add(airline);
		airline.setLocation(destination);
		destinationRepository.save(destination);
		logger.info("< location changed");
		return airline;
	}

}
