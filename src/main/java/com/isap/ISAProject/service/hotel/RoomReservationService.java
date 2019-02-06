package com.isap.ISAProject.service.hotel;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.repository.hotel.ExtraOptionRepository;
import com.isap.ISAProject.repository.hotel.RoomReservationRepository;

import hotelInterf.RoomReservationServiceInterface;

@Service
@Transactional(readOnly = true)
public class RoomReservationService implements RoomReservationServiceInterface {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoomReservationRepository roomReservationRepository;
	
	@Autowired
	private ExtraOptionRepository optionsRepository;
	
	@Autowired
	private RoomService roomService;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public RoomReservation findById(long id) {
		logger.info("> Rezervacija sobe findById id:{}", id);
		Optional<RoomReservation> roomReservation = roomReservationRepository.findById(id);
		logger.info("< Rezervacja sobe findById id:{}", id);
		if(roomReservation.isPresent())
			return roomReservation.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacija sobe sa zadatim id-em ne postoji");
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<RoomReservation> findAll(Pageable pageable) {
		logger.info("> Rezervacija sobe findAll");
		Page<RoomReservation> roomReservtions = roomReservationRepository.findAll(pageable);
		logger.info("< Rezervacija sobe findAll");
		if(!roomReservtions.isEmpty())
			return roomReservtions.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacije sobe ne postoje");
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RoomReservation save(RoomReservation roomReservation) {
		logger.info("> Rezervacija sobe create");
		RoomReservation savedRoomReservation = roomReservationRepository.save(roomReservation);
		logger.info("< Rezervacija sobe create");
		return savedRoomReservation;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteById(long id) {
		logger.info("> Rezervacija sobe delete");
		this.findById(id);
		roomReservationRepository.deleteById(id);
		logger.info("< Rezervacija sobe delete");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public RoomReservation updateRoomReservationById(Long roomReservationId, RoomReservation newRoomReservation) {
		logger.info("> Rezervacija sobe update");
		RoomReservation oldRoomReservation = this.findById(roomReservationId);
		Room room = this.getRoom(roomReservationId);
		if(newRoomReservation.getBeginDate().after(newRoomReservation.getEndDate()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datum rezervacije je lose unesen");
		if(room!= null && !checkIfRoomIsFree(newRoomReservation.getBeginDate(), oldRoomReservation.getEndDate(), room))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soba nije slobodna u tom periodu");	
		oldRoomReservation.copyFieldsFrom(newRoomReservation);
		Long difference = oldRoomReservation.getEndDate().getTime() - oldRoomReservation.getBeginDate().getTime();
		oldRoomReservation.setNumberOfNights((int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS));
		recalculateReservationPrice(oldRoomReservation);
		roomReservationRepository.save(oldRoomReservation);
		logger.info("< Rezervacija sobe update");
		return oldRoomReservation;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
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
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public ExtraOption createExtraOption(Long roomReservationId, ExtraOption extraOption){
		logger.info("> create extra-option for room-resevration");
		RoomReservation roomReservation = this.findById(roomReservationId);
		roomReservation.getExtraOptions().add(extraOption);
		extraOption.getRoomReservations().add(roomReservation);
		this.save(roomReservation);
		logger.info("< create extra-option for room-resevration");
		return extraOption;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public ExtraOption addExtraOption(Long id, Long extraOptionId) {
		logger.info("> adding extra option to room with id {}", id);
		RoomReservation roomReservation = this.findById(id);
		ExtraOption option = this.findExtraOption(extraOptionId);
		if(!roomReservation.getRoom().getFloor().getHotel().equals(option.getHotel()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room and options don't belong to the same hotel.");
		logger.info("< extra option added");
		roomReservation.getExtraOptions().add(option);
		recalculateReservationPrice(roomReservation);
		this.save(roomReservation);
		return option;
	}
	
	@Override
	public void recalculateReservationPrice(RoomReservation roomReservation) {
		roomReservation.setPrice(roomReservation.getRoom().getRoomType().getPricePerNight() * roomReservation.getNumberOfNights());
		for(ExtraOption eo : roomReservation.getExtraOptions())
			roomReservation.setPrice(roomReservation.getPrice() + eo.getPricePerDay() * roomReservation.getNumberOfNights());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void removeExtraOption(Long id, Long extraOptionId) {
		logger.info("> removing extra option from room with id {}", id);
		RoomReservation roomReservation = this.findById(id);
		ExtraOption option = this.findExtraOption(extraOptionId);
		if(!roomReservation.getExtraOptions().contains(option))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option doesn't belong to reservation.");
		logger.info("< extra option removed");
		roomReservation.getExtraOptions().remove(option);
		recalculateReservationPrice(roomReservation);
		this.save(roomReservation);
	}
	
	@Override
	public ExtraOption findExtraOption(Long extraOptionId) {
		logger.info("> fetching extra option with id {}", extraOptionId);
		Optional<ExtraOption> option = optionsRepository.findById(extraOptionId);
		logger.info("< extra option fetched");
		if(option.isPresent()) return option.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested extra option doesn't exist.");
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public Room getRoom(Long roomReservationId) {
		logger.info("> room from room-reservation", roomReservationId);
		RoomReservation roomReservation = this.findById(roomReservationId);
		Room room = roomReservation.getRoom();
		logger.info("< room from room-reservation");
		if(room != null)
			return room;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Soba za rezervaciju nije postavljena");
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RoomReservation saveWithRoomId(Long roomId, Date begin, Date end) {	
		logger.info("> create room reervation  with room", roomId);
		Room room = roomService.findById(roomId);
		if(!checkIfRoomIsFree(begin, end, room)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soba za dati period nije slobodna");
		}
		RoomReservation roomReservation = new RoomReservation();
		roomReservation.setRoom(room);
		roomReservation.setBeginDate(begin);
		roomReservation.setEndDate(end);
		Long difference = end.getTime() - begin.getTime();
		roomReservation.setNumberOfNights((int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS));
		roomReservation.setPrice(roomReservation.getNumberOfNights() * room.getRoomType().getPricePerNight());
		room.getRoomReservations().add(roomReservation);
		roomService.save(room);
		logger.info("< create room reservation  with room");
		this.save(roomReservation);
		return roomReservation;
	}
	
	@Override
	public boolean checkIfRoomIsFree(Date start, Date end, Room room) {
		Date reservedStart = null;
		Date reservedEnd = null;
		for(RoomReservation roomReservation :room.getRoomReservations()) {
			reservedStart = roomReservation.getBeginDate();
			reservedEnd = roomReservation.getEndDate();
			if((start.after(reservedStart) && start.before(reservedEnd)) || (end.after(reservedStart) && end.before(reservedEnd)))
				return false;
		}
		return true;
	}
}
