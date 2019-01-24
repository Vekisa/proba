package service.hotel;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.repository.hotel.RoomRepository;

@Service
@Transactional(readOnly = true)
public class RoomService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoomRepository roomRepository;
	
	public Optional<Room> findById(long id) {	
		logger.info("> findById id:{}", id);
		Optional<Room> room = roomRepository.findById(id);
		logger.info("< findById id:{}", id);
		return room;
	}
	
	public Page<Room> findAll(Pageable pageable) {
		logger.info("> findAll");
		Page<Room> rooms = roomRepository.findAll(pageable);
		logger.info("< findAll");
		return rooms;
	}
	
	@Transactional(readOnly = false)
	public Room save(Room room) {
		logger.info("> create");
		Room savedRoom = roomRepository.save(room);
		logger.info("< create");
		return savedRoom;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteById(long id) {
		logger.info("> delete");
		roomRepository.deleteById(id);
		logger.info("< delete");
	}

}
