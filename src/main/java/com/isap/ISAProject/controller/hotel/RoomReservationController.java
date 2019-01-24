package com.isap.ISAProject.controller.hotel;

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

import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.RoomReservation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import service.hotel.RoomReservationService;

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
		Page<RoomReservation> roomReservations = roomReservationService.findAll(pageable); 
		if(roomReservations.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return new ResponseEntity<List<Resource<RoomReservation>>>(HATEOASImplementorHotel.createRoomReservationList(roomReservations.getContent()), HttpStatus.OK);
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
	public ResponseEntity<Resource<RoomReservation>> createRoomReservation(@Valid @RequestBody RoomReservation roomReservation) {
		RoomReservation createdRoomReservation =  roomReservationService.save(roomReservation);
		return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(createdRoomReservation), HttpStatus.CREATED);
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
		Optional<RoomReservation> roomReservation = roomReservationService.findById(roomReservationId);
		if(roomReservation.isPresent())
			return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(roomReservation.get()), HttpStatus.OK);
		else
			return ResponseEntity.noContent().build();
	}
	
	//Brisanje rezervacij sobe sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše rezervacije sobe.", notes = "Briše rezervaciju sobe sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteRoomReservationWithId(@PathVariable(value="id") Long roomReservationId){
		if(!roomReservationService.findById(roomReservationId).isPresent())
			return ResponseEntity.notFound().build();
			
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
	public ResponseEntity<Resource<RoomReservation>> updateRoomResrvationWithId(@PathVariable(value = "id") Long roomReservationId,
			@Valid @RequestBody RoomReservation newRoomReservation) {
		Optional<RoomReservation> oldRoomReservation = roomReservationService.findById(roomReservationId);
		if(oldRoomReservation.isPresent()) {
			oldRoomReservation.get().copyFieldsFrom(newRoomReservation);
			roomReservationService.save(oldRoomReservation.get());
			return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(oldRoomReservation.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
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
			Optional<RoomReservation> roomReservation = roomReservationService.findById(roomReservationId);
			if(roomReservation.isPresent()) {
				List<ExtraOption> extraOptionsList = roomReservation.get().getExtraOptions();
				if(extraOptionsList.isEmpty())
					return ResponseEntity.noContent().build();
				else
					return new ResponseEntity<List<Resource<ExtraOption>>>(HATEOASImplementorHotel.createExtraOptionList(extraOptionsList), HttpStatus.OK);
			}else {
				return ResponseEntity.notFound().build();
			}
		}
		
		@RequestMapping(value = "/{id}/extra-options", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
		@ApiOperation(value = "Kreira extra option za zadatu rezervaciju.", notes = "Povratna vrednost metode je kreirana extraoption.",
				httpMethod = "POST", produces = "application/json", consumes = "application/json")
		@ApiResponses(value = {
				@ApiResponse(code = 200, message = "OK", response = ExtraOption.class),
				@ApiResponse(code = 204, message = "No Content"),
				@ApiResponse(code = 400, message = "Bad Request")
		})
		public ResponseEntity<Resource<ExtraOption>> createExtraOptionForRoomReservationWithId(@PathVariable(value = "id") Long roomReservationId,
				@Valid @RequestBody ExtraOption extraOption) {
			Optional<RoomReservation> roomReservation = roomReservationService.findById(roomReservationId);
			if(roomReservation.isPresent()) {
				roomReservation.get().add(extraOption);
				roomReservationService.save(roomReservation.get());
				return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(extraOption), HttpStatus.CREATED);
			} else {
				return ResponseEntity.notFound().build();
			}
		}
}
