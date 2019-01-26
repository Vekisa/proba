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
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.service.airline.LuggageInfoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/luggageInfos")
public class LuggageInfoController {

	@Autowired
	private LuggageInfoService service;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća informacija o prtljazima.", notes = "Povratna vrednost servisa je lista resursa informacija o prtljagu koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<LuggageInfo>>> getAllLuggageInfos(Pageable pageable) {
		return new ResponseEntity<List<Resource<LuggageInfo>>>(HATEOASImplementor.createLuggageInfosList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća informaciju o prtljagu sa ID.", notes = "Povratna vrednost servisa je informacija o prtljagu koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = LuggageInfo.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Informacija o prtljagu sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<LuggageInfo>> getLuggageInfoWithId(@PathVariable(value = "id") Long luggageInfoId) {
		return new ResponseEntity<Resource<LuggageInfo>>(HATEOASImplementor.createLuggageInfo(service.findById(luggageInfoId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira informaciju o prtljagu.", notes = "Ažurira informaciju o prtljagu sa prosleđenim ID na osnovu prosleđene avio kompanije. Kolekcije originalne informacije o prtljagu ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = LuggageInfo.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili avio kompaniju nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<LuggageInfo>> updateLuggageInfoWithId(@PathVariable(value = "id") Long luggageInfoId, @Valid @RequestBody LuggageInfo newLuggageInfo) {
		return new ResponseEntity<Resource<LuggageInfo>>(HATEOASImplementor.createLuggageInfo(service.updateLuggageInfo(luggageInfoId, newLuggageInfo)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše informaciju o prtljagu.", notes = "Briše informaciju o prtljagu sa prosleđenim ID", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Informacija o prtljagu sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteLuggageInfoWithId(@PathVariable(value = "id") Long luggageInfoId) {
		service.deleteLuggageInfo(luggageInfoId);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}/airline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća avio kompaniju informacije o prtljagu.", notes = "Vraća resurs avio kompanije.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content. Avio kompaniju za informaciju o prtljagu ne postoji."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Informacija o prtljagu sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Airline>> getAirlineForLuggageInfoWithId(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<Resource<Airline>>(HATEOASImplementor.createAirline(service.getAirlineForLuggageInfo(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/seats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sedišta koja koriste informaciju o prtljagu.", notes = "Povratna vrednost servisa je lista resursa sedišta.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje sedišta za datu informaciju o prtljagu."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Informaciju o prtljagu sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FlightSeat>>> getSeatsUsingLuggageInfoWithId(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementor.createFlightSeatsList(service.getSeatsUsingLuggageInfo(id)), HttpStatus.OK);
	}
	
}
