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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.repository.hotel.HotelRepository;

@Service
@Transactional(readOnly = true)
public class HotelService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private HotelRepository hotelRepository;
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Hotel findById(long id) {
		try {
			logger.info("> Hotel findById id:{}", id);
			Optional<Hotel> hotel = hotelRepository.findById(id);
			logger.info("< Hotel findById id:{}", id);
			if(hotel.isPresent())
				return hotel.get();
			else 
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel sa zadatim id-em ne postoji");
		}catch(Exception e) {
			//Neki response za los
			return null;
		}
	}
	
	public List<Hotel> findAll(Pageable pageable) {
		logger.info("> Hotel findAll");
		Page<Hotel> hotels = hotelRepository.findAll(pageable);
		logger.info("< Hotel findAll");
		if(!hotels.isEmpty())
			return hotels.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hoteli ne postoje");
	}
	
	@Transactional(readOnly = false)
	public Hotel save(Hotel hotel) {
		logger.info("> Hotel create");
		Hotel savedHotel = hotelRepository.save(hotel);
		logger.info("< Hotel create");
		return savedHotel;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteById(long id) {
		logger.info("> Hotel delete");
		this.findById(id);
		hotelRepository.deleteById(id);
		logger.info("< Hotel delete");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
	public Hotel updateHotelById(Long hotelId, Hotel newHotel) {
		logger.info("> Hotel update");
		Hotel oldHotel = this.findById(hotelId);
		oldHotel.copyFieldsFrom(newHotel);
		hotelRepository.save(oldHotel);
		logger.info("< Hotel update");
		return oldHotel;
	}
	
	public List<Floor> getFloors(Long id){
		logger.info("> get floors for hotel");
		Hotel hotel = this.findById(id);
		List<Floor> floorList = hotel.getFloor();
		logger.info("< get floors for hotel");
		if(!floorList.isEmpty())
			return floorList ;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Spratovi za dati hotel ne postoje");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	public Floor createFloor(Long hotelId, Floor floor){
		logger.info("> create floor for hotel");
		Hotel hotel = this.findById(hotelId);
		hotel.getFloor().add(floor);
		floor.setHotel(hotel);
		this.save(hotel);
		logger.info("< create floor for hotel");
		return floor;
	}
	
	public List<ExtraOption> getExtraOptions(Long id){
		logger.info("> get extra-options for hotel");
		Hotel hotel = this.findById(id);
		List<ExtraOption> extraOptionList = hotel.getExtraOption();
		logger.info("< get extra-options for hotel");
		if(!extraOptionList.isEmpty())
			return extraOptionList ;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Extra-optioni za dati hotel ne postoje");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	public ExtraOption createExtraOption(Long hotelId, ExtraOption extraOption){
		logger.info("> create extra-option for hotel");
		Hotel hotel = this.findById(hotelId);
		hotel.getExtraOption().add(extraOption);
		extraOption.setHotel(hotel);
		this.save(hotel);
		logger.info("< create extra-option for hotel");
		return extraOption;
	}
	
	public Catalogue getCatalogue(Long id){
		logger.info("> get catalogue for hotel");
		Hotel hotel = this.findById(id);
		Catalogue catalogue = hotel.getCatalogue();
		logger.info("< get catalogue for hotel");
		if(catalogue != null)
			return catalogue;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cenovnik za dati hotel ne postoji");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	public Catalogue createCatalogue(Long hotelId, Catalogue catalogue){
		logger.info("> create catalogue for hotel");
		Hotel hotel = this.findById(hotelId);
		hotel.setCatalogue(catalogue);
		catalogue.setHotel(hotel);
		this.save(hotel);
		logger.info("< create catalogue for hotel");
		return catalogue;
	}
}
