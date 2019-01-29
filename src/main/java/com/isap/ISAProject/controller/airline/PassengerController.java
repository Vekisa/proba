package com.isap.ISAProject.controller.airline;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.isap.ISAProject.service.airline.PassengerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

	@Autowired
	private PassengerService service;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća putnike.", notes = "Povratna vrednost servisa je lista putnika koji pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Passenger>>> getAllPassengers(Pageable pageable) {
		return new ResponseEntity<List<Resource<Passenger>>>(HATEOASImplementorAirline.createPassengersList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća putnika sa ID.", notes = "Povratna vrednost servisa je resurs putnika koji ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Passenger.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Putnik sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<Passenger>> getPassengerWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<Passenger>>(HATEOASImplementorAirline.createPassenger(service.findById(id)), HttpStatus.OK);
	}	

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše putnika.", notes = "Briše putnika sa prosleđenim ID", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Putnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deletePassengerWithId(@PathVariable("id") Long id) {
		service.deletePassenger(id);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira putnika.", notes = "Ažurira putnika sa prosleđenim ID na osnovu prosleđenog putnika. Kolekcije originalnog putnika ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Passenger.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili avio kompaniju nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Passenger>> updatePassengerWithId(@PathVariable("id") Long id, @RequestBody @Valid Passenger newPassenger) {
		return new ResponseEntity<Resource<Passenger>>(HATEOASImplementorAirline.createPassenger(service.updatePassenger(id, newPassenger)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/seats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sedišta koja je zauzeo putnik.", notes = "Povratna vrednost servisa je lista resursa sedišta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje sedišta za datog putnika."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Putnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FlightSeat>>> getSeatsForPassengerWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementorAirline.createFlightSeatsList(service.getSeatsWithPassenger(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/flights", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća letove na kojima je putnik.", notes = "Povratna vrednost servisa je lista resursa letova na kojima je učestvovao putnik sa prosleđenim ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji nijedan let za putnika."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Putnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Flight>>> getFlightsForPassengerWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<Flight>>>(HATEOASImplementorAirline.createFlightsList(service.getFlightsWithPassenger(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća karte na kojima je putnik.", notes = "Povratna vrednost servisa je lista resursa karata na kojima je učestvovao putnik sa prosleđenim ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji nijedna karta za putnika."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Putnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Ticket>>> getTicketsForPassengerWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<Ticket>>>(HATEOASImplementorAirline.createTicketsList(service.getTicketsOfPassenger(id)), HttpStatus.OK);
	}

}
