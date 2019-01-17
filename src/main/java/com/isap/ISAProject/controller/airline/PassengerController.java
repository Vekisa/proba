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
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.PassengerRepository;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

	@Autowired
	PassengerRepository repository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<Passenger>>> getAllPassengers(Pageable pageable) {
		Page<Passenger> passengers = repository.findAll(pageable);
		if(passengers.hasContent()) {
			return new ResponseEntity<List<Resource<Passenger>>>(HATEOASImplementor.createPassengersList(passengers.getContent()), HttpStatus.OK);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Passenger>> getPassengerWithId(@PathVariable("id") Long id) {
		Optional<Passenger> passenger = repository.findById(id);
		if(passenger.isPresent()) {
			return new ResponseEntity<Resource<Passenger>>(HATEOASImplementor.createPassenger(passenger.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePassengerWithId(@PathVariable("id") Long id) {
		if(!repository.findById(id).isPresent()) return ResponseEntity.notFound().build();
		repository.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Passenger>> updatePassengerWithId(@PathVariable("id") Long id, @RequestBody @Valid Passenger newPassenger) {
		Optional<Passenger> oldPassenger = repository.findById(id);
		if(oldPassenger.isPresent()) {
			oldPassenger.get().copyFieldsFrom(newPassenger);
			repository.save(oldPassenger.get());
			return new ResponseEntity<Resource<Passenger>>(HATEOASImplementor.createPassenger(oldPassenger.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/seats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<FlightSeat>>> getSeatsForPassengerWithId(@PathVariable("id") Long id) {
		Optional<Passenger> passenger = repository.findById(id);
		if(passenger.isPresent()) {
			List<FlightSeat> seats = passenger.get().getFlightSeats();
			if(seats.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementor.createFlightSeatsList(seats), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/flights", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<Flight>>> getFlightsForPassengerWithId(@PathVariable("id") Long id) {
		if(repository.findById(id).isPresent()) {
			List<Flight> flights = repository.getFlightsForPassenger(id);
			if(flights.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<Flight>>>(HATEOASImplementor.createFlightsList(flights), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<Ticket>>> getTicketsForPassengerWithId(@PathVariable("id") Long id) {
		if(repository.findById(id).isPresent()) {
			List<Ticket> tickets = repository.getTicketsForPassenger(id);
			if(tickets.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<Ticket>>>(HATEOASImplementor.createTicketsList(tickets), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
