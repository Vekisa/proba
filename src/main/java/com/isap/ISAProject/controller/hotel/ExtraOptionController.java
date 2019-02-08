package com.isap.ISAProject.controller.hotel;

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
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.service.hotel.ExtraOptionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/extra_options")
public class ExtraOptionController {
	
	@Autowired
	ExtraOptionService extraOptionService;
	
	//Lista svih extra-optiona
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća extra-optione.", notes = "Povratna vrednost metode je lista extra-optiona"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<ExtraOption>>> getAllExtraOptions(Pageable pageable){
			return new ResponseEntity<List<Resource<ExtraOption>>>(HATEOASImplementorHotel.createExtraOptionList(extraOptionService.findAll(pageable)), HttpStatus.OK);
	}
	
	//Kreiranje extra-optiona
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše extra option.", notes = "Povratna vrednost servisa je sačuvan extra option.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = ExtraOption.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN')")
	public ResponseEntity<Resource<ExtraOption>> createExtraOption(@Valid @RequestBody ExtraOption extraOption) {
		return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(extraOptionService.save(extraOption)), HttpStatus.CREATED);
	}
	

	//Vraca extra-option sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća extra-option sa zadatim ID-em.", notes = "Povratna vrednost metode je extra-option koji ima zadati ID.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = ExtraOption.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<ExtraOption>> getExtraOptionById(@PathVariable(value="id") Long extraOptionId) {
			return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(extraOptionService.findById(extraOptionId)), HttpStatus.OK);
	}
	
	//Brisanje extra-optiona sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše extra-option.", notes = "Briše extra-option sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToOption(#extraOptionId)")
	public ResponseEntity<?> deleteExtraOptionWithId(@PathVariable(value="id") Long extraOptionId){	
		extraOptionService.deleteById(extraOptionId);
		return ResponseEntity.ok().build();
	}
	
	//Update extra-optiona sa zadatim id-em
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update extra-optiona.", notes = "Ažurira extra-option sa zadatim ID-em na osnovu prosleđenog ectra-optiona."
			+ " Kolekcije originalnog extra-optiona ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = ExtraOption.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToOption(#extraOptionId)")
	public ResponseEntity<Resource<ExtraOption>> updateExtraOptionWithId(@PathVariable(value = "id") Long extraOptionId,
			@Valid @RequestBody ExtraOption newExtraOption) {
			return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(extraOptionService.updateExtraOptionById(extraOptionId, newExtraOption)), HttpStatus.OK);
	}
	
	//Vraca hotel u okviru extra-optiona
	@RequestMapping(value = "/{id}/hotel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća hotel", notes = "Povratna vrednost servisa je hotel",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Hotel>> getHotelForExtraOptionWithId(@PathVariable(value = "id") Long extraOptionId) {
				return new ResponseEntity<Resource<Hotel>>(HATEOASImplementorHotel.createHotel(extraOptionService.getHotel(extraOptionId)), HttpStatus.OK);
	}
	
	//Vraca hotel u okviru extra-optiona
	@RequestMapping(value = "/{id}/room-reservation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća rezervaciju sobe", notes = "Povratna vrednost servisa je rezervacija sobe",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RoomReservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<RoomReservation>>> getRoomReservationForExtraOptionWithId(@PathVariable(value = "id") Long extraOptionId) {
				return new ResponseEntity<List<Resource<RoomReservation>>>(HATEOASImplementorHotel.createRoomReservationList(extraOptionService.getRoomReservation(extraOptionId)), HttpStatus.OK);
	}
}
