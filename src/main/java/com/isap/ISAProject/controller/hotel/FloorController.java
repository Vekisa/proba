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

import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.service.hotel.FloorService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/floors")
public class FloorController {
	@Autowired
	FloorService floorService;
	
	//Vraca sve spratove
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća spratove.", notes = "Povratna vrednost metode je lista spratova"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<Floor>>> getAllFloors(Pageable pageable){
			return new ResponseEntity<List<Resource<Floor>>>(HATEOASImplementorHotel.createFloorList(floorService.findAll(pageable)), HttpStatus.OK);
	}
	
	//Kreiranje sprata
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše sprat.", notes = "Povratna vrednost servisa je sačuvan sprat.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = Floor.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Floor>> createFloor(@Valid @RequestBody Floor floor) {
		return new ResponseEntity<Resource<Floor>>(HATEOASImplementorHotel.createFloor(floorService.save(floor)), HttpStatus.CREATED);
	}
	

	//Vraca sprat sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sprat sa zadatim ID-em.", notes = "Povratna vrednost metode je sprat koji ima zadati ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Floor.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Floor>> getFloorById(@PathVariable(value="id") Long floorId) {
			return new ResponseEntity<Resource<Floor>>(HATEOASImplementorHotel.createFloor(floorService.findById(floorId)), HttpStatus.OK);
	}
	
	//Brisanje sprata sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše sprat.", notes = "Briše sprat sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteFloorWithId(@PathVariable(value="id") Long floorId){
		floorService.deleteById(floorId);
		return ResponseEntity.ok().build();
	}
	
	//Update sprata sa zadatim id-em
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update sprata.", notes = "Ažurira sprat sa zadatim ID-em na osnovu prosleđenog sprata. Kolekcije originalnog sparata ostaju netaknute.",
			httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Floor.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Floor>> updateFloorWithId(@PathVariable(value = "id") Long floorId,
			@Valid @RequestBody Floor newFloor) {
			return new ResponseEntity<Resource<Floor>>(HATEOASImplementorHotel.createFloor(floorService.updateFloorById(floorId, newFloor)), HttpStatus.OK);
	}
	
	//Vraca sobe za dati sprat
	@RequestMapping(value = "/{id}/rooms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sobe za dati sprat.", notes = "Povratna vrednost servisa je lista soba.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<Room>>> getRoomsForFloorWithId(@PathVariable(value = "id") Long floorId) {
				return new ResponseEntity<List<Resource<Room>>>(HATEOASImplementorHotel.createRoomList(floorService.getRooms(floorId)), HttpStatus.OK);	
	}
	
	//Kreira sobu na spratu
	@RequestMapping(value = "/{id}/rooms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira sobu na spratu", notes = "Povratna vrednost metode je kreirana soba.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Floor.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Room>> createRoomForFloorWithId(@PathVariable(value = "id") Long floorId,
			@Valid @RequestBody Room room) {
			return new ResponseEntity<Resource<Room>>(HATEOASImplementorHotel.createRoom(floorService.createRoom(floorId, room)), HttpStatus.CREATED);	
	}
}
