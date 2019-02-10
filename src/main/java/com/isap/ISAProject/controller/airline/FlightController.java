package com.isap.ISAProject.controller.airline;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.model.airline.TripType;
import com.isap.ISAProject.service.airline.FlightService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/flights")
public class FlightController {

	@Autowired
	private FlightService service;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve letove.", notes = "Povratna vrednost servisa je lista resursa letova koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji nijedan let za traženu stranicu."),
			@ApiResponse(code = 400, message = "Bad Request."),
	})
	public ResponseEntity<List<Resource<Flight>>> getAllFlights(Pageable pageable) {
		return new ResponseEntity<List<Resource<Flight>>>(HATEOASImplementorAirline.createFlightsList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća let sa ID.", notes = "Povratna vrednost servisa je let koji ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<Flight>> getFlightById(@PathVariable("id") Long flightId) {
		return new ResponseEntity<Resource<Flight>>(HATEOASImplementorAirline.createFlight(service.findById(flightId)), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća let sa ID.", notes = "Povratna vrednost servisa je let koji ima traženi ID.", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created.", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili let nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Let sa traženim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToAirline(#id)")
	public ResponseEntity<Resource<Flight>> createFlightForAirline(@RequestBody Flight flight, @RequestParam("airline") Long id, @RequestParam("destination") Long destinationId) {
		return new ResponseEntity<Resource<Flight>>(HATEOASImplementorAirline.createFlight(service.createFlight(id, flight, destinationId)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira let.", notes = "Ažurira let sa prosleđenim ID na osnovu prosleđenog leta. Kolekcije originalnog leta ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili let nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToFlight(#flightId)")
	public ResponseEntity<Resource<Flight>> updateFlightWithId(@PathVariable("id") Long flightId, @Valid @RequestBody Flight newFlight) {
		return new ResponseEntity<Resource<Flight>>(HATEOASImplementorAirline.createFlight(service.updateFlight(flightId, newFlight)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše let.", notes = "Briše let sa prosleđenim ID ukoliko nijedno sedište datog leta nije rezervisano", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToFlight(#flightId)")
	public ResponseEntity<?> deleteFlightWithId(@PathVariable("id") Long flightId) {
		service.deleteFlight(flightId);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Postavlja mesto sletanja za dati let.", notes = "Povratna vrednost servisa je let sa pridruženom destinacijom sletanja.", httpMethod = "PUT", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let ili destinacija sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToFlight(#flightId)")
	public ResponseEntity<Resource<Flight>> setFinishDestinationForFlightWithId(@PathVariable("id") Long flightId, @RequestParam("destinationId") Long destinationId) {
		return new ResponseEntity<Resource<Flight>>(HATEOASImplementorAirline.createFlight(service.setFinishDestinationForFlight(destinationId, flightId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/seats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sedišta datog leta.", notes = "Povratna vrednost servisa je lista sedišta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje sedišta za dati let."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FlightSeat>>> getFlightSeatsForFlightWithId(@PathVariable("id") Long flightId) {
		return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementorAirline.createFlightSeatsList(service.getSeatsForFlight(flightId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/newSeat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira red sedišta.", notes = "Povratna vrednost servisa je let. Moguće je dodati red jedino na kraj konfiguracije", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili red nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToFlight(#flightId)")
	public ResponseEntity<Resource<Flight>> addSeatToRowForFlightWithId(@PathVariable("id") Long flightId, @RequestParam("row") int row) {
		return new ResponseEntity<Resource<Flight>>(HATEOASImplementorAirline.createFlight(service.addSeatToRowForFlight(row, flightId)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća karte datog leta.", notes = "Povratna vrednost servisa je lista karata.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje karte za dati let."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToFlight(#flightId)")
	public ResponseEntity<List<Resource<Ticket>>> getTicketsForFlightWithId(@PathVariable("id") Long flightId) {
		return new ResponseEntity<List<Resource<Ticket>>>(HATEOASImplementorAirline.createTicketsList(service.getTicketsForFlight(flightId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/configuration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pridružuje konfiguraciju letu.", notes = "Povratna vrednost servisa je let. Prilikom promene konfiguracije, određena sedišta se brišu, a nova kreiraju.", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let ili konfiguraciju sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Flight>> addConfigurationToFlightWithId(@PathVariable("id") Long flightId, @RequestParam("configId") Long configurationId) {
		return new ResponseEntity<Resource<Flight>>(HATEOASImplementorAirline.createFlight(service.setConfigurationToFlight(configurationId, flightId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća polaznu destinaciju datog leta.", notes = "Povratna vrednost servisa je mesto iz kog se poleće.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Location.class),
			@ApiResponse(code = 204, message = "No Content. Letu nije pridružena destinacija poletanja."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Location>> getStartDestinationForFlightWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<Location>>(HATEOASImplementorAirline.createDestination(service.getStartDestinationOfFlight(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/finish", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća krajnju destinaciju datog leta.", notes = "Povratna vrednost servisa je mesto u koje se sleće.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Location.class),
			@ApiResponse(code = 204, message = "No Content. Letu nije pridružena destinacija sletanja."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Location>> getFinishDestinationForFlightWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<Location>>(HATEOASImplementorAirline.createDestination(service.getFinishDestinationOfFlight(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pretraga letova", responseContainer = "List", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Flight>>> search(Pageable pageable, 
			@RequestParam(value="locationName", required=false) String startDest,
			@RequestParam(value="name", required=false) String finishDest,
			@RequestParam(value="startDate", required=false) Long depTime, 
			@RequestParam(value="endDate", required=false) Long arrTime,
			@RequestParam(value="tripType", required=false) TripType tripType,
			@RequestParam(value="category", required=false) String category,
			@RequestParam(value="weight", required=false) Double weight,
			@RequestParam(value="personNum", required=false) Integer personNum,
			@RequestParam(value="airline", required=false) String airline,
			@RequestParam(value="priceBegin", required=false) Double priceBegin,
			@RequestParam(value="priceEnd", required=false) Double priceEnd,
			@RequestParam(value="durationBegin", required=false) Double durationBegin,
			@RequestParam(value="durationEnd", required=false) Double durationEnd){
		Date depTimeDate = null;
		Date arrTimeDate = null;
		if(depTime != null) {
			logger.info("detTime NIJE NULL");
			depTimeDate = new Date(depTime);
		}
		if(arrTime != null) {
			logger.info("arrTime NIJE NULL");
			arrTimeDate = new Date(arrTime);
		}
		List<Flight> ret = service.search(pageable, startDest, finishDest, depTimeDate, arrTimeDate, tripType, category, weight, personNum, airline, priceBegin, priceEnd, durationBegin, durationEnd);
		return new ResponseEntity<List<Resource<Flight>>>(HATEOASImplementorAirline.createFlightsList(ret), HttpStatus.OK);
	}
	
}
