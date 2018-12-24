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
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.repository.repository.hotel.FloorRepository;

@RestController
@RequestMapping("/floors")
public class FloorController {
	@Autowired
	FloorRepository floorRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Floor> getAllFloors(){
		return floorRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Floor createFloor(@Valid @RequestBody Floor floor) {
		return floorRepository.save(floor);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public Floor getFloorById(@PathVariable(value="id") Long floorId) {
		return floorRepository.findById(floorId).orElseThrow(() -> new ResourceNotFoundException("id: " + floorId));
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteFloor(@PathVariable(value="id") Long floorId){
		Floor floor = floorRepository.findById(floorId).orElseThrow(() -> 
			new ResourceNotFoundException("id: " + floorId));
		
		floorRepository.delete(floor);
		
		return ResponseEntity.ok().build();
	}	
}
