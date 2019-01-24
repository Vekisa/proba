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
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.service.airline.FlightSeatCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/seat_categories")
public class FlightSeatCategoryController {

	@Autowired
	private FlightSeatCategoryService service;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća kategorije letova.", notes = "Povratna vrednost servisa je lista kategorija letova koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<FlightSeatCategory>>> getAllFlightSeatCategories(Pageable pageable) {
		return new ResponseEntity<List<Resource<FlightSeatCategory>>>(HATEOASImplementor.createFlightSeatCategoriesList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća kategoriju sedista leta sa ID.", notes = "Povratna vrednost servisa je kategorija leta koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightSeatCategory.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Kategorija sedišta sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSeatCategory>> getFlightSeatCategoryWithId(@PathVariable("id") Long categoryId) {
		return new ResponseEntity<Resource<FlightSeatCategory>>(HATEOASImplementor.createFlightSeatCategory(service.findById(categoryId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira kategoriju sedišta.", notes = "Ažurira kategoriju sedišta sa prosleđenim ID na osnovu prosleđene kategorije leta. Kolekcije originalne kategorije ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightSeatCategory.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili kategorija leta nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Kategorija leta sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSeatCategory>> updateFlightSeatCategoryWithId(@PathVariable("id") Long categoryId, @Valid @RequestBody FlightSeatCategory newCategory) {
		return new ResponseEntity<Resource<FlightSeatCategory>>(HATEOASImplementor.createFlightSeatCategory(service.updateFlightSeatCategory(service.findById(categoryId), newCategory)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše kategoriju sedišta.", notes = "Briše kategoriju sedišta sa prosleđenim ID", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Kategorija sedišta sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteFlightSeatCategoryWithId(@PathVariable("id") Long categoryId) {
		service.deleteFlightSeatCategory(service.findById(categoryId));
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}/seats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sedišta u datoj kategoriji.", notes = "Povratna vrednost servisa je lista sedišta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje sedišta za datu kategoriju sedišta."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Kategorija sedišta sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FlightSeat>>> getSeatsInThisCategory(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementor.createFlightSeatsList(service.getSeatsInCategory(service.findById(id))), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/segments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća segmente kategorije sedišta.", notes = "Povratna vrednost servisa je lista segmenata.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje segmenti za datu kategoriju sedišta."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Kategorija sedišta sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FlightSegment>>> getSegmentsOfThisCategory(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<FlightSegment>>>(HATEOASImplementor.createFlightSegmentsList(service.getSegmentsOfCategory(service.findById(id))), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/airline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća avio kompaniju za datu kategoriju sedišta.", notes = "Povratna vrednost servisa je lista kategorija sedišta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji avio kompanija za datu konfiguraciju sedišta."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Konfiguracija sedišta sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Airline>> getAirlineForSeatWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<Airline>>(HATEOASImplementor.createAirline(service.getAirlineOfCategory(service.findById(id))), HttpStatus.OK);
	}

}
