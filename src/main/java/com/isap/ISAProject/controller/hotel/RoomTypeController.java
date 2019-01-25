package com.isap.ISAProject.controller.hotel;

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
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.service.hotel.RoomTypeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/room_types")
public class RoomTypeController {
	
	@Autowired
	RoomTypeService roomTypeService;
	
	//Lista svih tipova soba
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća tipove soba.", notes = "Povratna vrednost metode je lista tipova soba"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<RoomType>>> getAllRoomTypes(Pageable pageable){
			return new ResponseEntity<List<Resource<RoomType>>>(HATEOASImplementorHotel.createRoomTypeList(roomTypeService.findAll(pageable)), HttpStatus.OK);
	}
	
	//Kreiranje tipa sobe
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše tip sobe.", notes = "Povratna vrednost servisa je sačuvan tip sobe.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = RoomType.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<RoomType>> createRoomType(@Valid @RequestBody RoomType roomType) {
		return new ResponseEntity<Resource<RoomType>>(HATEOASImplementorHotel.createRoomType(roomTypeService.save(roomType)), HttpStatus.CREATED);
	}
	
	//Vraca tip sobe sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća tip sobe sa zadatim ID-em.", notes = "Povratna vrednost metode je tip sobe koji ima zadati ID.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RoomType.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<RoomType>> getRoomTypeById(@PathVariable(value="id") Long roomTypeId) {
			return new ResponseEntity<Resource<RoomType>>(HATEOASImplementorHotel.createRoomType(roomTypeService.findById(roomTypeId)), HttpStatus.OK);
	}
	
	//Brisanje tipa sobe sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše tip sobe.", notes = "Briše tip sobe sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteRoomTypeWithId(@PathVariable(value="id") Long roomTypeId){
		roomTypeService.deleteById(roomTypeId);
		return ResponseEntity.ok().build();
	}
	
	//Update tip sobe sa zadatim id-em
		@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiOperation(value = "Update tipa sobe.", notes = "Ažurira tip sobe sa zadatim ID-em na osnovu prosleđenog tipa sobe."
				+ " Kolekcije originalnog tipa sobe ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
		@ApiResponses(value = {
				@ApiResponse(code = 200, message = "OK", response = RoomType.class),
				@ApiResponse(code = 204, message = "No Content"),
				@ApiResponse(code = 400, message = "Bad Request")
		})
		public ResponseEntity<Resource<RoomType>> updateRoomTypenWithId(@PathVariable(value = "id") Long roomTypeId,
				@Valid @RequestBody RoomType newRoomType) {

				return new ResponseEntity<Resource<RoomType>>(HATEOASImplementorHotel.createRoomType(roomTypeService.updateRoomTypeById(roomTypeId, newRoomType)), HttpStatus.OK);	
		}
}
