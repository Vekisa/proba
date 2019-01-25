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

import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.repository.hotel.FloorRepository;

@Service
@Transactional(readOnly = true)
public class FloorService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FloorRepository floorRepository;
	
	public Floor findById(long id) {
		logger.info("> Floor findById id:{}", id);
		Optional<Floor> floor = floorRepository.findById(id);
		logger.info("< Floor findById id:{}", id);
		if(floor.isPresent())
			return floor.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel sa zadatim id-em ne postoji");
	}
	
	public List<Floor> findAll(Pageable pageable) {
		logger.info("> Floor findAll");
		Page<Floor> floors = floorRepository.findAll(pageable);
		logger.info("< Floor findAll");
		if(!floors.isEmpty())
			return floors.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hoteli ne postoje");
	}
	
	public Floor save(Floor floor) {
		logger.info("> Floor create");
		Floor savedFloor = floorRepository.save(floor);
		logger.info("< Floor create");
		return savedFloor;
	}
	
	public void deleteById(long id) {
		logger.info("> Floor delete");
		this.findById(id);
		floorRepository.deleteById(id);
		logger.info("< Floor delete");
	}
	
	public Floor updateFloorById(Long floorId, Floor newFloor) {
		logger.info("> Floor update");
		Floor oldFloor = this.findById(floorId);
		oldFloor.copyFieldsFrom(newFloor);
		floorRepository.save(oldFloor);
		logger.info("< Floor update");
		return oldFloor;
	}
	
	public List<Room> getRooms(Long id){
		logger.info("> get rooms for floor");
		Floor floor = this.findById(id);
		List<Room> roomList = floor.getRoom();
		logger.info("< get rooms for floor");
		if(!roomList.isEmpty())
			return roomList ;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sobe za dati sprat ne postoje");
	}
	
	public Room createRoom(Long floorId, Room room){
		logger.info("> create room for floor");
		Floor floor = this.findById(floorId);
		floor.getRoom().add(room);
		room.setFloor(floor);
		this.save(floor);
		logger.info("< create room for floor");
		return room;
	}

}