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

import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.repository.airline.DestinationRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

	@Autowired
	DestinationRepository destinationRepository;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća destinacije.", notes = "Povratna vrednost servisa je lista resursa destinacija koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Destination>>> getAllDestinations(Pageable pageable) {
		Page<Destination> destinations = destinationRepository.findAll(pageable);
		if(destinations.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return new ResponseEntity<List<Resource<Destination>>>(HATEOASImplementor.createDestinationsList(destinations.getContent()), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća destinacija sa ID.", notes = "Povratna vrednost servisa je destinacija koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK", response = Destination.class),
		@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
		@ApiResponse(code = 404, message = "Not Found. Destinacija sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<Destination>> getDestinationById(@PathVariable(value = "id") Long destinationId) {
		Optional<Destination> destination = destinationRepository.findById(destinationId);
		if(destination.isPresent())
			return new ResponseEntity<Resource<Destination>>(HATEOASImplementor.createDestination(destination.get()), HttpStatus.OK);
		else
			return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira destinaciju.", notes = "Ažurira destinaciju sa prosleđenim ID na osnovu prosleđene destinacija. Kolekcije originalne destinacije ostaju netaknute", httpMethod = "PUT", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Destination.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili destinacija nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Destination>> updateDestinationWithId(@PathVariable(value = "id") Long destinationId,
			@Valid @RequestBody Destination newDestination) {
		Optional<Destination> oldDestination = destinationRepository.findById(destinationId);
		if(oldDestination.isPresent()) {
			oldDestination.get().copyFieldsFrom(newDestination);
			return new ResponseEntity<Resource<Destination>>(HATEOASImplementor.createDestination(oldDestination.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		} 
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše destinaciju.", notes = "Briše destinaciju sa prosleđenim ID.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteDestinationWithId(@PathVariable(value = "id") Long destinationId) {
		if(!destinationRepository.findById(destinationId).isPresent()) return ResponseEntity.notFound().build();
		destinationRepository.deleteById(destinationId);
		return ResponseEntity.ok().build();
	}

}
