package com.isap.ISAProject.serviceInterface.hotel;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;

public interface RoomReservationServiceInterface {

	RoomReservation findById(long id);
	
	List<RoomReservation> findAll(Pageable pageable);
	
	RoomReservation save(RoomReservation roomReservation);
	
	void deleteById(long id);
	
	RoomReservation updateRoomReservationById(Long roomReservationId, RoomReservation newRoomReservation);
	
	List<ExtraOption> getExtraOptions(Long id);
	
	ExtraOption createExtraOption(Long roomReservationId, ExtraOption extraOption);
	
	ExtraOption addExtraOption(Long id, Long extraOptionId);
	
	void recalculateReservationPrice(RoomReservation roomReservation);
	
	void removeExtraOption(Long id, Long extraOptionId);
	
	ExtraOption findExtraOption(Long extraOptionId);
	
	Room getRoom(Long roomReservationId); 
	
	RoomReservation saveWithRoomId(Long roomId, Date begin, Date end);
	
	boolean checkIfRoomIsFree(Date start, Date end, Room room);
}
