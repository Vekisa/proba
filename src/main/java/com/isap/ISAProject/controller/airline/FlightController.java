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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/flights")
public class FlightController {

	@Autowired
	FlightRepository repository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve letove.", notes = "Povratna vrednost servisa je lista resursa letova koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji nijedan let za traženu stranicu."),
			@ApiResponse(code = 400, message = "Bad Request."),
	})
	public ResponseEntity<List<Resource<Flight>>> getAllFlights(Pageable pageable) {
		Page<Flight> flights = repository.findAll(pageable);
		if(flights.hasContent()) {
			return new ResponseEntity<List<Resource<Flight>>>(HATEOASImplementor.createFlightsList(flights.getContent()), HttpStatus.OK);
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća let sa ID.", notes = "Povratna vrednost servisa je let koji ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<Flight>> getFlightById(@PathVariable("id") Long flightId) {
		Optional<Flight> flight = repository.findById(flightId);
		if(flight.isPresent()) {
			return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(flight.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
 	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira let.", notes = "Ažurira let sa prosleđenim ID na osnovu prosleđenog leta. Kolekcije originalnog leta ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili let nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
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
	@ApiOperation(value = "Briše let.", notes = "Briše let sa prosleđenim ID ukoliko nijedno sedište datog leta nije rezervisano", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteFlightWithId(@PathVariable("id") Long flightId) {
		if(!repository.findById(flightId).isPresent()) return ResponseEntity.notFound().build();
		repository.deleteById(flightId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Postavlja mesto sletanja za dati let.", notes = "Povratna vrednost servisa je let sa pridruženom destinacijom sletanja.", httpMethod = "PUT", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let ili destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Flight>> setFinishDestinationForFlightWithId(@PathVariable("id") Long flightId, @RequestParam("destinationId") Long destinationId) {
		Optional<Flight> flight = repository.findById(flightId);
		if(flight.isPresent()) {
			Destination finishDestination = repository.findDestinationById(destinationId);
			if(finishDestination == null) return ResponseEntity.notFound().build();
			flight.get().setFinishDestination(finishDestination);
			repository.save(flight.get());
			return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(flight.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
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
	
	// TODO : Proveri kako tačno dodaje red
	@RequestMapping(value = "/{id}/newSeat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira red sedišta.", notes = "Povratna vrednost servisa je let. Moguće je dodati red jedino na kraj konfiguracije", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili red nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Flight>> addSeatToRowForFlightWithId(@PathVariable("id") Long flightId, @RequestParam("row") int row) {
		Optional<Flight> flight = repository.findById(flightId);
		if(flight.isPresent()) {
			flight.get().addSeatToRow(row);
			repository.save(flight.get());
			return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(flight.get()), HttpStatus.CREATED);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/tickets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća karte datog leta.", notes = "Povratna vrednost servisa je lista karata.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje karte za dati let."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
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
	@ApiOperation(value = "Pridružuje konfiguraciju letu.", notes = "Povratna vrednost servisa je let. Prilikom promene konfiguracije, određena sedišta se brišu, a nova kreiraju.", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Flight.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let ili konfiguraciju sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Flight>> addConfigurationToFlightWithId(@PathVariable("id") Long flightId, @RequestParam("configId") Long configurationId) {
		Optional<Flight> flight = repository.findById(flightId);
		if(flight.isPresent()) {
			FlightConfiguration configuration = repository.findConfigurationById(configurationId);
			if(configuration == null) return ResponseEntity.notFound().build();
			flight.get().setConfiguration(configuration);
			repository.save(flight.get());
			return new ResponseEntity<Resource<Flight>>(HATEOASImplementor.createFlight(flight.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća polaznu destinaciju datog leta.", notes = "Povratna vrednost servisa je mesto iz kog se poleće.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Destination.class),
			@ApiResponse(code = 204, message = "No Content. Letu nije pridružena destinacija poletanja."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
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
	@ApiOperation(value = "Vraća krajnju destinaciju datog leta.", notes = "Povratna vrednost servisa je mesto u koje se sleće.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Destination.class),
			@ApiResponse(code = 204, message = "No Content. Letu nije pridružena destinacija sletanja."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
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
	@ApiOperation(value = "Vraća konfiguraciju datog leta.", notes = "Povratna vrednost servisa je konfiguracija leta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightConfiguration.class),
			@ApiResponse(code = 204, message = "No Content. Letu nije pridružena konfiguracija."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Let sa prosleđenim ID ne postoji.")
	})
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
