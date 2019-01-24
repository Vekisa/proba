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

import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.repository.hotel.RoomReservationRepository;

@Service
@Transactional(readOnly = true)
public class RoomReservationService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoomReservationRepository roomReservationRepository;
	
	public Optional<RoomReservation> findById(long id) {	
		logger.info("> findById id:{}", id);
		Optional<RoomReservation> roomReservation = roomReservationRepository.findById(id);
		logger.info("< findById id:{}", id);
		return roomReservation;
	}
	
	public Page<RoomReservation> findAll(Pageable pageable) {
		logger.info("> findAll");
		Page<RoomReservation> roomReservations = roomReservationRepository.findAll(pageable);
		logger.info("< findAll");
		return roomReservations;
	}
	
	@Transactional(readOnly = false)
	public RoomReservation save(RoomReservation roomReservation) {
		logger.info("> create");
		RoomReservation savedRoomReservation = roomReservationRepository.save(roomReservation);
		logger.info("< create");
		return savedRoomReservation;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteById(long id) {
		logger.info("> delete");
		roomReservationRepository.deleteById(id);
		logger.info("< delete");
	}
}
