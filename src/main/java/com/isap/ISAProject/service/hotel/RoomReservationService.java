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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.repository.hotel.RoomReservationRepository;

@Service
@Transactional(readOnly = true)
public class RoomReservationService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoomReservationRepository roomReservationRepository;
	
	public RoomReservation findById(long id) {
		logger.info("> Rezervacija sobe findById id:{}", id);
		Optional<RoomReservation> roomReservation = roomReservationRepository.findById(id);
		logger.info("< Rezervacja sobe findById id:{}", id);
		if(roomReservation.isPresent())
			return roomReservation.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacija sobe sa zadatim id-em ne postoji");
	}
	
	public List<RoomReservation> findAll(Pageable pageable) {
		logger.info("> Rezervacija sobe findAll");
		Page<RoomReservation> roomReservtions = roomReservationRepository.findAll(pageable);
		logger.info("< Rezervacija sobe findAll");
		if(!roomReservtions.isEmpty())
			return roomReservtions.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacije sobe ne postoje");
	}
	
	public RoomReservation save(RoomReservation roomReservation) {
		logger.info("> Rezervacija sobe create");
		RoomReservation savedRoomReservation = roomReservationRepository.save(roomReservation);
		logger.info("< Rezervacija sobe create");
		return savedRoomReservation;
	}
	
	public void deleteById(long id) {
		logger.info("> Rezervacija sobe delete");
		this.findById(id);
		roomReservationRepository.deleteById(id);
		logger.info("< Rezervacija sobe delete");
	}
	
	public RoomReservation updateRoomReservationById(Long roomReservationId, RoomReservation newRoomReservation) {
		logger.info("> Rezervacija sobe update");
		RoomReservation oldRoomReservation = this.findById(roomReservationId);
		oldRoomReservation.copyFieldsFrom(newRoomReservation);
		roomReservationRepository.save(oldRoomReservation);
		logger.info("< Rezervacija sobe update");
		return oldRoomReservation;
	}
	
	public List<ExtraOption> getExtraOptions(Long id){
		logger.info("> get extra-options for room-reservation");
		RoomReservation roomReservation = this.findById(id);
		List<ExtraOption> extraOptionList = roomReservation.getExtraOptions();
		logger.info("< get extra-ptions for room-reservation");
		if(!extraOptionList.isEmpty())
			return extraOptionList;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Extra optioni za datu rezervaciju ne postoje");
	}
	
	public ExtraOption createExtraOption(Long roomReservationId, ExtraOption extraOption){
		logger.info("> create extra-option for room-resevration");
		RoomReservation roomReservation = this.findById(roomReservationId);
		roomReservation.getExtraOptions().add(extraOption);
		extraOption.setRoomReservation(roomReservation);
		this.save(roomReservation);
		logger.info("< create extra-option for room-resevration");
		return extraOption;
	}
}
