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

import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.repository.hotel.FloorRepository;

@Service
@Transactional(readOnly = true)
public class FloorService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FloorRepository floorRepository;
	
public Optional<Floor> findById(long id) {
		
		logger.info("> findById id:{}", id);
		Optional<Floor> floor = floorRepository.findById(id);
		logger.info("< findById id:{}", id);
		return floor;
	}
	
	public Page<Floor> findAll(Pageable pageable) {
		
		logger.info("> findAll");
		Page<Floor> floors = floorRepository.findAll(pageable);
		logger.info("< findAll");
		return floors;
	}
	
	@Transactional(readOnly = false)
	public Floor save(Floor floor) {
		logger.info("> create");
		Floor savedFloor = floorRepository.save(floor);
		logger.info("< create");
		return savedFloor;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteById(long id) {
		logger.info("> delete");
		floorRepository.deleteById(id);
		logger.info("< delete");
	}

}
