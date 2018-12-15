package controller.hotel;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import model.exception.ResourceNotFoundException;
import model.hotel.RoomReservation;
import repository.hotel.RoomReservationRepository;

@RestController
@RequestMapping("/room_reservations")
public class RoomReservationController {
	@Autowired
	RoomReservationRepository roomReservationRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<RoomReservation> getAllRoomReservations(){
		return roomReservationRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public RoomReservation createRoomReservation(@Valid @RequestBody RoomReservation roomReservation) {
		return roomReservationRepository.save(roomReservation);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public RoomReservation getRoomReservationById(@PathVariable(value="id") Long roomReservationId) {
		return roomReservationRepository.findById(roomReservationId).orElseThrow(() -> 
			new ResourceNotFoundException("RoomReservation", "id", roomReservationId));
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteRoomReservation(@PathVariable(value="id") Long roomReservationId){
		RoomReservation roomReservation = roomReservationRepository.findById(roomReservationId).orElseThrow(() -> 
			new ResourceNotFoundException("RoomReservation", "id", roomReservationId));
		
		roomReservationRepository.delete(roomReservation);
		
		return ResponseEntity.ok().build();
	}	
}
