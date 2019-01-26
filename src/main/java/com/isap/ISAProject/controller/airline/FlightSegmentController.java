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

import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.service.airline.FlightSegmentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/segments")
public class FlightSegmentController {

	@Autowired
	private FlightSegmentService service;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća segmente letova.", notes = "Povratna vrednost servisa je lista resursa segmenata letova koji pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni")
	})
	public ResponseEntity<List<Resource<FlightSegment>>> getAllFlightSegments(Pageable pageable) {
			return new ResponseEntity<List<Resource<FlightSegment>>>(HATEOASImplementor.createFlightSegmentsList(service.findAll(pageable)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća segment (leta) sa ID.", notes = "Povratna vrednost servisa je segment (leta) sa prosleđenim ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightSegment.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Segment (leta) sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSegment>> getFlightSegmentWithId(@PathVariable("id") Long segmentId) {
			return new ResponseEntity<Resource<FlightSegment>>(HATEOASImplementor.createFlightSegment(service.findById(segmentId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira segment (leta) sa ID.", notes = "Povratna vrednost servisa je segment (leta) sa prosleđenim ID nakon izvrešenih izmena.", httpMethod = "PUT", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightSegment.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili segment nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Segment (leta) sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSegment>> updateFlightSegmentWithId(@PathVariable("id") Long segmentId, @Valid @RequestBody FlightSegment newSegment) {
			return new ResponseEntity<Resource<FlightSegment>>(HATEOASImplementor.createFlightSegment(service.updateSegment(segmentId, newSegment)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše segment (leta).", notes = "Briše segment (leta) sa prosleđenim ID.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Segment sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteFlightSegmentWithId(@PathVariable("id") Long segmentId) {
		service.deleteSegment(segmentId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/configuration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća konfiguraciju leta za dati segment.", notes = "Povratna vrednost servisa je resurs konfiguracije leta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightConfiguration.class),
			@ApiResponse(code = 204, message = "No Content. Konfiguracija leta za dati segment ne postoji."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Segment sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightConfiguration>> getConfigurationForSegment(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<FlightConfiguration>>(HATEOASImplementor.createFlightConfiguration(service.getConfigurationForSegment(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/category", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća konfiguraciju leta za dati segment.", notes = "Povratna vrednost servisa je resurs konfiguracije leta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightSeatCategory.class),
			@ApiResponse(code = 204, message = "No Content. Kategorija sedišta za dati segment ne postoji."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Segment sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSeatCategory>> getCategoryForSegment(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<FlightSeatCategory>>(HATEOASImplementor.createFlightSeatCategory(service.getCategoryOfSegment(id)), HttpStatus.OK);
	}
	
}
