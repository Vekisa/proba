package com.isap.ISAProject.controller.airline;

import java.util.List;

import javax.validation.Valid;

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

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.service.airline.FlightConfigurationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/configurations")
public class FlightConfigurationController {

	@Autowired
	private FlightConfigurationService service;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća konfiguracije letova.", notes = "Povratna vrednost servisa je lista resursa konfiguracije letova koji pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN')")
	public ResponseEntity<List<Resource<FlightConfiguration>>> getAllFlightConfigurations(Pageable pageable) {
		return new ResponseEntity<List<Resource<FlightConfiguration>>>(HATEOASImplementorAirline.createFlightConfigurationsList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća konfiguraciju (leta) sa ID.", notes = "Povratna vrednost servisa je konfiguracija (leta) sa prosleđenim ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FlightConfiguration.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Konfiguracija (leta) sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToConfiguration(#configurationId)")
	public ResponseEntity<Resource<FlightConfiguration>> getFlightConfigurationWithId(@PathVariable("id") Long configurationId) {
		return new ResponseEntity<Resource<FlightConfiguration>>(HATEOASImplementorAirline.createFlightConfiguration(service.findById(configurationId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše konfiguraciju.", notes = "Briše konfiguraciju leta sa prosleđenim ID.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Konfiguracija sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToConfiguration(#configurationId)")
	public ResponseEntity<?> deleteFlightConfigurationWithId(@PathVariable("id") Long configurationId) {
		service.deleteConfiguration(configurationId);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}/segments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća segmente leta za datu konfiguraciju.", notes = "Povratna vrednost servisa je lista resursa segmenata leta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje segmenti za datu konfiguraciju leta."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Konfiguracija leta sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToConfiguration(#configurationId)")
	public ResponseEntity<List<Resource<FlightSegment>>> getSegmentsForConfigurationWithId(@PathVariable("id") Long configurationId) {
		return new ResponseEntity<List<Resource<FlightSegment>>>(HATEOASImplementorAirline.createFlightSegmentsList(service.getSegmentsForConfiguration(configurationId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/segments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira segment za datu konfiguraciju.", notes = "Povratna vrednost servisa je kreirani segment leta. Vrši se provera poklapanja segmenata.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = FlightSegment.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili segment nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Konfiguracija leta sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToConfiguration(#configurationId)")
	public ResponseEntity<Resource<FlightSegment>> createSegmentForConfigurationWithId(@PathVariable("id") Long configurationId, @Valid @RequestBody FlightSegment flightSegment, @RequestParam("category") Long categoryId) {
		return new ResponseEntity<Resource<FlightSegment>>(HATEOASImplementorAirline.createFlightSegment(service.createSegmentForConfiguration(flightSegment, configurationId, categoryId)), HttpStatus.CREATED);
	}


	@RequestMapping(value = "/{id}/airline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća avio kompaniju kofiguracije leta.", notes = "Povratna vrednost servisa je resurs avio kompanije u koju je registrovana konfiguracija leta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 204, message = "No Content. Avio kompanija nije pridružena konfiguraciji leta."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Konfiguracija leta sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('AIRLINE_ADMIN') AND @securityServiceImpl.hasAccessToConfiguration(#configurationId)")
	public ResponseEntity<Resource<Airline>> getAirlineForConfigurationWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<Airline>>(HATEOASImplementorAirline.createAirline(service.getAirlineForConfiguration(id)), HttpStatus.OK);
	}

}
