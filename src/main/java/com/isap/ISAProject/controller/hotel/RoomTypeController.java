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

import com.isap.ISAProject.exception.ResourceNotFoundException;
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.repository.repository.hotel.RoomTypeRepository;

@RestController
@RequestMapping("/room_types")
public class RoomTypeController {
	@Autowired
	RoomTypeRepository roomTypeRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<RoomType> getAllRoomTypes(){
		return roomTypeRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public RoomType createRoomType(@Valid @RequestBody RoomType roomType) {
		return roomTypeRepository.save(roomType);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public RoomType getRoomTypeById(@PathVariable(value="id") Long roomTypeId) {
		return roomTypeRepository.findById(roomTypeId).orElseThrow(() -> 
			new ResourceNotFoundException("id: " + roomTypeId));
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteRoomType(@PathVariable(value="id") Long roomTypeId){
		RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() -> 
			new ResourceNotFoundException("id: " + roomTypeId));
		
		roomTypeRepository.delete(roomType);
		
		return ResponseEntity.ok().build();
	}	
}
