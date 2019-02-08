package com.isap.ISAProject.controller.hotel;

import java.util.Date;
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

import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.service.hotel.RoomReservationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/room_reservations")
public class RoomReservationController {
	@Autowired
	RoomReservationService roomReservationService;
	
	//Vraca sve rezervacije soba
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve rezervacije soba.", notes = "Povratna vrednost metode je lista rezervacija soba"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<RoomReservation>>> getAllRoomReservations(Pageable pageable){
			return new ResponseEntity<List<Resource<RoomReservation>>>(HATEOASImplementorHotel.createRoomReservationList(roomReservationService.findAll(pageable)), HttpStatus.OK);
	}
	
	//Kreiranje rezeracije
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše rezervaciju sobe.", notes = "Povratna vrednost servisa je sačuvana rezervacija sobe.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = RoomReservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN')")
	public ResponseEntity<Resource<RoomReservation>> createRoomReservation(@Valid @RequestBody RoomReservation roomReservation) {
		return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(roomReservationService.save(roomReservation)), HttpStatus.CREATED);
	}
	
	//Kreiranje rezeracije
	@RequestMapping(value="/create-with-room/{id}",method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše rezervaciju sobe.", notes = "Povratna vrednost servisa je sačuvana rezervacija sobe.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = RoomReservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("(hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToRoom(#roomReservationId)) OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<RoomReservation>> createRoomReservationWithRoomId(@PathVariable(value="id") Long roomReservationId, 
			@RequestParam("begin") Long begin, @RequestParam("end") Long end) {
		return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(roomReservationService.saveWithRoomId(roomReservationId,new Date(begin),new Date(end))), HttpStatus.CREATED);
	}
	
	//Vraca rezervaciju sobe sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća rezervaciju sobe sa zadatim ID-em.", notes = "Povratna vrednost metode je rezervacija sobe koja ima zadati ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RoomReservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<RoomReservation>> getRoomReservationById(@PathVariable(value="id") Long roomReservationId) {
			return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(roomReservationService.findById(roomReservationId)), HttpStatus.OK);
	}
	
	//Brisanje rezervacij sobe sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše rezervacije sobe.", notes = "Briše rezervaciju sobe sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToRoomReservation(#roomReservationId)")
	public ResponseEntity<?> deleteRoomReservationWithId(@PathVariable(value="id") Long roomReservationId){			
		roomReservationService.deleteById(roomReservationId);
		return ResponseEntity.ok().build();
	}
	
	//Update rezervacija sobe sa zadatim id-em
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update rezervacije sobe.", notes = "Ažurira rezervaciju sobe sa zadatim ID-em na osnovu prosleđene  rezervacije sobe."
			+ " Kolekcije originalnog rezervacija sobe ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RoomReservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToRoomReservation(#roomReservationId)")
	public ResponseEntity<Resource<RoomReservation>> updateRoomResrvationWithId(@PathVariable(value = "id") Long roomReservationId,
			@Valid @RequestBody RoomReservation newRoomReservation) {
			return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(roomReservationService.updateRoomReservationById(roomReservationId, newRoomReservation)), HttpStatus.OK);
	}
	
	//Vraca extra option-e za datu rezervaciju sobe
	@RequestMapping(value = "/{id}/extra-options", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća extra optione za datu rezervaciju.", notes = "Povratna vrednost servisa je lista extra option-a.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<ExtraOption>>> getExtraOptionsForRoomReservationWithId(@PathVariable(value = "id") Long roomReservationId) {
				return new ResponseEntity<List<Resource<ExtraOption>>>(HATEOASImplementorHotel.createExtraOptionList(roomReservationService.getExtraOptions(roomReservationId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/extra-options", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira extra option za zadatu rezervaciju.", notes = "Povratna vrednost metode je kreirana extraoption.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = ExtraOption.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToRoomReservation(#roomReservationId)")
	public ResponseEntity<Resource<ExtraOption>> createExtraOptionForRoomReservationWithId(@PathVariable(value = "id") Long roomReservationId,
			@Valid @RequestBody ExtraOption extraOption) {
			return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(roomReservationService.createExtraOption(roomReservationId, extraOption)), HttpStatus.CREATED);
	}
	
	//vraca sobu od prosledjene rezervacije
	@RequestMapping(value = "/{id}/room", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraca sobu.", notes = "Povratna vrednost soba od prosledjene rezervacije", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Room.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found")
	})
	public ResponseEntity<Resource<Room>> getRoomForRoomReservationWithId(@PathVariable("id") Long roomReservationId) {
				return new ResponseEntity<Resource<Room>>(HATEOASImplementorHotel.createRoom(roomReservationService.getRoom(roomReservationId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/options", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<ExtraOption>> addExtraOptionToRoomReservationWithId(@PathVariable("id") Long id, @RequestParam("option") Long optionId) {
		return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(roomReservationService.addExtraOption(id, optionId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/options", method = RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('REGULAR_USER')")
	public ResponseEntity<?> removeExtraOptionFromRoomReservation(@PathVariable("id") Long id, @RequestParam("option") Long optionId) {
		roomReservationService.removeExtraOption(id, optionId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/quicks", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToRoom(#roomId)")
	public ResponseEntity<Resource<RoomReservation>> createQuickRoomReservation(@RequestParam("room") Long roomId, @RequestBody @Valid RoomReservation reservation) {
		return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(roomReservationService.saveQuickRoomReservation(reservation, roomId)), HttpStatus.CREATED);
	}
		
	@RequestMapping(value = "/{id}/multiple_extra_options", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("(hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToRoomReservation(#roomReservationId)) OR hasAuthority('REGULAR_USER')")
	public ResponseEntity<Resource<RoomReservation>> addMultipleExtraOptionsToRoomReservationWithId(@PathVariable("id") Long roomReservationId, @RequestBody List<Long> extraOptions) {
		return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(roomReservationService.addMultipleExtraOptionsToRoomReservation(roomReservationId, extraOptions)), HttpStatus.CREATED);

	}
	
}
