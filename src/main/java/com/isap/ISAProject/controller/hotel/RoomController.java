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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.service.hotel.RoomService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/rooms")
public class RoomController {
	
	@Autowired
	RoomService roomService;
	
	//Vraca sve sobe
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sobe.", notes = "Povratna vrednost metode je lista soba"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<Room>>> getAllRooms(Pageable pageable){
			return new ResponseEntity<List<Resource<Room>>>(HATEOASImplementorHotel.createRoomList(roomService.findAll(pageable)), HttpStatus.OK);
	}
	
	//Kreiranje sobe
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše sobu.", notes = "Povratna vrednost servisa je sačuvana soba.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = Room.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Room>> createRoom(@Valid @RequestBody Room room) {
		return new ResponseEntity<Resource<Room>>(HATEOASImplementorHotel.createRoom(roomService.save(room)), HttpStatus.CREATED);
	}
	
	//Vraca sobu sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sobu sa zadatim ID-em.", notes = "Povratna vrednost metode je soba koja ima zadati ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Room.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Room>> getRoomById(@PathVariable(value="id") Long roomId) {
			return new ResponseEntity<Resource<Room>>(HATEOASImplementorHotel.createRoom(roomService.findById(roomId)), HttpStatus.OK);
	}
	
	//Brisanje sobe sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše sobu.", notes = "Briše sobu sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteRoomWithId(@PathVariable(value="id") Long roomId){
		roomService.deleteById(roomId);
		return ResponseEntity.ok().build();
	}
	
	//Update sobe sa zadatim id-em
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update sobe.", notes = "Ažurira sobu sa zadatim ID-em na osnovu prosleđenog sobe. Kolekcije originalnog soba ostaju netaknute.",
			httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Room.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Room>> updateRoomWithId(@PathVariable(value = "id") Long roomId,
			@Valid @RequestBody Room newRoom) {
			return new ResponseEntity<Resource<Room>>(HATEOASImplementorHotel.createRoom(roomService.updateRoomById(roomId, newRoom)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/type", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Room>> setRoomTypeForRoom(@PathVariable("id") Long id, @RequestParam("roomtype") Long roomTypeId) {
		return new ResponseEntity<Resource<Room>>(HATEOASImplementorHotel.createRoom(roomService.setRoomTypeForRoom(roomTypeId, id)), HttpStatus.OK);
	}
	
	//Vraca rezervacije za datu sobu
	@RequestMapping(value = "/{id}/reservations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća rezervacije za datu sobu.", notes = "Povratna vrednost servisa je lista rezervacija.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<RoomReservation>>> getRoomReservationsForRoomWithId(@PathVariable(value = "id") Long roomId) {
				return new ResponseEntity<List<Resource<RoomReservation>>>(HATEOASImplementorHotel.createRoomReservationList(roomService.getRoomReservations(roomId)), HttpStatus.OK);	
	}
	
	//Kreira rezervaciju za sobu
	@RequestMapping(value = "/{id}/rooms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira rezervaciju za sobu", notes = "Povratna vrednost metode je kreirana rezervacija.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Floor.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<RoomReservation>> createRoomReservationForRoomWithId(@PathVariable(value = "id") Long roomId,
			@Valid @RequestBody RoomReservation roomReservation) {
			return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(roomService.createRoomReservation(roomId, roomReservation)), HttpStatus.CREATED);
	}
	
	//vraca hotel od prosledjene sobe
	@RequestMapping(value = "/{id}/hotel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraca hotel.", notes = "Povratna vrednost servisa je hotel kome soba pripada", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found")
	})
	public ResponseEntity<Resource<Hotel>> getHotelForRoomWithId(@PathVariable("id") Long roomId) {
				return new ResponseEntity<Resource<Hotel>>(HATEOASImplementorHotel.createHotel(roomService.getHotel(roomId)), HttpStatus.OK);
	}
	
	//vraca sprat od prosledjene sobe
	@RequestMapping(value = "/{id}/floor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraca sprat.", notes = "Povratna vrednost servisa je sprat kome soba pripada", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Floor.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found")
	})
	public ResponseEntity<Resource<Floor>> getFloorForRoomWithId(@PathVariable("id") Long roomId) {
				return new ResponseEntity<Resource<Floor>>(HATEOASImplementorHotel.createFloor(roomService.getFloor(roomId)), HttpStatus.OK);
	}
	
	//vraca tip sobe od prosledjene sobe
	@RequestMapping(value = "/{id}/room-type", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraca tip sobe.", notes = "Povratna vrednost servisa je tip sobe od prosledjene sobe", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RoomType.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found")
	})
	public ResponseEntity<Resource<RoomType>> getRoomTypeForRoomWithId(@PathVariable("id") Long roomId) {
				return new ResponseEntity<Resource<RoomType>>(HATEOASImplementorHotel.createRoomType(roomService.getRoomType(roomId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pretraga soba", responseContainer = "List", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Room>>> search(Pageable pageable, 
			@RequestParam(value="hotelId", required=true) Long hotelId,
			@RequestParam(value="roomTypeId", required=true) Long roomTypeId){
		List<Room> ret = roomService.searchWithHotelAndRoomType(pageable, hotelId, roomTypeId);
		return new ResponseEntity<List<Resource<Room>>>(HATEOASImplementorHotel.createRoomList(ret), HttpStatus.OK);
	}
	@RequestMapping(value = "/{id}/floor", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Room>> setFloorForRoomWithId(@PathVariable("id") Long roomId, @RequestParam("floor") Long floorId) {
		return new ResponseEntity<Resource<Room>>(HATEOASImplementorHotel.createRoom(roomService.setFloor(roomId, floorId)), HttpStatus.OK);
	}
	
}
