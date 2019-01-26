package com.isap.ISAProject.controller.user;

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

import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.repository.user.ReservationRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

	@Autowired
	ReservationRepository reservationRepository;
	
	//Lista svih rezervacija
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve rezervacije.", notes = "Povratna vrednost metode je lista zahteva rezervacija"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<Reservation>>> getReservations(Pageable pageable){
		Page<Reservation> reservations = reservationRepository.findAll(pageable); 
		if(reservations.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return new ResponseEntity<List<Resource<Reservation>>>(HATEOASImplementor.createReservationList(reservations.getContent()), HttpStatus.OK);
	}
	
	//Kreiranje rezervacije
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše rezervaciju.", notes = "Povratna vrednost servisa je sačuvana rezervacija.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = Reservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Reservation>> createReservation(@Valid @RequestBody Reservation reservation) {
		Reservation createdReservation =  reservationRepository.save(reservation);
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementor.createReservation(createdReservation), HttpStatus.CREATED);
	}
	
	//Vraca rezervaciju sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća rezervaciju sa zadatim ID-em.", notes = "Povratna vrednost metode je rezervcacija"
			+ "koji ima zadati ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Reservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Reservation>> getReservationById(@PathVariable(value="id") Long reservationId) {
		Optional<Reservation> reservation = reservationRepository.findById(reservationId);
		if(reservation.isPresent())
			return new ResponseEntity<Resource<Reservation>>(HATEOASImplementor.createReservation(reservation.get()), HttpStatus.OK);
		else
			return ResponseEntity.noContent().build();
	}
	
	//Brisanje rezervaciju sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše rezervaciju.", notes = "Briše rezervaciju sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteReservationWithId(@PathVariable(value="id") Long reservationId){
		if(!reservationRepository.findById(reservationId).isPresent())
			return ResponseEntity.notFound().build();
		
		reservationRepository.deleteById(reservationId);
		return ResponseEntity.ok().build();
	}	
	
	//Update rezervacije sa zadatim id-em
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update rezervacije.", notes = "Ažurira rezervacije sa zadatim ID-em na osnovu prosleđene rzervacije."
			+ " Kolekcije originalnog usera ostaju netaknute.",
			httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Reservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Reservation>> updateReservationWithId(@PathVariable(value = "id") Long reservationId,
			@Valid @RequestBody Reservation newReservation) {
		Optional<Reservation> oldReservation = reservationRepository.findById(reservationId);
		if(oldReservation.isPresent()) {
			oldReservation.get().copyFieldsFrom(newReservation);
			reservationRepository.save(oldReservation.get());
			return new ResponseEntity<Resource<Reservation>>(HATEOASImplementor.createReservation(oldReservation.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
