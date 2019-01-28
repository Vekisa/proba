package com.isap.ISAProject.service.user;

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

import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.repository.user.ReservationRepository;
import com.isap.ISAProject.service.airline.TicketService;
import com.isap.ISAProject.service.hotel.RoomReservationService;
import com.isap.ISAProject.service.rentacar.VehicleReservationService;

@Service
@Transactional(readOnly = true)
public class ReservationService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private VehicleReservationService vehicleReservationService;
	
	@Autowired
	private RoomReservationService roomReservationService;
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Reservation findById(long id) {
		logger.info("> Reservation findById id:{}", id);
		Optional<Reservation> reservation = reservationRepository.findById(id);
		logger.info("< Reservation findById id:{}", id);
		if(reservation.isPresent())
			return reservation.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacija sa zadatim id-em ne postoji");
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Reservation> findAll(Pageable pageable) {
		logger.info("> Reservation findAll");
		Page<Reservation> reservation = reservationRepository.findAll(pageable);
		logger.info("< Reservation findAll");
		if(!reservation.isEmpty())
			return reservation.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacije ne postoje");
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Reservation save(Reservation reservation, Ticket ticket, VehicleReservation vehicleReservation, RoomReservation roomReservation){
		logger.info("> Reservation create");
		if(ticket == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avionska karta nije rezervisana");
		
		if(vehicleReservation != null) {
			reservation.setVehicleReservation(vehicleReservation);
			vehicleReservation.setReservation(reservation);
			vehicleReservationService.saveVehicleReservation(vehicleReservation);
		}
		if(roomReservation != null) {
			reservation.setRoomReservation(roomReservation);
			roomReservation.setReservation(reservation);
			roomReservationService.save(roomReservation);
		}
		
		ticket.setReservation(reservation);
		reservation.setTicket(ticket);
		
		ticketService.saveTicket(ticket);
		
		//reservation.setPrice(ticket.getPrice() + vehicleReservation.getPrice() + roomReservation.getRoom().getRoomType().getPricePerNight() *
		//		getDifferenceDays(roomReservation.getBeginDate(), roomReservation.getEndDate()));
		
		reservationRepository.save(reservation);
		
		logger.info("< Reservation create");
		return reservation;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteById(long id) {
		logger.info("> Reservation delete");
		Reservation reservation = this.findById(id);
		ticketService.deleteTicket(reservation.getTicket().getId());
		vehicleReservationService.deleteVehicleReservation(reservation.getVehicleReservation().getId());
		roomReservationService.deleteById(reservation.getRoomReservation().getId());
		reservationRepository.delete(reservation);
		logger.info("< Reservation delete");
	}
	
	public static long getDifferenceDays(Date d1, Date d2) {
	    long diff = d2.getTime() - d1.getTime();
	    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	
}
