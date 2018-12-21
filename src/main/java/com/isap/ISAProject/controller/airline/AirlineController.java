package com.isap.ISAProject.controller.airline;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.repository.airline.AirlineRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/airlines")
public class AirlineController {

	@Autowired
	AirlineRepository airlineRepository;

	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "Get airlines.", notes = "Returns list of airlines defined by pagination parameters.", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Airline.class),
	        @ApiResponse(code = 401, message = "Unauthorized"), 
	        @ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<List<Airline>> getAllAirlines(Pageable pageable) {
		Page<Airline> airlines = airlineRepository.findAll(pageable);
		if(airlines.isEmpty())
			return ResponseEntity.noContent().header("Message", "No airlines found").build();	// TODO : Ubacena poruka za front, mozda ubaciti lokalizaciju nekako?
		else
			return new ResponseEntity<List<Airline>>(airlines.getContent(), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Resource<Airline>> getAirlineById(@PathVariable(value = "id") Long airlineId) {
		Optional<Airline> airline = airlineRepository.findById(airlineId);
		if(airline.isPresent()) {
			// Primer za kreiranje jednog linka
			Resource<Airline> resource = new Resource<Airline>(airline.get());
			resource.add(linkTo(methodOn(AirlineController.class).getAllAirlines(null)).slash("?page=0&size=5").withRel("all-airlines"));
			return new ResponseEntity<Resource<Airline>>(resource, HttpStatus.OK);
		} else
			return ResponseEntity.notFound().build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Airline> createAirline(@Valid @RequestBody Airline airline) {
		Airline created = airlineRepository.save(airline);
		return new ResponseEntity<Airline>(created, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Airline> updateAirlineWithId(@PathVariable(value = "id") Long airlineId,
			@Valid @RequestBody Airline newAirline) {
		Optional<Airline> oldAirline = airlineRepository.findById(airlineId);
		if(oldAirline.isPresent()) {
			oldAirline.get().copyFieldsFrom(newAirline);
			airlineRepository.save(oldAirline.get());
			return new ResponseEntity<Airline>(oldAirline.get(), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAirline(@PathVariable(value = "id") Long airlineId) {
		try {
			airlineRepository.deleteById(airlineId);
			return ResponseEntity.ok().build();
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	// TODO : I ovde treba paginacija - hocu da izvlacim 10 po 10 informacija npr, a ne sve odjednom
	// Pocetak : pageable.getOffset() ; Broj komada : pageable.getPageSize()
	@RequestMapping(value = "/{id}/luggageInfos", method = RequestMethod.GET)
	public ResponseEntity<List<LuggageInfo>> getLuggageInfosForAirlineWithId(@PathVariable(value = "id") Long airlineId) {
		Optional<Airline> airline = airlineRepository.findById(airlineId);
		if(airline.isPresent()) {
			List<LuggageInfo> list = airline.get().getLuggageInfos();
			if(list.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<LuggageInfo>>(list, HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/luggageInfos", method = RequestMethod.POST)
	public ResponseEntity<LuggageInfo> createLuggageInfoForAirlineWithId(@PathVariable(value = "id") Long airlineId,
			@Valid @RequestBody LuggageInfo luggageInfo) {
			Optional<Airline> airline = airlineRepository.findById(airlineId);
			if(airline.isPresent()) {
				airline.get().add(luggageInfo);
				airlineRepository.save(airline.get());
				return new ResponseEntity<LuggageInfo>(luggageInfo, HttpStatus.CREATED);
			} else {
				return ResponseEntity.notFound().build();
			}
	}

	// TODO : I ovde treba paginacija - hocu da izvlacim 10 po 10 destinacija npr, a ne sve odjednom
	@RequestMapping(value = "/{id}/destinations", method = RequestMethod.GET)
	public ResponseEntity<List<Destination>> getDestinationsForAirlineWithId(@PathVariable(value = "id") Long airlineId) {
		Optional<Airline> airline = airlineRepository.findById(airlineId);
		if(airline.isPresent()) {
			List<Destination> list = airline.get().getDestinations();
			if(list.isEmpty()) {
				return ResponseEntity.notFound().build();
			} else {
				return new ResponseEntity<List<Destination>>(list, HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/destinations", method = RequestMethod.POST)
	public ResponseEntity<Destination> createDestinationForAirlineWithId(@PathVariable(value = "id") Long airlineId,
			@Valid @RequestBody Destination destination) {
		Optional<Airline> airline = airlineRepository.findById(airlineId);
		if(airline.isPresent()) {
			airline.get().add(destination);
			airlineRepository.save(airline.get());
			return new ResponseEntity<Destination>(destination, HttpStatus.CREATED);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// TODO : I ovde treba paginacija - hocu da izvlacim 10 po 10 konfiguracija npr, a ne sve odjednom
	@RequestMapping(value = "/{id}/configurations", method = RequestMethod.GET)
	public ResponseEntity<List<FlightConfiguration>> getFlightConfigurationsForAirlineWithId(@PathVariable(value = "id") Long airlineId) {
		Optional<Airline> airline = airlineRepository.findById(airlineId);
		if(airline.isPresent()) {
			List<FlightConfiguration> list = airline.get().getConfigurations();
			if(list.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<FlightConfiguration>>(list, HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/configurations", method = RequestMethod.POST)
	public ResponseEntity<FlightConfiguration> createFlightConfigurationForAirlineWithId(@PathVariable(value = "id") Long airlineId,
			@Valid @RequestBody FlightConfiguration flightConfiguration) {
		Optional<Airline> airline = airlineRepository.findById(airlineId);
		if(airline.isPresent()) {
			airline.get().add(flightConfiguration);
			airlineRepository.save(airline.get());
			return new ResponseEntity<FlightConfiguration>(flightConfiguration, HttpStatus.CREATED);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
