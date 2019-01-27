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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.service.airline.AirlineService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/airlines")
public class AirlineController {

	@Autowired
	private AirlineService service;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća avio kompanije.", notes = "Povratna vrednost servisa je lista avio kompanija koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Airline>>> getAllAirlines(Pageable pageable) {
		return new ResponseEntity<List<Resource<Airline>>>(HATEOASImplementor.createAirlinesList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća avio kompaniju sa ID.", notes = "Povratna vrednost servisa je avio kompanija koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<Airline>> getAirlineById(@PathVariable(value = "id") Long airlineId) {
		return new ResponseEntity<Resource<Airline>>(HATEOASImplementor.createAirline(service.findById(airlineId)), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše avio kompaniju.", notes = "Povratna vrednost servisa je sačuvana avio kompanija - ne ona koja je pristigla, iako se po atributima poklapaju (izuzev ID).", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđena avio kompanija nije validna.")
	})
	public ResponseEntity<Resource<Airline>> createAirline(@Valid @RequestBody Airline airline, @RequestParam("destination") Long id) {
		return new ResponseEntity<Resource<Airline>>(HATEOASImplementor.createAirline(service.saveAirline(airline, id)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira avio kompaniju.", notes = "Ažurira avio kompaniju sa prosleđenim ID na osnovu prosleđene avio kompanije. Kolekcije originalne avio kompanije ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili avio kompaniju nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Airline>> updateAirlineWithId(@PathVariable(value = "id") Long airlineId, @Valid @RequestBody Airline newAirline) {
		return new ResponseEntity<Resource<Airline>>(HATEOASImplementor.createAirline(service.updateAirline(airlineId, newAirline)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/location", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira lokaciju zadate avio kompanije.", notes = "Ažurira lokaciju avio kompanije sa prosleđenim ID na osnovu parametra zahteva.", httpMethod = "PUT", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija ili destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Airline>> changeLocationOfAirline(@PathVariable("id") Long airlineId, @RequestParam("destination") Long id) {
		return new ResponseEntity<Resource<Airline>>(HATEOASImplementor.createAirline(service.changeLocationOfAirline(airlineId, id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše avio kompaniju.", notes = "Briše avio kompaniju sa prosleđenim ID", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteAirlineWithId(@PathVariable(value = "id") Long airlineId) {
		service.deleteAirline(airlineId);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Dodaje rejting za datu avio kompaniju.", notes = "Povratna vrednost servisa je avion sa izmenjenim rejtingom.", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Airline>> addRatingToAirlineWithId(@PathVariable(value = "id") Long airlineId, @RequestParam("rating") int rating) {
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/luggageInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća informacije o prtljagu za datu avio kompaniju.", notes = "Povratna vrednost servisa je lista informacija o prljagu.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje informacije o prtljagu za datu avio kompaniju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<LuggageInfo>>> getLuggageInfosForAirlineWithId(@PathVariable(value = "id") Long airlineId) {
		return new ResponseEntity<List<Resource<LuggageInfo>>>(HATEOASImplementor.createLuggageInfosList(service.getLuggageInfosForAirline(airlineId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/luggageInfos", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira informacije o prtljagu za datu avio kompaniju.", notes = "Povratna vrednost servisa je kreirana informacija o prtljagu.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = LuggageInfo.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili informacija o prtljagu nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<LuggageInfo>> createLuggageInfoForAirlineWithId(@PathVariable(value = "id") Long airlineId,
			@Valid @RequestBody LuggageInfo luggageInfo) {
			return new ResponseEntity<Resource<LuggageInfo>>(HATEOASImplementor.createLuggageInfo(service.addLuggageInfoToAirline(airlineId, luggageInfo)),
					HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/destinations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća destinacije za datu avio kompaniju.", notes = "Povratna vrednost servisa je lista destinacija.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje destinacije za datu avio kompaniju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Destination>>> getDestinationsForAirlineWithId(@PathVariable(value = "id") Long airlineId) {
		return new ResponseEntity<List<Resource<Destination>>>(HATEOASImplementor.createDestinationsList(service.getDestinationsForAirline(airlineId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/destinations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira destinaciju za datu avio kompaniju.", notes = "Povratna vrednost servisa je kreirana destinacija.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Destination.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili destinacija nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Destination>> createDestinationForAirlineWithId(@PathVariable(value = "id") Long airlineId,
			@Valid @RequestBody Destination destination) {
			return new ResponseEntity<Resource<Destination>>(HATEOASImplementor.createDestination(service.addDestinationToAirline(airlineId, destination)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/configurations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća konfiguracije leta za datu avio kompaniju.", notes = "Povratna vrednost servisa je lista konfiguracija leta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje konfiguracija leta za datu avio kompaniju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FlightConfiguration>>> getFlightConfigurationsForAirlineWithId(@PathVariable(value = "id") Long airlineId) {
		return new ResponseEntity<List<Resource<FlightConfiguration>>>(HATEOASImplementor.createFlightConfigurationsList(service.getFlightConfigurationsForAirline(airlineId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/configurations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira konfiguraciju leta za datu avio kompaniju.", notes = "Povratna vrednost servisa je kreirana konfiguracija leta.", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = FlightConfiguration.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili konfiguracija leta nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightConfiguration>> createFlightConfigurationForAirlineWithId(@PathVariable(value = "id") Long airlineId) {
		return new ResponseEntity<Resource<FlightConfiguration>>(HATEOASImplementor.createFlightConfiguration(service.addFlightConfigurationToAirline(airlineId, new FlightConfiguration())), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća kategorije sedišta za datu avio kompaniju.", notes = "Povratna vrednost servisa je lista resursa kategorija sedišta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje kategorije sedišta za datu avio kompaniju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FlightSeatCategory>>> getFlightSeatCategoriesForAirlineWithId(@PathVariable(value = "id") Long airlineId) {
		return new ResponseEntity<List<Resource<FlightSeatCategory>>>(HATEOASImplementor.createFlightSeatCategoriesList(service.getFlightSeatCategoriesForAirline(airlineId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/categories", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira kategorija sedišta za datu avio kompaniju.", notes = "Povratna vrednost servisa je kreirana kategorija sedišta.", httpMethod = "GET", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = FlightSeatCategory.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili kategorija sedišta nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSeatCategory>> createFlightSeatCategoryForAirlineWithId(@PathVariable(value = "id") Long airlineId, @Valid @RequestBody FlightSeatCategory category) {
			return new ResponseEntity<Resource<FlightSeatCategory>>(HATEOASImplementor.createFlightSeatCategory(service.addFlightSeatCategoryToAirline(airlineId, category)), HttpStatus.CREATED);
	}
	
}
