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

import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.repository.hotel.ExtraOptionRepository;

@Service
@Transactional(readOnly = true)
public class ExtraOptionService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ExtraOptionRepository extraOptionRepository;
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ExtraOption findById(long id) {
		logger.info("> Extra-option findById id:{}", id);
		Optional<ExtraOption> extraOption = extraOptionRepository.findById(id);
		logger.info("< Extra-option findById id:{}", id);
		if(extraOption.isPresent())
			return extraOption.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Extra-option sa zadatim id-em ne postoji");
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<ExtraOption> findAll(Pageable pageable) {
		logger.info("> Extra-option findAll");
		Page<ExtraOption> extraOptions = extraOptionRepository.findAll(pageable);
		logger.info("< Extra-option findAll");
		if(!extraOptions.isEmpty())
			return extraOptions.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Extra-optioni ne postoje");
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public ExtraOption save(ExtraOption extraOption) {
		logger.info("> Extra-option create");
		ExtraOption savedExtraOption = extraOptionRepository.save(extraOption);
		logger.info("< Extra-option create");
		return savedExtraOption;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteById(long id) {
		logger.info("> Extra-option delete");
		this.findById(id);
		extraOptionRepository.deleteById(id);
		logger.info("< Extra-option delete");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public ExtraOption updateExtraOptionById(Long extraOptionId, ExtraOption newExtraOption) {
		logger.info("> Extra-option update");
		ExtraOption oldExtraOption = this.findById(extraOptionId);
		oldExtraOption.copyFieldsFrom(newExtraOption);
		extraOptionRepository.save(oldExtraOption);
		logger.info("< Extra-option update");
		return oldExtraOption;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public RoomReservation getRoomReservation(Long extraOptionId) {
		logger.info("> room-reservation from extra-option", extraOptionId);
		ExtraOption extraOption = this.findById(extraOptionId);
		RoomReservation roomReservation = extraOption.getRoomReservation();
		logger.info("< room-reservation from extra-option");
		if(roomReservation != null)
			return roomReservation;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Rezervacija sobe za dati extra-option nije postavljen");
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public Hotel getHotel(Long extraOptionId) {
		logger.info("> hotel from extra-option", extraOptionId);
		ExtraOption extraOption = this.findById(extraOptionId);
		Hotel hotel = extraOption.getHotel();
		logger.info("< hotel from extra-option");
		if(hotel != null)
			return hotel;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Hotel za dati extra-option nije postavljen");
	}

}
