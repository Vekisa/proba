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

import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.repository.airline.DestinationRepository;
import com.isap.ISAProject.repository.hotel.HotelRepository;

@Service
@Transactional(readOnly = true)
public class HotelService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private DestinationRepository destinationRepository;
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Hotel findById(long id) {
			logger.info("> Hotel findById id:{}", id);
			Optional<Hotel> hotel = hotelRepository.findById(id);
			logger.info("< Hotel findById id:{}", id);
			if(hotel.isPresent())
				return hotel.get();
			else 
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel sa zadatim id-em ne postoji");
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Hotel> findAll(Pageable pageable) {
		logger.info("> Hotel findAll");
		Page<Hotel> hotels = hotelRepository.findAll(pageable);
		logger.info("< Hotel findAll");
		if(!hotels.isEmpty())
			return hotels.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hoteli ne postoje");
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Hotel save(Hotel hotel) {
		logger.info("> Hotel create");
		Hotel savedHotel = hotelRepository.save(hotel);
		logger.info("< Hotel create");
		return savedHotel;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteById(long id) {
		logger.info("> Hotel delete");
		this.findById(id);
		hotelRepository.deleteById(id);
		logger.info("< Hotel delete");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public Hotel updateHotelById(Long hotelId, Hotel newHotel) {
		logger.info("> Hotel update");
		Hotel oldHotel = this.findById(hotelId);
		oldHotel.copyFieldsFrom(newHotel);
		this.save(oldHotel);
		logger.info("< Hotel update");
		return oldHotel;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
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
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public Floor createFloor(Long hotelId, Floor floor){
		logger.info("> create floor for hotel");
		Hotel hotel = this.findById(hotelId);
		hotel.getFloor().add(floor);
		floor.setHotel(hotel);
		this.save(hotel);
		logger.info("< create floor for hotel");
		return floor;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
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
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public ExtraOption createExtraOption(Long hotelId, ExtraOption extraOption){
		logger.info("> create extra-option for hotel");
		Hotel hotel = this.findById(hotelId);
		hotel.getExtraOption().add(extraOption);
		extraOption.setHotel(hotel);
		this.save(hotel);
		logger.info("< create extra-option for hotel");
		return extraOption;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
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
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public Catalogue createCatalogue(Long hotelId, Catalogue catalogue){
		logger.info("> create catalogue for hotel");
		Hotel hotel = this.findById(hotelId);
		hotel.setCatalogue(catalogue);
		catalogue.setHotel(hotel);
		this.save(hotel);
		logger.info("< create catalogue for hotel");
		return catalogue;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public Hotel changeLocationOfHotel(Long hotelId, Long destinationId) {
		logger.info("> changing location of hotel with id {}", hotelId);
		Hotel hotel = this.findById(hotelId);
		Destination destination = this.findDestination(destinationId);
		destination.getHotels().add(hotel);
		hotel.setLocation(destination);
		destinationRepository.save(destination);
		logger.info("< location changed");
		return hotel;
	}
	
	private Destination findDestination(Long id) {
		logger.info("> fetching destination with id {}", id);
		Optional<Destination> destination = destinationRepository.findById(id);
		logger.info("< destination fetched");
		if(destination.isPresent()) return destination.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested destination doesn't exist.");
	}
	
}
