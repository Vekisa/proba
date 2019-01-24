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
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.repository.hotel.RoomTypeRepository;

@Service
@Transactional(readOnly = true)
public class RoomTypeService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoomTypeRepository roomTypeRepository;
	
	public Optional<RoomType> findById(long id) {	
		logger.info("> findById id:{}", id);
		Optional<RoomType> roomType = roomTypeRepository.findById(id);
		logger.info("< findById id:{}", id);
		return roomType;
	}
	
	public Page<RoomType> findAll(Pageable pageable) {
		logger.info("> findAll");
		Page<RoomType> roomTypes = roomTypeRepository.findAll(pageable);
		logger.info("< findAll");
		return roomTypes;
	}
	
	@Transactional(readOnly = false)
	public RoomType save(RoomType roomType) {
		logger.info("> create");
		RoomType savedRoomType = roomTypeRepository.save(roomType);
		logger.info("< create");
		return savedRoomType;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteById(long id) {
		logger.info("> delete");
		roomTypeRepository.deleteById(id);
		logger.info("< delete");
	}
}
