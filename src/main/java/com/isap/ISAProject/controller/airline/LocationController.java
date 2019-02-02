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

import com.isap.ISAProject.controller.hotel.HATEOASImplementorHotel;
import com.isap.ISAProject.controller.rentacar.HATEOASImplementorRentacar;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Coordinates;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.service.airline.LocationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/destinations")
public class LocationController {

	@Autowired
	private LocationService service;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća lokacije.", notes = "Povratna vrednost servisa je lista resursa lokacija koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Location>>> getAllDestinations(Pageable pageable) {
		return new ResponseEntity<List<Resource<Location>>>(HATEOASImplementorAirline.createDestinationsList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća lokacija sa ID.", notes = "Povratna vrednost servisa je lokacija koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK", response = Location.class),
		@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
		@ApiResponse(code = 404, message = "Not Found. lokacija sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<Location>> getDestinationById(@PathVariable(value = "id") Long destinationId) {
		return new ResponseEntity<Resource<Location>>(HATEOASImplementorAirline.createDestination(service.findById(destinationId)), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira novu lokaciju.", notes = "Povratna vrednost servisa je resurs sačuvane lokacije.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Created", response = Location.class),
		@ApiResponse(code = 400, message = "Bad Request. Prosleđena lokacija nije validna.")
	})
	public ResponseEntity<Resource<Location>> createDestination(@RequestBody Location destination) {
		return new ResponseEntity<Resource<Location>>(HATEOASImplementorAirline.createDestination(service.saveDestination(destination)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira lokaciju.", notes = "Ažurira lokaciju sa prosleđenim ID na osnovu prosleđene lokacija. Kolekcije originalne lokacije ostaju netaknute", httpMethod = "PUT", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Location.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili lokacija nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. lokacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Location>> updateDestinationWithId(@PathVariable(value = "id") Long destinationId,
			@Valid @RequestBody Location newDestination) {
		return new ResponseEntity<Resource<Location>>(HATEOASImplementorAirline.createDestination(service.updateDestination(destinationId, newDestination)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše lokaciju.", notes = "Briše lokaciju sa prosleđenim ID.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. lokacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteDestinationWithId(@PathVariable(value = "id") Long destinationId) {
		service.deleteDestination(destinationId);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}/flightsFromDestination", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve letove iz date lokacije.", notes = "Povratna vrednost servisa je lista resursa letova koji poleću iz lokacije.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje letovi iz date lokacije."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. lokacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Flight>>> getFlightsFromDestinationWithId(@PathVariable("id") Long destinationId) {
		return new ResponseEntity<List<Resource<Flight>>>(HATEOASImplementorAirline.createFlightsList(service.getFlightsFromDestination(destinationId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/flightsToDestination", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve letove za datu lokaciju.", notes = "Povratna vrednost servisa je lista resursa letova koji poleću iz lokacije.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje letovi za datu lokaciju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. lokacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Flight>>> getFlightsToDestinationWithId(@PathVariable("id") Long destinationId) {
		return new ResponseEntity<List<Resource<Flight>>>(HATEOASImplementorAirline.createFlightsList(service.getFlightsToDestination(destinationId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/airlines", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve avio kompanije sa date lokacije.", notes = "Povratna vrednost servisa je lista resursa avio kompanija koje se nalaze na zadatoj lokaciji.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje avio kompanije za datu lokaciju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Lokacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Airline>>> getAirlinesOnLocation(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<Airline>>>(HATEOASImplementorAirline.createAirlinesList(service.getAirlinesOnLocation(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/offices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve filijale sa date lokacije.", notes = "Povratna vrednost servisa je lista resursa filijala koje se nalaze na zadatoj lokaciji.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje filijale za datu lokaciju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Lokacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<BranchOffice>>> getOfficesOnLocation(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<BranchOffice>>>(HATEOASImplementorRentacar.branchOfficeLinksList(service.getBranchOfficesOnLocation(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/hotels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve hotele sa date lokacije.", notes = "Povratna vrednost servisa je lista resursa hotela koji se nalaze na zadatoj lokaciji.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje hoteli za datu lokaciju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Lokacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Hotel>>> getHotelsOnLocation(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<Hotel>>>(HATEOASImplementorHotel.createHotelsList(service.getHotelsOnLocation(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/coordinates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća koordinate prosleđenog grada.", notes = "Povratna vrednost servisa su koordinate u vidu geografske širine i visine.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Coordinates.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Lokacija sa prosleđenim ID ne postoji."),
			@ApiResponse(code = 500, message = "Internal Server Error. Došlo je do greške prilikom preuzimanja koordinata.")
	})
	public ResponseEntity<Coordinates> getLongLatForDestinationWithName(@PathVariable("id") Long id) {
		return new ResponseEntity<Coordinates>(service.getCoordinatesForCity(id), HttpStatus.OK);
	}

}
