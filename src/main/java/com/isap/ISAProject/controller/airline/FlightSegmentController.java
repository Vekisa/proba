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
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.repository.airline.FlightSegmentRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/segments")
public class FlightSegmentController {

	@Autowired
	FlightSegmentRepository repository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća segmente letova.", notes = "Povratna vrednost servisa je lista resursa segmenata letova koji pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni")
	})
	public ResponseEntity<List<Resource<FlightSegment>>> getAllFlightSegments(Pageable pageable) {
		Page<FlightSegment> segments = repository.findAll(pageable);
		if(segments.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return new ResponseEntity<List<Resource<FlightSegment>>>(HATEOASImplementor.createSegmentsList(segments.getContent()), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća segment (leta) sa ID.", notes = "Povratna vrednost servisa je segment (leta) sa prosleđenim ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightSegment.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Segment (leta) sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSegment>> getFlightSegmentWithId(@PathVariable("id") Long segmentId) {
		Optional<FlightSegment> segment = repository.findById(segmentId);
		if(segment.isPresent()) {
			return new ResponseEntity<Resource<FlightSegment>>(HATEOASImplementor.createFlightSegment(segment.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
