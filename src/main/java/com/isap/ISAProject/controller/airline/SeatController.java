package com.isap.ISAProject.controller.airline;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.isap.ISAProject.service.airline.FlightSeatService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/seats")
public class SeatController {

	@Autowired
	private FlightSeatService service;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sedišta.", notes = "Povratna vrednost servisa je lista sedišta koja pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<FlightSeat>>> getAllSeats(Pageable pageable) {
		return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementor.createFlightSeatsList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sedište sa ID.", notes = "Povratna vrednost servisa je sedište koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightSeat.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSeat>> getSeatWithId(@PathVariable("id") Long seatId) {
		return new ResponseEntity<Resource<FlightSeat>>(HATEOASImplementor.createFlightSeat(service.findById(seatId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše sedište.", notes = "Briše sedište sa prosleđenim ID", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteSeatWithId(@PathVariable("id") Long seatId) {
		service.deleteSeat(service.findById(seatId));
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}/luggageInfo", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Postavlja informaciju o prtljagu za sedište.", notes = "Dodeljuje informaciju o prtljagu za sedište sa prosleđenim ID.", httpMethod = "PUT", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightSeat.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište ili informaciju o prtljagu sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSeat>> setLuggageInfoForSeatWithId(@PathVariable("id") Long seatId, @RequestParam("luggageId") Long luggageId) {
		return new ResponseEntity<Resource<FlightSeat>>(HATEOASImplementor.createFlightSeat(service.setLuggageInfoForSeat(service.findById(seatId), luggageId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/luggageInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća informaciju o prtljagu za dato sedište.", notes = "Povratna vrednost servisa je resurs informacije o prtljagu.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = LuggageInfo.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji informacija o prtljagu za dato sedište."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<LuggageInfo>> getLuggageInfoForSeatWithId(@PathVariable("id") Long seatId) {
		return new ResponseEntity<Resource<LuggageInfo>>(HATEOASImplementor.createLuggageInfo(service.getLuggageInfoOfSeat(service.findById(seatId))), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/passenger", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Postavlja putnika za sedište.", notes = "Dodeljuje putnika za sedište sa prosleđenim ID.", httpMethod = "PUT", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightSeat.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište ili putnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSeat>> setPassengerForSeatWithId(@PathVariable("id") Long seatId, @RequestParam("passengerId") Long passengerId) {
		return new ResponseEntity<Resource<FlightSeat>>(HATEOASImplementor.createFlightSeat(service.setPassengerToSeat(service.findById(seatId), passengerId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/passenger", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća putnika za dato sedište.", notes = "Povratna vrednost servisa je resurs putnika.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Passenger.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji putnik za dato sedište."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Passenger>> getPassengerForSeatWithId(@PathVariable("id") Long seatId) {
		return new ResponseEntity<Resource<Passenger>>(HATEOASImplementor.createPassenger(service.getPassengerOfSeat(service.findById(seatId))), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/category", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća kategoriju kojoj pripada sedište.", notes = "Povratna vrednost servisa je resurs kategorije sedišta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightSeatCategory.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji kategorija za dato sedište."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSeatCategory>> getCategoryForSeatWithId(@PathVariable("id") Long seatId) {
		return new ResponseEntity<Resource<FlightSeatCategory>>(HATEOASImplementor.createFlightSeatCategory(service.getCategoryOfSeat(service.findById(seatId))), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/ticket", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća kartu kojoj pripada sedište.", notes = "Povratna vrednost servisa je resurs kategorije sedišta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Ticket.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji karta za dato sedište."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Ticket>> getTicketForSeatWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<Ticket>>(HATEOASImplementor.createTicket(service.getTicketOfSeat(service.findById(id))), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/flight", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća let kojem pripada sedište.", notes = "Povratna vrednost servisa je resurs leta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Flight.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji let za dato sedište."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Sedište sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Flight>> getFlightForSeatWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(service.getFlightOfSeat(service.findById(id))), HttpStatus.OK);
	}

}
