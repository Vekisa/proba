package hotelInterf;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.hotel.RoomType;

public interface RoomServiceInterface {

	Room findById(long id);
	
	List<Room> findAll(Pageable pageable);
	
	Room save(Room room);
	
	void deleteById(long id);
	
	Room updateRoomById(Long roomId, Room newRoom);
	
	boolean checkIfRoomIsReserved(Room oldRoom);
	
	List<RoomReservation> getRoomReservations(Long id);
	
	RoomReservation createRoomReservation(Long roomId, RoomReservation roomReservation);
	
	Hotel getHotel(Long roomId);
	
	Floor getFloor(Long roomId);
	
	RoomType getRoomType(Long roomId);
	
	boolean checkIfRoomIsFree(Date start, Date end, Long roomId);
	
	Room setRoomTypeForRoom(Long roomTypeId, Long id);
	
	RoomType findRoomType(Long roomTypeId);
	
	List<Room> searchWithHotelAndRoomType(Pageable pageable, Long hotelId, Long roomTypeId);
	
	Room setFloor(Long roomId, Long floorId);
	
}
