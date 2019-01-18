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

import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.repository.hotel.HotelRepository;

@Service
@Transactional(readOnly = true)
public class HotelService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private HotelRepository hotelRepository;
	
	public Optional<Hotel> findById(long id) {
		
		logger.info("> findById id:{}", id);
		Optional<Hotel> hotel = hotelRepository.findById(id);
		logger.info("< findById id:{}", id);
		return hotel;
	}
	
	public Page<Hotel> findAll(Pageable pageable) {
		
		logger.info("> findAll");
		Page<Hotel> hotels = hotelRepository.findAll(pageable);
		logger.info("< findAll");
		return hotels;
	}
	
	@Transactional(readOnly = false)
	public Hotel save(Hotel hotel) {
		logger.info("> create");
		Hotel savedHotel = hotelRepository.save(hotel);
		logger.info("< create");
		return savedHotel;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteById(long id) {
		logger.info("> delete");
		hotelRepository.deleteById(id);
		logger.info("< delete");
	}
	
}
