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

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.repository.airline.FlightSeatCategoryRepository;

@RestController
@RequestMapping("/seat_categories")
public class FlightSeatCategoryController {

	@Autowired
	FlightSeatCategoryRepository repository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<FlightSeatCategory>>> getAllFlightSeatCategories(Pageable pageable) {
		Page<FlightSeatCategory> categories = repository.findAll(pageable);
		if(categories.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return new ResponseEntity<List<Resource<FlightSeatCategory>>>(HATEOASImplementor.createFlightSeatCategoriesList(categories.getContent()), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<FlightSeatCategory>> getFlightSeatCategoryWithId(@PathVariable("id") Long categoryId) {
		Optional<FlightSeatCategory> category = repository.findById(categoryId);
		if(category.isPresent()) {
			return new ResponseEntity<Resource<FlightSeatCategory>>(HATEOASImplementor.createFlightSeatCategory(category.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<FlightSeatCategory>> updateFlightSeatCategoryWithId(@PathVariable("id") Long categoryId, @Valid @RequestBody FlightSeatCategory newCategory) {
		Optional<FlightSeatCategory> oldCategory = repository.findById(categoryId);
		if(oldCategory.isPresent()) {
			oldCategory.get().copyFieldsFrom(newCategory);
			repository.save(oldCategory.get());
			return new ResponseEntity<Resource<FlightSeatCategory>>(HATEOASImplementor.createFlightSeatCategory(oldCategory.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFlightSeatCategoryWithId(@PathVariable("id") Long categoryId) {
		if(!repository.findById(categoryId).isPresent()) return ResponseEntity.notFound().build();
		repository.deleteById(categoryId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/seats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<FlightSeat>>> getSeatsInThisCategory(@PathVariable("id") Long id) {
		Optional<FlightSeatCategory> category = repository.findById(id);
		if(category.isPresent()) {
			List<FlightSeat> seats = category.get().getSeats();
			if(seats.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<FlightSeat>>>(HATEOASImplementor.createFlightSeatsList(seats), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/segments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<FlightSegment>>> getSegmentsOfThisCategory(@PathVariable("id") Long id) {
		Optional<FlightSeatCategory> category = repository.findById(id);
		if(category.isPresent()) {
			List<FlightSegment> segments = category.get().getSegments();
			if(segments.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<FlightSegment>>>(HATEOASImplementor.createFlightSegmentsList(segments), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}/airline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Airline>> getAirlineForSeatWithId(@PathVariable("id") Long id) {
		Optional<FlightSeatCategory> seat = repository.findById(id);
		if(seat.isPresent()) {
			Airline airline = seat.get().getAirline();
			if(airline == null) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<Resource<Airline>>(HATEOASImplementor.createAirline(airline), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
