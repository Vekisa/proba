package com.isap.ISAProject.controller.airline;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.repository.airline.FlightSeatsRepository;

@RestController
@RequestMapping(value = "/seats")
public class SeatController {

	@Autowired
	FlightSeatsRepository repository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<FlightSeat>>> getAllSeats(Pageable pageable) {
		Page<FlightSeat> seats = repository.findAll(pageable);
		if(seats.hasContent()) {
			return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementor.createFlightSeatsList(seats.getContent()), HttpStatus.OK);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<FlightSeat>> getSeatWithId(@PathVariable("id") Long seatId) {
		Optional<FlightSeat> seat = repository.findById(seatId);
		if(seat.isPresent()) {
			return new ResponseEntity<Resource<FlightSeat>>(HATEOASImplementor.createFlightSeat(seat.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteSeatWithId(@PathVariable("id") Long seatId) {
		if(!repository.findById(seatId).isPresent()) return ResponseEntity.notFound().build();
		repository.deleteById(seatId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/luggageInfo", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<FlightSeat>> setLuggageInfoForSeatWithId(@PathVariable("id") Long seatId, @RequestParam("luggageId") Long luggageId) {
		Optional<FlightSeat> seat = repository.findById(seatId);
		if(seat.isPresent()) {
			LuggageInfo luggage = repository.findLuggageInfoWithId(luggageId);
			if(luggage != null) {
				seat.get().setLuggageInfo(luggage);
				repository.save(seat.get());
				return new ResponseEntity<Resource<FlightSeat>>(HATEOASImplementor.createFlightSeat(seat.get()), HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/luggageInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<LuggageInfo>> getLuggageInfoForSeatWithId(@PathVariable("id") Long seatId) {
		Optional<FlightSeat> seat = repository.findById(seatId);
		if(seat.isPresent()) {
			if(seat.get().getLuggageInfo() != null)
				return new ResponseEntity<Resource<LuggageInfo>>(HATEOASImplementor.createLuggageInfo(seat.get().getLuggageInfo()), HttpStatus.OK);
			else
				return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/passenger", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<FlightSeat>> setPassengerForSeatWithId(@PathVariable("id") Long seatId, @RequestParam("passengerId") Long passengerId) {
		Optional<FlightSeat> seat = repository.findById(seatId);
		if(seat.isPresent()) {
			Passenger passenger = repository.findPassengerById(passengerId);
			if(passenger != null) {
				seat.get().setPassenger(passenger);
				repository.save(seat.get());
				return new ResponseEntity<Resource<FlightSeat>>(HATEOASImplementor.createFlightSeat(seat.get()), HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/passenger", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Passenger>> getPassengerForSeatWithId(@PathVariable("id") Long seatId) {
		Optional<FlightSeat> seat = repository.findById(seatId);
		if(seat.isPresent()) {
			if(seat.get().getPassenger() != null) {
				return new ResponseEntity<Resource<Passenger>>(HATEOASImplementor.createPassenger(seat.get().getPassenger()), HttpStatus.OK);
			} else {
				return ResponseEntity.noContent().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/category", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<FlightSeatCategory>> getCategoryForSeatWithId(@PathVariable("id") Long seatId) {
		Optional<FlightSeat> seat = repository.findById(seatId);
		if(seat.isPresent()) {
			if(seat.get().getCategory() != null) {
				return new ResponseEntity<Resource<FlightSeatCategory>>(HATEOASImplementor.createFlightSeatCategory(seat.get().getCategory()), HttpStatus.OK);
			} else {
				return ResponseEntity.noContent().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/ticket", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Ticket>> getTicketForSeatWithId(@PathVariable("id") Long id) {
		Optional<FlightSeat> seat = repository.findById(id);
		if(seat.isPresent()) {
			Ticket ticket = seat.get().getTicket();
			if(ticket == null) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<Resource<Ticket>>(HATEOASImplementor.createTicket(ticket), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/flight", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Flight>> getFlightForSeatWithId(@PathVariable("id") Long id) {
		Optional<FlightSeat> seat = repository.findById(id);
		if(seat.isPresent()) {
			Flight flight = seat.get().getFlight();
			if(flight == null) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(flight), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
