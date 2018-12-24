package com.isap.ISAProject.controller.airline;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.repository.airline.DestinationRepository;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

	@Autowired
	DestinationRepository destinationRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Destination>> getAllDestinations() {
		List<Destination> destinations = destinationRepository.findAll();
		if(destinations.isEmpty())
			return ResponseEntity.notFound().build();
		else
			return new ResponseEntity<List<Destination>>(destinations, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Destination> getDestinationById(@PathVariable(value = "id") Long destinationId) {
		try {
			Optional<Destination> destination = destinationRepository.findById(destinationId);
			if(destination.isPresent())
				return new ResponseEntity<Destination>(destination.get(), HttpStatus.OK);
			else
				return ResponseEntity.notFound().build();
		} catch(IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Destination> updateDestinationWithId(@PathVariable(value = "id") Long destinationId,
			@Valid @RequestBody Destination newDestination) {
		try {
			Optional<Destination> oldDestination = destinationRepository.findById(destinationId);
			if(oldDestination.isPresent()) {
				oldDestination.get().copyFieldsFrom(newDestination);
				return new ResponseEntity<Destination>(oldDestination.get(), HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build();
			} 
		}	catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(value = "id", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteDestinationWithId(@PathVariable(value = "id") Long destinationId) {
		try {
			destinationRepository.deleteById(destinationId);
			return ResponseEntity.ok().build();
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
