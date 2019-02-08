package com.isap.ISAProject.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

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

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.model.user.ConfirmedReservation;
import com.isap.ISAProject.model.user.PendingReservation;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.repository.airline.FlightSeatsRepository;
import com.isap.ISAProject.repository.airline.PassengerRepository;
import com.isap.ISAProject.repository.user.ReservationRepository;
import com.isap.ISAProject.service.EmailSenderService;
import com.isap.ISAProject.service.airline.TicketService;
import com.isap.ISAProject.service.hotel.RoomReservationService;
import com.isap.ISAProject.service.hotel.RoomService;
import com.isap.ISAProject.service.rentacar.VehicleReservationService;
import com.isap.ISAProject.service.rentacar.VehicleService;

@Service
@Transactional(readOnly = true)
public class ReservationService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private VehicleReservationService vehicleReservationService;
	
	@Autowired
	private RoomReservationService roomReservationService;
	
	@Autowired
	private RegisteredUserService userService;
	
	@Autowired
	private PassengerRepository passengersRepository;
	
	@Autowired
	private EmailSenderService emailService;
	
	@Autowired
	private FlightSeatsRepository flightSeatsRepository;
	
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
			//TODO: otkomentarisati
			//vehicleReservation.setReservation(reservation);
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
		checkIfReservationCanBeCancelled(reservation.getBeginDate());
		ticketService.deleteTicket(reservation.getTicket().getId());
		vehicleReservationService.deleteVehicleReservation(reservation.getVehicleReservation().getId());
		roomReservationService.deleteById(reservation.getRoomReservation().getId());
		reservationRepository.delete(reservation);
		logger.info("< Reservation delete");
	}
	
	private void checkIfReservationCanBeCancelled(Date departureTime) {
		logger.info("> checking if ticket (reservation) can be cancelled");
		Date time = new Date();
		Long difference = departureTime.getTime() - time.getTime();
		if(((int) TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS)) < 3)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are too late, reservation can't be cancelled!");
		logger.info("< ticket (reservation) can be cancelled");
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Reservation addUserToReservation(Long id, Long userId) {
		logger.info("> adding user to reservation with id {}", id);
		Reservation reservation = this.findById(id);
		RegisteredUser user = userService.findById(userId);
		reservation.getConfirmedReservations().add(new ConfirmedReservation(user, reservation));
		FlightSeat seat = getFreeSeat(reservation.getTicket());
		if(user.getPassenger() == null) {
			user.setPassenger(this.createNewPassenger(user));
		}
		passengersRepository.save(user.getPassenger());
		seat.setPassenger(user.getPassenger());
		flightSeatsRepository.save(seat);
		reservationRepository.save(reservation);
		emailService.sendReservationInfo(user, reservation);
		logger.info("< user added");
		return reservation;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Reservation inviteUsersToReservation(Long id, List<Long> users) {
		logger.info("> inviting users to reservation with id {}", id);
		Reservation reservation = this.findById(id);
		if(users.size() > reservation.getTicket().getNumberOfSeats() && this.getFreeSeat(reservation.getTicket()) != null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Too many friends invited.");
		RegisteredUser owner = reservation.getConfirmedReservations().get(0).getUser();
		if(owner == null) 
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This reservation is unassigned.");
		List<RegisteredUser> friends = userService.getFriends(owner.getId());
		for(Long userId : users) {
			RegisteredUser user = userService.findById(userId);
			this.inviteUserToReservation(user, friends, reservation);
			emailService.sendInvitation(user, reservation, owner);
		}
		reservationRepository.save(reservation);
		logger.info("< users invited");
		return reservation;
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	private void inviteUserToReservation(RegisteredUser user, List<RegisteredUser> friends, Reservation reservation) {
		if(friends.contains(user)) {
			for(PendingReservation pending : user.getPendingReservations())
				if(pending.getUser().equals(user))
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Friend already invited.");
			reservation.getPendingReservations().add(new PendingReservation(user, reservation));
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner is not friend with user with id " + user.getId());
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Reservation declineInvitation(Long id, Long userId) {
		logger.info("> declining invitation of reservation with id {}", id);
		Reservation reservation = this.findById(id);
		RegisteredUser user = userService.findById(userId);
		if(!this.removeUserFromPendingReservations(reservation, user))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id " + userId + " isn't invited.");
		reservationRepository.save(reservation);
		logger.info("< invitation declined");
		return reservation;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Reservation acceptInvitation(Long id, Long userId) {
		logger.info("> accepting invitation of reservation with id {}", id);
		Reservation reservation = this.findById(id);
		RegisteredUser user = userService.findById(userId);
		if(!this.removeUserFromPendingReservations(reservation, user))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id " + userId + " isn't invited.");
		FlightSeat seat = getFreeSeat(reservation.getTicket());
		if(user.getPassenger() == null)
			user.setPassenger(this.createNewPassenger(user));
		seat.setPassenger(user.getPassenger());
		reservation.getConfirmedReservations().add(new ConfirmedReservation(user, reservation));
		try {
			passengersRepository.save(user.getPassenger());
		} catch(Exception e) { }
		userService.save(user);
		reservationRepository.save(reservation);
		emailService.sendReservationInfo(user, reservation);
		flightSeatsRepository.save(seat);
		logger.info("< accepted invitation");
		return reservation;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	private Passenger createNewPassenger(RegisteredUser user) {
		logger.info("> creating passenger for user with id {}", user.getId());
		Passenger passenger = new Passenger();
		passenger.setFirstName(user.getFirstName());
		passenger.setLastName(user.getLastName());
		// TODO : Videti sta sa brojem pasosa
		passenger.setPassportNumber(user.getId());
		passenger.setUser(user);
		logger.info("< created passenger");
		return passenger;
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	private FlightSeat getFreeSeat(Ticket ticket) {
		for(FlightSeat fs : ticket.getSeats())
			if(fs.getPassenger() == null)
				return fs;
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There are no free seats on the ticket.");
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Reservation cancelReservation(Long id, Long userId) {
		logger.info("> cancelling reservation with id {}", id);
		Reservation reservation = this.findById(id);
		System.out.println("---------------------------------------");
		RegisteredUser user = userService.findById(userId);
		System.out.println("---------------------------------------");
		checkIfReservationCanBeCancelled(reservation.getBeginDate());
		System.out.println("---------------------------------------");
		if(!this.removeUserFromConfirmedReservations(reservation, user))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id " + userId + " isn't on the reservation.");
		System.out.println("---------------------------------------");
		this.removeUserFromSeat(reservation.getTicket(), user.getPassenger());
		System.out.println("---------------------------------------");
		if(reservation.getConfirmedReservations().isEmpty()) {
			logger.info("< reservation cancelled");
			this.deleteById(id);
			return null;
		} else {
			reservationRepository.save(reservation);
			logger.info("< reservation cancelled");
			return reservation;			
		}
	}
	
	private boolean removeUserFromConfirmedReservations(Reservation reservation, RegisteredUser user) {
		ConfirmedReservation forRemoval = null;
		for(ConfirmedReservation confirmed : reservation.getConfirmedReservations())
			if(confirmed.getUser().equals(user)) {
				forRemoval = confirmed;
				forRemoval.setReservation(null);
				break;
			}
		return reservation.getConfirmedReservations().remove(forRemoval);
	}
	
	private boolean removeUserFromPendingReservations(Reservation reservation, RegisteredUser user) {
		PendingReservation forRemoval = null;
		for(PendingReservation pending : reservation.getPendingReservations())
			if(pending.getUser().equals(user)) {
				forRemoval = pending;
				forRemoval.setReservation(null);
				break;
			}
		return reservation.getPendingReservations().remove(forRemoval);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	private void removeUserFromSeat(Ticket ticket, Passenger passenger) {
		for(FlightSeat fs : ticket.getSeats())
			if(fs.getPassenger() != null && fs.getPassenger().equals(passenger)) {
				fs.setPassenger(null);
				return;
			}
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Passenger is not on any seat, but should be.");
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Reservation addRoomReservationToReservation(Long id, Long roomId, @Valid RoomReservation roomReservation) {
		logger.info("> adding room reservation to room with id {}", id);
		Reservation reservation = this.findById(id);
		Room room = roomService.findById(roomId);
		if(roomReservation.getBeginDate().after(roomReservation.getEndDate()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datum rezervacije je lose unesen!");
		if(roomReservation.getBeginDate().before(reservation.getBeginDate()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soba se moze iznajmiti samo nakon sletanja!");
		if(!this.checkIfRoomIsFree(roomReservation.getBeginDate(), roomReservation.getEndDate(), room))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soba nije slobodna u tom periodu!");
		if(reservation.getTicket().getNumberOfSeats() < roomReservation.getNumberOfRooms())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Niste zauzeli sobe za sve putnike.!");
		room.getRoomReservations().add(roomReservation);
		roomReservation.setRoom(room);
		Long difference = roomReservation.getEndDate().getTime() - roomReservation.getBeginDate().getTime();
		roomReservation.setNumberOfNights((int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS));
		roomReservation.setPrice(room.getRoomType().getPricePerNight() * roomReservation.getNumberOfNights());
		reservation.setRoomReservation(roomReservation);
		reservation.setPrice(reservation.getPrice() + roomReservation.getPrice());
		reservationRepository.save(reservation);
		logger.info("< room reservation added");
		return reservation;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Reservation addRoomReservationToReservationWitdId(Long reservationid, Long roomReservationId) {
		Reservation reservation = this.findById(reservationid);
		RoomReservation roomReservation = roomReservationService.findById(roomReservationId);
		reservation.setRoomReservation(roomReservation);
		roomReservation.setReservation(reservation);
		if(reservation.getEndDate().before(roomReservation.getEndDate()))
			reservation.setEndDate(roomReservation.getEndDate());
		reservation.setPrice(reservation.getPrice() + roomReservation.getPrice());
		reservationRepository.save(reservation);
		roomReservationService.save(roomReservation);
		return reservation;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Reservation removeRoomReservation(Long id) {
		logger.info("> removing room reservation from reservation with id {}", id);
		Reservation reservation = this.findById(id);
		RoomReservation roomReservation = reservation.getRoomReservation();
		checkIfRoomReservationCanBeCancelled(roomReservation.getBeginDate());
		reservation.setRoomReservation(null);
		reservation.setPrice(reservation.getPrice() - roomReservation.getPrice());
		reservationRepository.save(reservation);
		logger.info("< room reservation removed");
		return reservation;
	}
	
	private void checkIfRoomReservationCanBeCancelled(Date beginDate) {
		logger.info("> checking if room reservation can be cancelled");
		Date time = new Date();
		Long difference = beginDate.getTime() - time.getTime();
		if(((int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)) < 2)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are too late, reservation can't be cancelled!");
		logger.info("< room reservation can be cancelled");
	}

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
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Reservation addVehicleReservationToReservation(Long id, Long vehicleId, @Valid VehicleReservation vehicleReservation) {
		logger.info("> adding vehicle reservation to room with id {}", id);
		Reservation reservation = this.findById(id);
		Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
		if(vehicleReservation.getBeginDate().after(vehicleReservation.getEndDate()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datum rezervacije je lose unesen!");
		if(vehicleReservation.getBeginDate().before(reservation.getBeginDate()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soba se moze iznajmiti samo nakon sletanja!");
		if(!this.checkIfVehicleIsFree(vehicleReservation.getBeginDate(), vehicleReservation.getEndDate(), vehicle))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soba nije slobodna u tom periodu!");
		if(reservation.getTicket().getNumberOfSeats() < vehicleReservation.getVehicle().getSeatsNumber())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Niste zauzeli sobe za sve putnike.!");
		vehicle.getVehicleReservations().add(vehicleReservation);
		vehicleReservation.setVehicle(vehicle);
		Long difference = vehicleReservation.getEndDate().getTime() - vehicleReservation.getBeginDate().getTime();
		vehicleReservation.setPrice(((int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)) * vehicle.getPricePerDay());
		reservation.setVehicleReservation(vehicleReservation);
		reservation.setPrice(reservation.getPrice() + vehicleReservation.getPrice());
		reservationRepository.save(reservation);
		logger.info("< vehicle reservation added");
		return reservation;
	}

	private boolean checkIfVehicleIsFree(Date start, Date end, Vehicle vehicle) {
		Date reservedStart = null;
		Date reservedEnd = null;
		for(VehicleReservation vehicleReservation : vehicle.getVehicleReservations()) {
			reservedStart = vehicleReservation.getBeginDate();
			reservedEnd = vehicleReservation.getEndDate();
			if((start.after(reservedStart) && start.before(reservedEnd)) || (end.after(reservedStart) && end.before(reservedEnd)))
				return false;
		}
		return true;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Location getLocation(Long reservationId) {
		logger.info("> fethcing location with id {}", reservationId);
		Reservation reservation = this.findById(reservationId);
		Ticket ticket = reservation.getTicket();
		if(ticket == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Greska prilikom dobavljanja lokacije");
		FlightSeat flightSeat = ticket.getSeats().get(0);
		if(flightSeat == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Greska prilikom dobavljanja lokacije");
		Flight flight = flightSeat.getFlight();
		if(flight == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Greska prilikom dobavljanja lokacije");
		Location location = flight.getFinishDestination();
		logger.info("< location fetched");
		
		if(location != null) 
			return location;
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lokacija ne postoji");
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Reservation addVehicleReservationToReservationWithId(Long rId, Long vrId) {
		Reservation r = this.findById(rId);
		VehicleReservation vr = vehicleReservationService.getVehicleReservationById(vrId);
		r.setVehicleReservation(vr);
		vr.setReservation(r);
		if(r.getEndDate().before(vr.getEndDate()))
			r.setEndDate(vr.getEndDate());
		r.setPrice(r.getPrice() + vr.getPrice());
		reservationRepository.save(r);
		vehicleReservationService.saveVehicleReservation(vr);
		return r;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Reservation create(Reservation reservation) {
		logger.info("> creating reservation");
		reservationRepository.save(reservation);
		logger.info("< reservation created");
		return reservation;
	}
	
	public List<Passenger> getPassengers(Long id){
		logger.info("> getting passengers");
		Optional<Reservation> reservation = reservationRepository.findById(id);
		if(!reservation.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacija ne postoji");
		List<Passenger> passengers = new ArrayList<>();
		Ticket ticket = reservation.get().getTicket();
		for(FlightSeat fs : ticket.getSeats()) {
			if(fs.getPassenger() != null)
				passengers.add(fs.getPassenger());
		}
		logger.info("< getting passengers");
		
		return passengers;
	}
	
	public List<Passenger> getUnregisteredPassengers(Long id){
		logger.info("> getting unregistered passengers");
		Optional<Reservation> reservation = reservationRepository.findById(id);
		if(!reservation.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacija ne postoji");
		
		List<Passenger> passengers = new ArrayList<>();
		
		Ticket ticket = reservation.get().getTicket();
		for(FlightSeat fs : ticket.getSeats()) {
			if(fs.getPassenger() != null && fs.getPassenger().getUser() == null)
				passengers.add(fs.getPassenger());
		}
		
		logger.info("< getting unregistered passengers");
		
		return passengers;
	}
	
	public List<FlightSeat> getFreeSeats(Long id){
		logger.info("> getting free seats");
		Optional<Reservation> reservation = reservationRepository.findById(id);
		if(!reservation.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacija ne postoji");
		
		List<FlightSeat> seats = new ArrayList<>();
		Ticket ticket = reservation.get().getTicket();
		
		for(FlightSeat fs : ticket.getSeats()) {
			if(fs.getPassenger() == null)
				seats.add(fs);
		}
		
		logger.info("< getting free seats");
		
		return seats;
	}
	
	@Transactional(readOnly = false)
	public Passenger deletePassenger(Long id, Long passengerId) {
		logger.info("> deleting passenger");
		Optional<Reservation> reservation = reservationRepository.findById(id);
		Optional<Passenger> passenger = passengersRepository.findById(passengerId);
		if(!reservation.isPresent() || !reservation.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rezervacija ili putnik ne postoji");
		
		FlightSeat flightSeat = null;
		for(FlightSeat fs : reservation.get().getTicket().getSeats()) {
			if(fs.getPassenger().getId() == passenger.get().getId()) {
				flightSeat = fs;
				break;
			}
		}
		
		if(flightSeat == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Putnik nije na zadatom letu");
		flightSeat.setPassenger(null);
		passenger.get().getFlightSeats().remove(flightSeat);
		passengersRepository.save(passenger.get());
		flightSeatsRepository.save(flightSeat);
		logger.info("< deleting passenger");
		
		return passenger.get();
	}
	
}
