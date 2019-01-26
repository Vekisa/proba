package com.isap.ISAProject.service.hotel;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.hotel.RoomType;
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
		if(roomReservation.getBeginDate().after(roomReservation.getEndDate()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datum rezervacije je lose unesen");
		if(!checkIfRoomIsFree(roomReservation.getBeginDate(), roomReservation.getEndDate(), room))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soba nije slobodna u tom periodu");
		room.getRoomReservation().add(roomReservation);
		roomReservation.setRoom(room);
		this.save(room);
		logger.info("< create room reservation for room");
		return roomReservation;
	}
	
	public Hotel getHotel(Long roomId) {
		logger.info("> hotel from room", roomId);
		Room room = this.findById(roomId);
		Floor floor = room.getFloor();
		if(floor == null) {
			logger.info("< hotel from floor");
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Sprat za prosledjenu sobu nije postavljen");
		}
		Hotel hotel = floor.getHotel();
		logger.info("< hotel from room");
		if(hotel != null)
			return hotel;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Hotel za datu sobu nije postavljen");
	}
	
	public Floor getFloor(Long roomId) {
		logger.info("> floor from room", roomId);
		Room room = this.findById(roomId);
		Floor floor = room.getFloor();
		logger.info("< floor from room");
		if(floor != null)
			return floor;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Sprat za datu sobu nije postavljen");
	}
	
	public RoomType getRoomType(Long roomId) {
		logger.info("> room-type from room", roomId);
		Room room = this.findById(roomId);
		RoomType roomType = room.getRoomType();
		logger.info("< room-type from room");
		if(roomType != null)
			return roomType;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Tip sobe za datu sobu nije postavljen");
	}
	
	public boolean checkIfRoomIsFree(Date start, Date end, Room room) {
		Date reservedStart = null;
		Date reservedEnd = null;
		for(RoomReservation roomReservation :room.getRoomReservation()) {
			reservedStart = roomReservation.getBeginDate();
			reservedEnd = roomReservation.getEndDate();
			if((start.after(reservedStart) && start.before(reservedEnd)) || (end.after(reservedStart) && end.before(reservedEnd)))
				return false;
		}
		return true;
	}

}
