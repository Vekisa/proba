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

import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.repository.hotel.RoomRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/rooms")
public class RoomController {
	@Autowired
	RoomRepository roomRepository;
	
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
		Page<Room> rooms = roomRepository.findAll(pageable); 
		if(rooms.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return new ResponseEntity<List<Resource<Room>>>(HATEOASImplementorHotel.createRoomList(rooms.getContent()), HttpStatus.OK);
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
		Room createdRoom =  roomRepository.save(room);
		return new ResponseEntity<Resource<Room>>(HATEOASImplementorHotel.createRoom(createdRoom), HttpStatus.CREATED);
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
		Optional<Room> room = roomRepository.findById(roomId);
		if(room.isPresent())
			return new ResponseEntity<Resource<Room>>(HATEOASImplementorHotel.createRoom(room.get()), HttpStatus.OK);
		else
			return ResponseEntity.noContent().build();
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
		if(!roomRepository.findById(roomId).isPresent())
			return ResponseEntity.notFound().build();
			
		roomRepository.deleteById(roomId);
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
		Optional<Room> oldRoom = roomRepository.findById(roomId);
		if(oldRoom.isPresent()) {
			oldRoom.get().copyFieldsFrom(newRoom);
			roomRepository.save(oldRoom.get());
			return new ResponseEntity<Resource<Room>>(HATEOASImplementorHotel.createRoom(oldRoom.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
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
		Optional<Room> room = roomRepository.findById(roomId);
		if(room.isPresent()) {
			List<RoomReservation> roomReservationList = room.get().getRoomReservation();
			if(roomReservationList.isEmpty())
				return ResponseEntity.noContent().build();
			else
				return new ResponseEntity<List<Resource<RoomReservation>>>(HATEOASImplementorHotel.createRoomReservationList(roomReservationList), HttpStatus.OK);
		}else {
			return ResponseEntity.notFound().build();
		}
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
		Optional<Room> room = roomRepository.findById(roomId);
		if(room.isPresent()) {
			room.get().add(roomReservation);
			roomRepository.save(room.get());
			return new ResponseEntity<Resource<RoomReservation>>(HATEOASImplementorHotel.createRoomReservation(roomReservation), HttpStatus.CREATED);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
