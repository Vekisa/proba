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

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.service.airline.DestinationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

	@Autowired
	private DestinationService service;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća destinacije.", notes = "Povratna vrednost servisa je lista resursa destinacija koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Destination>>> getAllDestinations(Pageable pageable) {
		return new ResponseEntity<List<Resource<Destination>>>(HATEOASImplementor.createDestinationsList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća destinacija sa ID.", notes = "Povratna vrednost servisa je destinacija koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK", response = Destination.class),
		@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
		@ApiResponse(code = 404, message = "Not Found. Destinacija sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<Destination>> getDestinationById(@PathVariable(value = "id") Long destinationId) {
		return new ResponseEntity<Resource<Destination>>(HATEOASImplementor.createDestination(service.findById(destinationId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira destinaciju.", notes = "Ažurira destinaciju sa prosleđenim ID na osnovu prosleđene destinacija. Kolekcije originalne destinacije ostaju netaknute", httpMethod = "PUT", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Destination.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili destinacija nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Destination>> updateDestinationWithId(@PathVariable(value = "id") Long destinationId,
			@Valid @RequestBody Destination newDestination) {
		return new ResponseEntity<Resource<Destination>>(HATEOASImplementor.createDestination(service.updateDestination(destinationId, newDestination)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše destinaciju.", notes = "Briše destinaciju sa prosleđenim ID.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteDestinationWithId(@PathVariable(value = "id") Long destinationId) {
		service.deleteDestination(destinationId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/flights", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira let za datu destinaciju.", notes = "Povratna vrednost servisa je kreirani let.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili let nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Flight>> addFlightToDestinationWithId(@PathVariable("id") Long destinationId, @Valid @RequestBody Flight flight) {
		return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(service.addFlightToDestination(flight, destinationId)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/flightsFromDestination", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve letove iz date destinacije.", notes = "Povratna vrednost servisa je lista resursa letova koji poleću iz destinacije.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje letovi iz date destinacije."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Flight>>> getFlightsFromDestinationWithId(@PathVariable("id") Long destinationId) {
		return new ResponseEntity<List<Resource<Flight>>>(HATEOASImplementor.createFlightsList(service.getFlightsFromDestination(destinationId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/flightsToDestination", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve letove za datu destinaciju.", notes = "Povratna vrednost servisa je lista resursa letova koji poleću iz destinacije.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje letovi za datu destinaciju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Flight>>> getFlightsToDestinationWithId(@PathVariable("id") Long destinationId) {
		return new ResponseEntity<List<Resource<Flight>>>(HATEOASImplementor.createFlightsList(service.getFlightsToDestination(destinationId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/airline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća avio kompaniju destinacije.", notes = "Povratna vrednost servisa je resurs avio kompanije u koju je registrovana destinacija.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 204, message = "No Content. Avio kompanija nije pridružena destinaciji."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Airline>> getAirlineForDestinationWithId(@PathVariable("id") Long id) {
				return new ResponseEntity<Resource<Airline>>(HATEOASImplementor.createAirline(service.getAirlineForDestination(id)), HttpStatus.OK);
	}
	
}
