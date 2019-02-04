package com.isap.ISAProject.service.hotel;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.repository.hotel.FloorRepository;
import com.isap.ISAProject.repository.hotel.RoomRepository;
import com.isap.ISAProject.repository.hotel.RoomSpecifications;
import com.isap.ISAProject.repository.hotel.RoomTypeRepository;

@Service
@Transactional(readOnly = true)
public class RoomService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private RoomTypeRepository roomTypeRepository;
	
	@Autowired
	private FloorRepository floorRepository;
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Room findById(long id) {
		logger.info("> Room findById id:{}", id);
		Optional<Room> room = roomRepository.findById(id);
		logger.info("< Room findById id:{}", id);
		if(room.isPresent())
			return room.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Soba sa zadatim id-em ne postoji");
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Room> findAll(Pageable pageable) {
		logger.info("> Room findAll");
		Page<Room> rooms = roomRepository.findAll(pageable);
		logger.info("< Room findAll");
		if(!rooms.isEmpty())
			return rooms.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sobe ne postoje");
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Room save(Room room) {
		logger.info("> Room create");
		Room savedRoom = roomRepository.save(room);
		logger.info("< Room create");
		return savedRoom;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteById(long id) {
		logger.info("> Room delete");
		Room room = this.findById(id);
		if(this.checkIfRoomIsReserved(room))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Soba je rezervisana i ne moze se brisat.");
		roomRepository.deleteById(id);
		logger.info("< Room delete");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public Room updateRoomById(Long roomId, Room newRoom) {
		logger.info("> Room update");
		Room oldRoom = this.findById(roomId);
		if(this.checkIfRoomIsReserved(oldRoom))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Soba je rezervisana i ne moze se menjati.");
		oldRoom.setNumberOfBeds(newRoom.getNumberOfBeds());
		roomRepository.save(oldRoom);
		logger.info("< Room update");
		return oldRoom;
	}
	
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	private boolean checkIfRoomIsReserved(Room oldRoom) {
		Date current = new Date();
		for(RoomReservation r : oldRoom.getRoomReservations())
			if(r.getBeginDate().after(current) || r.getEndDate().after(current))
				return true;
		return false;
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<RoomReservation> getRoomReservations(Long id){
		logger.info("> get room reservations for room");
		Room room = this.findById(id);
		List<RoomReservation> roomReservationList = room.getRoomReservations();
		logger.info("< get room reservations for room");
		if(!roomReservationList.isEmpty())
			return roomReservationList;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacije za datu sobu ne postoje");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public RoomReservation createRoomReservation(Long roomId, RoomReservation roomReservation){
		logger.info("> create room reservation for room");
		Room room = this.findById(roomId);
		if(roomReservation.getBeginDate().after(roomReservation.getEndDate()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datum rezervacije je lose unesen");
		if(!checkIfRoomIsFree(roomReservation.getBeginDate(), roomReservation.getEndDate(), room))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soba nije slobodna u tom periodu");
		room.getRoomReservations().add(roomReservation);
		roomReservation.setRoom(room);
		Long difference = roomReservation.getEndDate().getTime() - roomReservation.getBeginDate().getTime();
		roomReservation.setNumberOfNights((int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS));
		roomReservation.setPrice(room.getRoomType().getPricePerNight() * roomReservation.getNumberOfNights());
		this.save(room);
		logger.info("< create room reservation for room");
		return roomReservation;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
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
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
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
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
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
		for(RoomReservation roomReservation :room.getRoomReservations()) {
			reservedStart = roomReservation.getBeginDate();
			reservedEnd = roomReservation.getEndDate();
			if((start.after(reservedStart) && start.before(reservedEnd)) || (end.after(reservedStart) && end.before(reservedEnd)))
				return false;
		}
		return true;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Room setRoomTypeForRoom(Long roomTypeId, Long id) {
		logger.info("> setting room type for room {}", id);
		Room room = this.findById(id);
		RoomType type = this.findRoomType(roomTypeId);
		if(this.checkIfRoomIsReserved(room))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Soba je rezervisana i ne moze se menjati.");
		if(!type.getCatalogue().getHotels().contains(room.getFloor().getHotel()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room and type don't belong to the same hotel.");
		room.setRoomType(type);
		this.save(room);
		logger.info("< room type set");
		return room;
	}

	private RoomType findRoomType(Long roomTypeId) {
		logger.info("> fetching room type with id {}", roomTypeId);
		Optional<RoomType> type = roomTypeRepository.findById(roomTypeId);
		logger.info("< room type fetched");
		if(type.isPresent()) return type.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request room type doesn't exist.");
	}
	
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Room> searchWithHotelAndRoomType(Pageable pageable, Long hotelId, Long roomTypeId) {
		logger.info("> searching rooms for specific hotel and room type");
		List<Room> rooms = roomRepository.findAll(RoomSpecifications.findByHotelRoomType(hotelId, roomTypeId));
		logger.info("hotels: " + rooms);
		logger.info("< rooms found");
		return rooms;
	}

	@Transactional(readOnly = false)
	public Room setFloor(Long roomId, Long floorId) {
		logger.info("> setting floor to room with id {}", roomId);
		Room room = this.findById(roomId);
		if(this.checkIfRoomIsReserved(room))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Soba je rezervisana i ne moze se menjati.");
		Optional<Floor> floor = floorRepository.findById(floorId);
		if(floor.isPresent()) {
			room.setFloor(floor.get());
			logger.info("< floor set");
			this.save(room);
			return room;
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request floor doesn't exist.");
	}
}
