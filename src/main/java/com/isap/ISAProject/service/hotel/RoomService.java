package com.isap.ISAProject.service.hotel;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.repository.hotel.RoomRepository;

@Service
@Transactional(readOnly = true)
public class RoomService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoomRepository roomRepository;
	
	public Room findById(long id) {
		logger.info("> Room findById id:{}", id);
		Optional<Room> room = roomRepository.findById(id);
		logger.info("< Room findById id:{}", id);
		if(room.isPresent())
			return room.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel sa zadatim id-em ne postoji");
	}
	
	public List<Room> findAll(Pageable pageable) {
		logger.info("> Room findAll");
		Page<Room> rooms = roomRepository.findAll(pageable);
		logger.info("< Room findAll");
		if(!rooms.isEmpty())
			return rooms.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hoteli ne postoje");
	}
	
	public Room save(Room room) {
		logger.info("> Room create");
		Room savedRoom = roomRepository.save(room);
		logger.info("< Room create");
		return savedRoom;
	}
	
	public void deleteById(long id) {
		logger.info("> Room delete");
		this.findById(id);
		roomRepository.deleteById(id);
		logger.info("< Room delete");
	}
	
	public Room updateRoomById(Long roomId, Room newRoom) {
		logger.info("> Room update");
		Room oldRoom = this.findById(roomId);
		oldRoom.copyFieldsFrom(newRoom);
		roomRepository.save(oldRoom);
		logger.info("< Room update");
		return oldRoom;
	}
	
	public List<RoomReservation> getRoomReservations(Long id){
		logger.info("> get room reservations for room");
		Room room = this.findById(id);
		List<RoomReservation> roomReservationList = room.getRoomReservation();
		logger.info("< get room reservations for room");
		if(!roomReservationList.isEmpty())
			return roomReservationList;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacije za datu sobu ne postoje");
	}
	
	public RoomReservation createRoomReservation(Long roomId, RoomReservation roomReservation){
		logger.info("> create room reservation for room");
		Room room = this.findById(roomId);
		room.getRoomReservation().add(roomReservation);
		roomReservation.setRoom(room);
		this.save(room);
		logger.info("< create room reservation for room");
		return roomReservation;
	}

}
