package com.isap.ISAProject.serviceInterface.hotel;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;

public interface FloorServiceInterface {

	Floor findById(long id);
	
	List<Floor> findAll(Pageable pageable);
	
	Floor save(Floor floor);
	
	void deleteById(long id);
	
	Floor updateFloorById(Long floorId, Floor newFloor);
	
	List<Room> getRooms(Long id);
	
	Room createRoom(Long floorId, Room room);
	
	Hotel getHotel(Long floorId);
}
