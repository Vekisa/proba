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
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.repository.airline.FlightConfigurationRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/configurations")
public class FlightConfigurationController {

	@Autowired
	FlightConfigurationRepository flightConfigurationRepository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća konfiguracije letova.", notes = "Povratna vrednost servisa je lista resursa konfiguracije letova koji pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<FlightConfiguration>>> getAllFlightConfigurations(Pageable pageable) {
		Page<FlightConfiguration> configurations = flightConfigurationRepository.findAll(pageable);
		if(configurations.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return new ResponseEntity<List<Resource<FlightConfiguration>>>(HATEOASImplementor.createFlightConfigurationsList(configurations.getContent()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća konfiguraciju (leta) sa ID.", notes = "Povratna vrednost servisa je konfiguracija (leta) sa prosleđenim ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightConfiguration.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Konfiguracija (leta) sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightConfiguration>> getFlightConfigurationWithId(@PathVariable("id") Long configurationId) {
		Optional<FlightConfiguration> configuration = flightConfigurationRepository.findById(configurationId);
		if(configuration.isPresent()) {
			return new ResponseEntity<Resource<FlightConfiguration>>(HATEOASImplementor.createFlightConfiguration(configuration.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše konfiguraciju.", notes = "Briše konfiguraciju leta sa prosleđenim ID.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Konfiguracija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteFlightConfigurationWithId(@PathVariable("id") Long configurationId) {
		if(!flightConfigurationRepository.findById(configurationId).isPresent()) return ResponseEntity.notFound().build();
		flightConfigurationRepository.deleteById(configurationId);
		return ResponseEntity.ok().build();
	}
	
	/* TODO : Da li je ovo uopste neophodno?
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<FlightConfiguration>> updateFlightConfigurationWithId(@PathVariable("id") Long configurationId,
			@Valid @RequestBody FlightConfiguration newConfiguration) {
		Optional<FlightConfiguration> oldConfiguration = flightConfigurationRepository.findById(configurationId);
		if(oldConfiguration.isPresent()) {
			oldConfiguration.get().copyFieldsFrom(newConfiguration);
			return new ResponseEntity<Resource<FlightConfiguration>>(HATEOASImplementor.createFlightConfiguration(oldConfiguration.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}*/
	
	@RequestMapping(value = "/{id}/segments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća segmente leta za datu konfiguraciju.", notes = "Povratna vrednost servisa je lista resursa segmenata leta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje segmenti za datu konfiguraciju leta."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Konfiguracija leta sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FlightSegment>>> getSegmentsForConfigurationWithId(@PathVariable("id") Long configurationId) {
		Optional<FlightConfiguration> configuration = flightConfigurationRepository.findById(configurationId);
		if(configuration.isPresent()) {
			List<FlightSegment> list = configuration.get().getSegments();
			if(list.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<FlightSegment>>>(HATEOASImplementor.createSegmentsList(list), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/segments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira segment za datu konfiguraciju.", notes = "Povratna vrednost servisa je kreirani segment leta.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = FlightSegment.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili segment nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Konfiguracija leta sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FlightSegment>> createSegmentForConfigurationWithId(@PathVariable("id") Long configurationId, @Valid @RequestBody FlightSegment flightSegment) {
		Optional<FlightConfiguration> configuration = flightConfigurationRepository.findById(configurationId);
		if(configuration.isPresent()) {
			configuration.get().add(flightSegment);
			return new ResponseEntity<Resource<FlightSegment>>(HATEOASImplementor.createFlightSegment(flightSegment), HttpStatus.CREATED);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
