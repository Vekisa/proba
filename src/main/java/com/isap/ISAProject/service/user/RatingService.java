package com.isap.ISAProject.service.user;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.rating.AirlineRating;
import com.isap.ISAProject.model.rating.FlightRating;
import com.isap.ISAProject.model.rating.HotelRating;
import com.isap.ISAProject.model.rating.RentACarRating;
import com.isap.ISAProject.model.rating.RoomRating;
import com.isap.ISAProject.model.rating.VehicleRating;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.repository.user.RatingRepository;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;
import com.isap.ISAProject.serviceInterface.user.RatingServiceInterface;

@Service
public class RatingService implements RatingServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RegisteredUserRepository usersRepository;
	
	@Autowired
	private RatingRepository ratingRepository;
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	@Scheduled(fixedRate = 300000)
	public void giveAllUsersAccessToRating() {
		logger.info("> creating ratings for all eligible users");
		Date time = new Date();
		for(RegisteredUser user : usersRepository.findAll())
			for(Reservation reservation : user.getConfirmedReservations())
				if(reservation.getEndDate().after(time))
					this.giveAccessToRating(user, reservation);
		logger.info("< ratings created");
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private void giveAccessToRating(RegisteredUser user, Reservation reservation) {
		logger.info("> giving access to user");
		Flight flight = reservation.getTicket().getSeats().get(0).getFlight();
		accessToRateFlight(flight, user);
		accessToRateAirline(flight.getAirline(), user);
		if(reservation.getRoomReservation() != null) {
			Room room = reservation.getRoomReservation().getRoom();
			accessToRateRoom(room, user);
			accessToRateHotel(room.getFloor().getHotel(), user);
		}
		if(reservation.getVehicleReservation() != null) {
			Vehicle vehicle = reservation.getVehicleReservation().getVehicle();
			accessToRateVehicle(vehicle, user);
			accessToRateRentACar(vehicle.getBranchOffice().getRentACar(), user);
		}
		usersRepository.save(user);
		logger.info("< access given");
	}
	
	private void accessToRateRentACar(RentACar rentACar, RegisteredUser user) {
		for(RentACarRating rating : user.getRentACarRatings())
			if(rating.getRentACar().equals(rentACar))
				return;
		user.getRentACarRatings().add(new RentACarRating(user, rentACar));
	}

	private void accessToRateVehicle(Vehicle vehicle, RegisteredUser user) {
		for(VehicleRating rating : user.getVehicleRatings())
			if(rating.getVehicle().equals(vehicle))
				return;
		user.getVehicleRatings().add(new VehicleRating(user, vehicle));
	}

	private void accessToRateHotel(Hotel hotel, RegisteredUser user) {
		for(HotelRating rating : user.getHotelRatings())
			if(rating.getHotel().equals(hotel))
				return;
		user.getHotelRatings().add(new HotelRating(user, hotel));
	}

	private void accessToRateRoom(Room room, RegisteredUser user) {
		for(RoomRating rating : user.getRoomRatings())
			if(rating.getRoom().equals(room))
				return;
		user.getRoomRatings().add(new RoomRating(user, room));
	}

	private void accessToRateAirline(Airline airline, RegisteredUser user) {
		for(AirlineRating rating : user.getAirlineRatings())
			if(rating.getAirline().equals(airline))
				return;
		user.getAirlineRatings().add(new AirlineRating(user, airline));
	}
	
	private void accessToRateFlight(Flight flight, RegisteredUser user) {
		for(FlightRating rating : user.getFlightRatings())
			if(rating.getFlight().equals(flight))
				return;
		user.getFlightRatings().add(new FlightRating(user, flight));
	}

	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	private RegisteredUser findById(Long id) {
		logger.info("> fetch user with id {}", id);
		Optional<RegisteredUser> user = usersRepository.findById(id);
		logger.info("< user fetched");
	
		if(user.isPresent()) return user.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user doesn't exist.");
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void rateHotel(Long hotelId, int rating, Long id) {
		logger.info("> rating hotel with id {}", hotelId);
		RegisteredUser user = this.findById(id);
		for(HotelRating hr : user.getHotelRatings())
			if(hr.getHotel().getId().equals(hotelId)) {
				hr.setRating(rating);
				ratingRepository.updateHotelRating(hotelId);
				usersRepository.save(user);
				logger.info("< hotel rated");
				return;
			}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested hotel can't be rated by this user.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void rateRoom(Long roomId, int rating, Long id) {
		logger.info("> rating room with id {}", roomId);
		RegisteredUser user = this.findById(id);
		for(RoomRating hr : user.getRoomRatings())
			if(hr.getRoom().getId().equals(roomId)) {
				hr.setRating(rating);
				ratingRepository.updateRoomRating(roomId);
				usersRepository.save(user);
				logger.info("< room rated");
				return;
			}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested room can't be rated by this user.");
	}

	@Override
	public void rateAirline(Long airlineId, int rating, Long id) {
		logger.info("> rating airline with id {}", airlineId);
		RegisteredUser user = this.findById(id);
		for(AirlineRating hr : user.getAirlineRatings())
			if(hr.getAirline().getId().equals(airlineId)) {
				hr.setRating(rating);
				ratingRepository.updateAirlineRating(airlineId);
				usersRepository.save(user);
				logger.info("< airline rated");
				return;
			}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested airline can't be rated by this user.");
	}

	@Override
	public void rateFlight(Long flightId, int rating, Long id) {
		logger.info("> rating flight with id {}", flightId);
		RegisteredUser user = this.findById(id);
		for(FlightRating hr : user.getFlightRatings())
			if(hr.getFlight().getId().equals(flightId)) {
				hr.setRating(rating);
				ratingRepository.updateFlightRating(flightId);
				usersRepository.save(user);
				logger.info("< flight rated");
				return;
			}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested flight can't be rated by this user.");
	}

	@Override
	public void rateRentACar(Long rentACarId, int rating, Long id) {
		logger.info("> rating rentacar with id {}", rentACarId);
		RegisteredUser user = this.findById(id);
		for(RentACarRating hr : user.getRentACarRatings())
			if(hr.getRentACar().getId().equals(rentACarId)) {
				hr.setRating(rating);
				ratingRepository.updateRentACarRating(rentACarId);
				usersRepository.save(user);
				logger.info("< rentacar rated");
				return;
			}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested rentacar can't be rated by this user.");
	}

	@Override
	public void rateVehicle(Long vehicleId, int rating, Long id) {
		logger.info("> rating vehicle with id {}", vehicleId);
		RegisteredUser user = this.findById(id);
		for(VehicleRating hr : user.getVehicleRatings())
			if(hr.getVehicle().getId().equals(vehicleId)) {
				hr.setRating(rating);
				ratingRepository.updateVehicleRating(vehicleId);
				usersRepository.save(user);
				logger.info("< vehicle rated");
				return;
			}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested vehicle can't be rated by this user.");
	}
	
}
