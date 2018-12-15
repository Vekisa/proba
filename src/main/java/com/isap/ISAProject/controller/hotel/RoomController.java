package com.isap.ISAProject.controller.hotel;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.exception.ResourceNotFoundException;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.repository.repository.hotel.RoomRepository;

@RestController
@RequestMapping("/rooms")
public class RoomController {
	@Autowired
	RoomRepository roomRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Room> getAllRooms(){
		return roomRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Room createRoom(@Valid @RequestBody Room room) {
		return roomRepository.save(room);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public Room getRoomById(@PathVariable(value="id") Long roomId) {
		return roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteRoom(@PathVariable(value="id") Long roomId){
		Room room = roomRepository.findById(roomId).orElseThrow(() -> 
			new ResourceNotFoundException("Room", "id", roomId));
		
		roomRepository.delete(room);
		
		return ResponseEntity.ok().build();
	}	
}
