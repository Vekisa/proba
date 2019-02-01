package com.isap.ISAProject.controller.user;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.service.user.ReservationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

	@Autowired
	ReservationService reservationService;
	
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
			return new ResponseEntity<List<Resource<Reservation>>>(HATEOASImplementorUsers.createReservationList(reservationService.findAll(pageable)), HttpStatus.OK);
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
	public ResponseEntity<Resource<Reservation>> createReservation(@Valid @RequestBody Reservation reservation, @Valid @RequestBody Ticket ticket,
			@Valid @RequestBody VehicleReservation vehicleReservation, @Valid @RequestBody RoomReservation roomReservation) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.save(reservation,ticket,
				vehicleReservation, roomReservation)), HttpStatus.CREATED);
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
			return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.findById(reservationId)), HttpStatus.OK);
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
		reservationService.deleteById(reservationId);
		return ResponseEntity.ok().build();
	}	
	
	@RequestMapping(value = "/{id}/room-reservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Reservation>> addRoomReservation(@PathVariable("id") Long id, @RequestParam("room") Long roomId, @Valid @RequestBody RoomReservation roomReservation) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.addRoomReservationToReservation(id, roomId, roomReservation)), HttpStatus.CREATED);
	}
	
	public ResponseEntity<Resource<Reservation>> removeRoomReservation(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.removeRoomReservation(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Reservation>> addUserToReservation(@PathVariable(value = "id") Long id, @RequestParam("user") Long userId) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.addUserToReservation(id, userId)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/invite", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Reservation>> inviteUserToReservation(@PathVariable(value = "id") Long id, @RequestBody List<Long> users) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.inviteUsersToReservation(id, users)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/decline", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Reservation>> declineReservationInvite(@PathVariable(value = "id") Long id, @RequestParam("user") Long userId) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.declineInvitation(id, userId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/accept", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Reservation>> acceptInvite(@PathVariable(value = "id") Long id, @RequestParam("user") Long userId) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.acceptInvitation(id, userId)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/cancel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Reservation>> cancelReservation(@PathVariable(value = "id") Long id, @RequestParam("user") Long userId) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.cancelReservation(id, userId)), HttpStatus.OK);
	}
	
}
