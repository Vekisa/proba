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
import com.isap.ISAProject.serviceInterface.hotel.ExtraOptionServiceInterface;

@Service
@Transactional(readOnly = true)
public class ExtraOptionService implements ExtraOptionServiceInterface {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ExtraOptionRepository extraOptionRepository;
	
	@Override
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
	
	@Override
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
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public ExtraOption save(ExtraOption extraOption) {
		logger.info("> Extra-option create");
		ExtraOption savedExtraOption = extraOptionRepository.save(extraOption);
		logger.info("< Extra-option create");
		return savedExtraOption;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteById(long id) {
		logger.info("> Extra-option delete");
		this.findById(id);
		extraOptionRepository.deleteById(id);
		logger.info("< Extra-option delete");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public ExtraOption updateExtraOptionById(Long extraOptionId, ExtraOption newExtraOption) {
		logger.info("> Extra-option update");
		ExtraOption oldExtraOption = this.findById(extraOptionId);
		oldExtraOption.setPricePerDay(newExtraOption.getPricePerDay());
		oldExtraOption.setDescription(newExtraOption.getDescription());
		oldExtraOption.setDiscount(newExtraOption.getDiscount());
		extraOptionRepository.save(oldExtraOption);
		logger.info("< Extra-option update");
		return oldExtraOption;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<RoomReservation> getRoomReservation(Long extraOptionId) {
		logger.info("> room-reservations from extra-option", extraOptionId);
		ExtraOption extraOption = this.findById(extraOptionId);
		List<RoomReservation> roomReservations = extraOption.getRoomReservations();
		logger.info("< room-reservations from extra-option");
		if(!roomReservations.isEmpty())
			return roomReservations;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Rezervacija sobe za dati extra-option nije postavljen");
	}
	
	@Override
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
