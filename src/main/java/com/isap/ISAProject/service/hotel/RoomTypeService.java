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
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.repository.hotel.RoomTypeRepository;

import hotelInterf.RoomTypeServiceInterface;

@Service
@Transactional(readOnly = true)
public class RoomTypeService implements RoomTypeServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoomTypeRepository roomTypeRepository;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public RoomType findById(long id) {
		logger.info("> Room-type findById id:{}", id);
		Optional<RoomType> roomType = roomTypeRepository.findById(id);
		logger.info("< Room-type findById id:{}", id);
		if(roomType.isPresent())
			return roomType.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tip sobe sa zadatim id-em ne postoji");
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<RoomType> findAll(Pageable pageable) {
		logger.info("> Room-type findAll");
		Page<RoomType> roomType = roomTypeRepository.findAll(pageable);
		logger.info("< Room-type findAll");
		if(!roomType.isEmpty())
			return roomType.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipovi soba ne postoje");
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RoomType save(RoomType roomType) {
		logger.info("> Room-type create");
		RoomType savedRoomType = roomTypeRepository.save(roomType);
		logger.info("< Room-type create");
		return savedRoomType;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteById(long id) {
		logger.info("> Room-type delete");
		this.findById(id);
		roomTypeRepository.deleteById(id);
		logger.info("< Room-type delete");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public RoomType updateRoomTypeById(Long roomTypeId, RoomType newRoomType) {
		logger.info("> Room-Type update");
		RoomType oldRoomType = this.findById(roomTypeId);
		oldRoomType.setName(newRoomType.getName());
		oldRoomType.setDescription(newRoomType.getDescription());
		oldRoomType.setPricePerNight(newRoomType.getPricePerNight());
		roomTypeRepository.save(oldRoomType);
		logger.info("< Room-type update");
		return oldRoomType;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public Catalogue getCatalogue(Long roomTypeId) {
		logger.info("> catalogue from room-type", roomTypeId);
		RoomType roomType = this.findById(roomTypeId);
		Catalogue catalogue = roomType.getCatalogue();
		logger.info("< catalogue from room-type");
		if(catalogue != null)
			return catalogue;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Cenovnik za tip sobe nije postavljena");
	}

}
