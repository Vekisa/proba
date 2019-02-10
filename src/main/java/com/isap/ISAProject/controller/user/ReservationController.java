package com.isap.ISAProject.controller.user;

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

import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.airline.Passenger;
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
	@PreAuthorize("hasAuthority('USERS_ADMIN')")
	public ResponseEntity<Resource<Reservation>> createReservation(@Valid @RequestBody Reservation reservation, @Valid @RequestBody Ticket ticket,
			@Valid @RequestBody VehicleReservation vehicleReservation, @Valid @RequestBody RoomReservation roomReservation) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.save(reservation,ticket,
				vehicleReservation, roomReservation)), HttpStatus.CREATED);
	}
	
	//Povezivanje sa rezervacijom sobe
	@RequestMapping(value= "/{id}/set-room-reservation/{idroom}", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraca rezervaciju.", notes = "Povezuje rezervaciju sobe za glavnu rezervaciju",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = Reservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN') OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<Reservation>> addRoomReservationToReservationWitdId(@PathVariable(value="id") Long reservationId, 
			@PathVariable(value="idroom") Long roomReservationId) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.addRoomReservationToReservationWitdId(reservationId, roomReservationId)), HttpStatus.OK);
	}
	
	//Povezivanje sa rezervacijom automobila
	@RequestMapping(value= "/{id}/set-vehicle-reservation/{idvehicle}", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraca rezervaciju.", notes = "Povezuje rezervaciju automobila za glavnu rezervaciju",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = Reservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN') OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<Reservation>> addVehicleReservationToReservationWitdId(@PathVariable(value="id") Long reservationId, 
			@PathVariable(value="idvehicle") Long vehicleReservationId) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.addVehicleReservationToReservationWithId(reservationId, vehicleReservationId)), HttpStatus.OK);
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
	@PreAuthorize("hasAuthority('USERS_ADMIN') OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<?> deleteReservationWithId(@PathVariable(value="id") Long reservationId){
		reservationService.deleteById(reservationId);
		return ResponseEntity.ok().build();
	}
	
	//Vraca lokaciju za koju vazi rezervacija
	@RequestMapping(value="/{id}/location", method=RequestMethod.GET)
	@ApiOperation(value = "Lokacija rezervacije.", notes = "Vraca lokaciju rezervacije sa proslejenim id-em", httpMethod = "GET")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Location.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Location>> getLocationOfReservation(@PathVariable(value="id") Long reservationId){
		return new ResponseEntity<Resource<Location>>(HATEOASImplementorUsers.createLocation(reservationService.getLocation(reservationId)), HttpStatus.OK);
	}	
	
	@RequestMapping(value = "/{id}/room-reservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("(hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToRoom(#roomId)) OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<Reservation>> addRoomReservation(@PathVariable("id") Long id, @RequestParam("room") Long roomId, @Valid @RequestBody RoomReservation roomReservation) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.addRoomReservationToReservation(id, roomId, roomReservation)), HttpStatus.CREATED);
	}
	
	public ResponseEntity<Resource<Reservation>> removeRoomReservation(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.removeRoomReservation(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('USERS_ADMIN') OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<Reservation>> addUserToReservation(@PathVariable(value = "id") Long id, @RequestParam("user") Long userId, @RequestParam("points") int points) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.addUserToReservation(id, userId, points)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/invite", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('USERS_ADMIN') OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<Reservation>> inviteUserToReservation(@PathVariable(value = "id") Long id, @RequestBody List<Long> users) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.inviteUsersToReservation(id, users)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/decline", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('USERS_ADMIN') OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<Reservation>> declineReservationInvite(@PathVariable(value = "id") Long id, @RequestParam("user") Long userId) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.declineInvitation(id, userId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/accept", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('USERS_ADMIN') OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<Reservation>> acceptInvite(@PathVariable(value = "id") Long id, @RequestParam("user") Long userId) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.acceptInvitation(id, userId)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/cancel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('USERS_ADMIN') OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<Reservation>> cancelReservation(@PathVariable(value = "id") Long id, @RequestParam("user") Long userId) {
		return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(reservationService.cancelReservation(id, userId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/passengers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Passenger>> getPassengers(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<List<Passenger>>(reservationService.getPassengers(id), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/free-seats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FlightSeat>> getFreeSeats(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<List<FlightSeat>>(reservationService.getFreeSeats(id), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/passengers_unregistered", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Passenger>> getUnregisteredPassengers(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<List<Passenger>>(reservationService.getUnregisteredPassengers(id), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/delete_passenger/{idpass}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('USERS_ADMIN') OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Passenger> deletePassenger(@PathVariable(value = "id") Long id, @PathVariable(value = "idpass") Long passengerId) {
		return new ResponseEntity<Passenger>(reservationService.deletePassenger(id,passengerId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/factor", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('USERS_ADMIN')")
	public ResponseEntity<?> setDiscountFactor(@RequestParam("factor") int factor) {
		reservationService.setDiscountFactor(factor);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/factor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> getDiscountFactor() {
		return new ResponseEntity<Integer>(reservationService.getDiscountFactor(), HttpStatus.OK);
	}
	
}
