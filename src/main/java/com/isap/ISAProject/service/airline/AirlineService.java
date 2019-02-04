package com.isap.ISAProject.service.airline;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.repository.airline.AirlineRepository;
import com.isap.ISAProject.repository.airline.LocationRepository;
import com.isap.ISAProject.serviceInterface.airline.AirlineServiceInterface;

@Service
public class AirlineService implements AirlineServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AirlineRepository repository;

	@Autowired
	private LocationRepository destinationRepository;

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Airline> findAll(Pageable pageable) {
		logger.info("> fetch airlines at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Airline> airlines = repository.findAll(pageable);
		logger.info("< airlines fetched");
		if(airlines.hasContent()) return airlines.getContent();
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested airlines don't exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Airline findById(Long id) {
		logger.info("> fetch airline with id {}", id);
		Optional<Airline> airline = repository.findById(id);
		logger.info("< airline fetched");
		if(airline.isPresent()) return airline.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested airline doesn't exist.");
	}

	private Location findDestination(Long id) {
		logger.info("> fetching destination with id {}", id);
		Optional<Location> destination = destinationRepository.findById(id);
		logger.info("< destination fetched");
		if(destination.isPresent()) return destination.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested destination doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Airline saveAirline(Airline airline, Long id) {
		logger.info("> saving airline");
		Location location = this.findDestination(id);
		airline.setRating(0);
		airline.setLocation(location);
		location.getAirlines().add(airline);
		destinationRepository.save(location);
		repository.save(airline);
		logger.info("< airline saved");
		return airline;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void deleteAirline(Long airlineId) {
		logger.info("> deleting airline with id {}", airlineId);
		repository.delete(this.findById(airlineId));
		logger.info("< airline deleted");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<LuggageInfo> getLuggageInfosForAirline(Long airlineId) {
		logger.info("> fetching luggage infos for airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		List<LuggageInfo> list = airline.getLuggageInfos();
		logger.info("< luggage infos fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested luggage infos do not exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<FlightConfiguration> getFlightConfigurationsForAirline(Long airlineId) {
		logger.info("> fetching flight configurations for airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		List<FlightConfiguration> list = airline.getConfigurations();
		logger.info("< flight configurations fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flight configurations do not exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<FlightSeatCategory> getFlightSeatCategoriesForAirline(Long airlineId) {
		logger.info("> fetching flight seat categories for airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		List<FlightSeatCategory> list = airline.getCategories();
		logger.info("< flight seat categories fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flight seat categories do not exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Airline changeLocationOfAirline(Long airlineId, Long id) {
		logger.info("> changing location of airline with id {}", airlineId);
		Airline airline = this.findById(airlineId);
		Location destination = this.findDestination(id);
		destination.getAirlines().add(airline);
		airline.setLocation(destination);
		destinationRepository.save(destination);
		logger.info("< location changed");
		return airline;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<CompanyAdmin> getAdminsOfAirline(Long id) {
		logger.info("> fetching admins of airline with id {}", id);
		Airline airline = this.findById(id);
		List<CompanyAdmin> list = airline.getAdmins();
		logger.info("< admins fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested admins do not exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Location getLocationOfAirline(Long id) {
		logger.info("> fetching location of airline with id {}", id);
		Airline airline = this.findById(id);
		Location location = airline.getLocation();
		logger.info("< location fetched");
		if(location != null) return location;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested location doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Flight> getFlightsOfAirline(Long id) {
		logger.info("> fetching flights of airline with id {}", id);
		Airline airline = this.findById(id);
		List<Flight> list = airline.getFlights();
		logger.info("< flights fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flights do not exist.");
	}

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Location> getDestinationsOfAirline(Long id) {
		logger.info("> fetching destinations of airline with id {}", id);
		Airline airline = this.findById(id);
		List<Location> destinations = new ArrayList<>();
		for(Flight f : airline.getFlights())
			if(!destinations.contains(f.getFinishDestination()))
				destinations.add(f.getFinishDestination());
		logger.info("< destinations fetched");
		if(!destinations.isEmpty()) return destinations;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested destinations do not exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Map<Long, Double> getIncomeFor(Long id, Date beginDate, Date endDate) {
		logger.info("> calculating income");
		Airline airline = this.findById(id);
		Map<Long, Double> incomeMap = new HashMap<Long, Double>();
		for(Flight f : airline.getFlights())
			if(f.getDepartureTime().after(beginDate) && f.getDepartureTime().before(endDate)) {
				for(FlightSeat fs : f.getSeats())
					if(fs.isTaken()) {
						if(incomeMap.containsKey(f.getDepartureTime().getTime())) {
							incomeMap.put(f.getDepartureTime().getTime(), incomeMap.get(f.getDepartureTime().getTime()) + fs.getPrice());
						} else {
							incomeMap.put(f.getDepartureTime().getTime(), fs.getPrice());
						}
					}
			}
		logger.info("< income calculated");
		return incomeMap;
	}

}
