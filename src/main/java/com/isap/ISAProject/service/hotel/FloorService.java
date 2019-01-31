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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.repository.hotel.FloorRepository;
import com.isap.ISAProject.repository.hotel.RoomRepository;

@Service
@Transactional(readOnly = true)
public class FloorService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FloorRepository floorRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Floor findById(long id) {
		logger.info("> Floor findById id:{}", id);
		Optional<Floor> floor = floorRepository.findById(id);
		logger.info("< Floor findById id:{}", id);
		if(floor.isPresent())
			return floor.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sprat sa zadatim id-em ne postoji");
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Floor> findAll(Pageable pageable) {
		logger.info("> Floor findAll");
		Page<Floor> floors = floorRepository.findAll(pageable);
		logger.info("< Floor findAll");
		if(!floors.isEmpty())
			return floors.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hoteli ne postoje");
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Floor save(Floor floor) {
		logger.info("> Floor create");
		Floor savedFloor = floorRepository.save(floor);
		logger.info("< Floor create");
		return savedFloor;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteById(long id) {
		logger.info("> Floor delete");
		this.findById(id);
		floorRepository.deleteById(id);
		logger.info("< Floor delete");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public Floor updateFloorById(Long floorId, Floor newFloor) {
		logger.info("> Floor update");
		Floor oldFloor = this.findById(floorId);
		oldFloor.setNumber(newFloor.getNumber());
		floorRepository.save(oldFloor);
		logger.info("< Floor update");
		return oldFloor;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Room> getRooms(Long id){
		logger.info("> get rooms for floor");
		Floor floor = this.findById(id);
		List<Room> roomList = floor.getRooms();
		logger.info("< get rooms for floor");
		if(!roomList.isEmpty())
			return roomList ;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sobe za dati sprat ne postoje");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public Room createRoom(Long floorId, Room room){
		logger.info("> create room for floor");
		Floor floor = this.findById(floorId);
		room.setRating(0);
		room.setFloor(floor);
		roomRepository.save(room);
		logger.info("< create room for floor");
		return room;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public Hotel getHotel(Long floorId) {
		logger.info("> hotel from floor", floorId);
		Floor floor = this.findById(floorId);
		Hotel hotel = floor.getHotel();
		logger.info("< hotel from floor");
		if(hotel != null)
			return hotel;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Hotel za dati sprat nije postavljen");
	}

}