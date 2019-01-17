package com.isap.ISAProject.controller.airline;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.FlightRepository;

@RestController
@RequestMapping("/flights")
public class FlightController {

	@Autowired
	FlightRepository repository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<Flight>>> getAllFlights(Pageable pageable) {
		Page<Flight> flights = repository.findAll(pageable);
		if(flights.hasContent()) {
			return new ResponseEntity<List<Resource<Flight>>>(HATEOASImplementor.createFlightsList(flights.getContent()), HttpStatus.OK);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Flight>> getFlightById(@PathVariable("id") Long flightId) {
		Optional<Flight> flight = repository.findById(flightId);
		if(flight.isPresent()) {
			return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(flight.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
 	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Flight>> updateFlightWithId(@PathVariable("id") Long flightId, @Valid @RequestBody Flight newFlight) {
		Optional<Flight> oldFlight = repository.findById(flightId);
		if(oldFlight.isPresent()) {
			oldFlight.get().copyFieldsFrom(newFlight);
			repository.save(oldFlight.get());
			return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(oldFlight.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// TODO : Samo one gde nijedno sediste nije rezervisano moze brisati
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFlightWithId(@PathVariable("id") Long flightId) {
		if(!repository.findById(flightId).isPresent()) return ResponseEntity.notFound().build();
		repository.deleteById(flightId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Flight>> setFinishDestinationForFlightWithId(@PathVariable("id") Long flightId, @RequestParam("destinationId") Long destinationId) {
		Optional<Flight> flight = repository.findById(flightId);
		if(flight.isPresent()) {
			flight.get().setFinishDestination(repository.findDestinationById(destinationId));
			repository.save(flight.get());
			return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(flight.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/seats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<FlightSeat>>> getFlightSeatsForFlightWithId(@PathVariable("id") Long flightId) {
		Optional<Flight> flight = repository.findById(flightId);
		if(flight.isPresent()) {
			List<FlightSeat> seats = flight.get().getSeats();
			if(seats.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementor.createFlightSeatsList(seats), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/newSeat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Flight>> addSeatToRowForFlightWithId(@PathVariable("id") Long flightId, @RequestParam("row") int row) {
		Optional<Flight> flight = repository.findById(flightId);
		if(flight.isPresent()) {
			flight.get().addSeatToRow(row);
			repository.save(flight.get());
			return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(flight.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<Ticket>>> getTicketsForFlightWithId(@PathVariable("id") Long flightId) {
		Optional<Flight> flight = repository.findById(flightId);
		if(flight.isPresent()) {
			List<Ticket> list = repository.findTicketsForFlightWithId(flightId);
			if(list.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<Ticket>>>(HATEOASImplementor.createTicketsList(list), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/configuration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Flight>> addConfigurationToFlightWithId(@PathVariable("id") Long flightId, @RequestParam("configId") Long configurationId) {
		Optional<Flight> flight = repository.findById(flightId);
		if(flight.isPresent()) {
			FlightConfiguration configuration = repository.findConfigurationById(configurationId);
			flight.get().setConfiguration(configuration);
			repository.save(flight.get());
			return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(flight.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Destination>> getStartDestinationForFlightWithId(@PathVariable("id") Long id) {
		Optional<Flight> flight = repository.findById(id);
		if(flight.isPresent()) {
			Destination start = flight.get().getStartDestination();
			if(start == null) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<Resource<Destination>>(HATEOASImplementor.createDestination(start), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/finish", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Destination>> getFinishDestinationForFlightWithId(@PathVariable("id") Long id) {
		Optional<Flight> flight = repository.findById(id);
		if(flight.isPresent()) {
			Destination finish = flight.get().getStartDestination();
			if(finish == null) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<Resource<Destination>>(HATEOASImplementor.createDestination(finish), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/configuration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<FlightConfiguration>> getFlightConfigurationForFlightWithId(@PathVariable("id") Long id) {
		Optional<Flight> flight = repository.findById(id);
		if(flight.isPresent()) {
			FlightConfiguration configuration = flight.get().getConfiguration();
			if(configuration == null) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<Resource<FlightConfiguration>>(HATEOASImplementor.createFlightConfiguration(configuration), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
