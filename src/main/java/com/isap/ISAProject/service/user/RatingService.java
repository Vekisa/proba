package com.isap.ISAProject.service.user;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.rating.AirlineRating;
import com.isap.ISAProject.model.rating.FlightRating;
import com.isap.ISAProject.model.rating.HotelRating;
import com.isap.ISAProject.model.rating.RentACarRating;
import com.isap.ISAProject.model.rating.RoomRating;
import com.isap.ISAProject.model.rating.VehicleRating;
import com.isap.ISAProject.model.user.ConfirmedReservation;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.repository.user.RatingRepository;
import com.isap.ISAProject.serviceInterface.user.RatingServiceInterface;

@Service
public class RatingService implements RatingServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RegisteredUserService userService;
	
	@Autowired
	private RatingRepository ratingRepository;
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void rateHotel(Long hotelId, int rating, Long id) {
		logger.info("> rating hotel with id {}", hotelId);
		RegisteredUser user = userService.findById(id);
		Date time = new Date();
		for(ConfirmedReservation confirmed : user.getConfirmedReservations())
			if(confirmed.getReservation().getRoomReservation() != null)
				if(confirmed.getReservation().getEndDate().after(time) &&
						confirmed.getReservation().getRoomReservation().getRoom().getFloor().getHotel().getId() == hotelId) {
					for(HotelRating hotelRating : user.getHotelRatings())
						if(hotelRating.getHotel().getId() == hotelId) {
							hotelRating.setRating(rating);
							ratingRepository.updateHotelRating(hotelId);
							userService.save(user);
							logger.info("< hotel rated");
							return;
						}
					HotelRating hotelRating = new HotelRating(user, confirmed.getReservation().getRoomReservation().getRoom().getFloor().getHotel());
					hotelRating.setRating(rating);
					ratingRepository.updateHotelRating(hotelId);
					userService.save(user);
					logger.info("< hotel rated");
					return;
				}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested hotel can't be rated by this user.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void rateRoom(Long roomId, int rating, Long id) {
		logger.info("> rating room with id {}", roomId);
		RegisteredUser user = userService.findById(id);
		Date time = new Date();
		for(ConfirmedReservation confirmed : user.getConfirmedReservations())
			if(confirmed.getReservation().getRoomReservation() != null)
				if(confirmed.getReservation().getEndDate().after(time) &&
						confirmed.getReservation().getRoomReservation().getRoom().getId() == roomId) {
					for(RoomRating roomRating : user.getRoomRatings())
						if(roomRating.getRoom().getId() == roomId) {
							roomRating.setRating(rating);
							ratingRepository.updateRoomRating(roomId);
							userService.save(user);
							logger.info("< room rated");
							return;
						}
					RoomRating roomRating = new RoomRating(user, confirmed.getReservation().getRoomReservation().getRoom());
					roomRating.setRating(rating);
					ratingRepository.updateRoomRating(roomId);
					userService.save(user);
					logger.info("< room rated");
					return;
				}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested room can't be rated by this user.");
	}

	@Override
	public void rateAirline(Long airlineId, int rating, Long id) {
		logger.info("> rating airline with id {}", airlineId);
		RegisteredUser user = userService.findById(id);
		Date time = new Date();
		for(ConfirmedReservation confirmed : user.getConfirmedReservations())
			if(confirmed.getReservation().getTicket() != null)
				if(confirmed.getReservation().getEndDate().after(time) &&
						confirmed.getReservation().getTicket().getSeats().get(0).getFlight().getAirline().getId() == airlineId) {
					for(AirlineRating airlineRating : user.getAirlineRatings())
						if(airlineRating.getAirline().getId() == airlineId) {
							airlineRating.setRating(rating);
							ratingRepository.updateAirlineRating(airlineId);
							userService.save(user);
							logger.info("< airline rated");
							return;
						}
					AirlineRating airlineRating = new AirlineRating(user, confirmed.getReservation().getTicket().getSeats().get(0).getFlight().getAirline());
					airlineRating.setRating(rating);
					ratingRepository.updateAirlineRating(airlineId);
					userService.save(user);
					logger.info("< airline rated");
					return;
				}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested airline can't be rated by this user.");
	}

	@Override
	public void rateFlight(Long flightId, int rating, Long id) {
		logger.info("> rating flight with id {}", flightId);
		RegisteredUser user = userService.findById(id);
		Date time = new Date();
		for(ConfirmedReservation confirmed : user.getConfirmedReservations())
			if(confirmed.getReservation().getTicket() != null)
				if(confirmed.getReservation().getEndDate().after(time) &&
						confirmed.getReservation().getTicket().getSeats().get(0).getFlight().getId() == flightId) {
					for(FlightRating flightRating : user.getFlightRatings())
						if(flightRating.getFlight().getId() == flightId) {
							flightRating.setRating(rating);
							ratingRepository.updateFlightRating(flightId);
							userService.save(user);
							logger.info("< flight rated");
							return;
						}
					FlightRating flightRating = new FlightRating(user, confirmed.getReservation().getTicket().getSeats().get(0).getFlight());
					flightRating.setRating(rating);
					ratingRepository.updateFlightRating(flightId);
					userService.save(user);
					logger.info("< flight rated");
					return;
				}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested flight can't be rated by this user.");
	}

	@Override
	public void rateRentACar(Long rentACarId, int rating, Long id) {
		logger.info("> rating rent-a-car with id {}", rentACarId);
		RegisteredUser user = userService.findById(id);
		Date time = new Date();
		for(ConfirmedReservation confirmed : user.getConfirmedReservations())
			if(confirmed.getReservation().getVehicleReservation() != null)
				if(confirmed.getReservation().getEndDate().after(time) &&
						confirmed.getReservation().getVehicleReservation().getVehicle().getBranchOffice().getRentACar().getId() == rentACarId) {
					for(RentACarRating rentACarRating : user.getRentACarRatings())
						if(rentACarRating.getRentACar().getId() == rentACarId) {
							rentACarRating.setRating(rating);
							ratingRepository.updateRentACarRating(rentACarId);
							userService.save(user);
							logger.info("< rent-a-car rated");
							return;
						}
					RentACarRating rentACarRating = new RentACarRating(user, confirmed.getReservation().getVehicleReservation().getVehicle().getBranchOffice().getRentACar());
					rentACarRating.setRating(rating);
					ratingRepository.updateRentACarRating(rentACarId);
					userService.save(user);
					logger.info("< rent-a-car rated");
					return;
				}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested rent-a-car can't be rated by this user.");
	}

	@Override
	public void rateVehicle(Long vehicleId, int rating, Long id) {
		logger.info("> rating vehicle with id {}", vehicleId);
		RegisteredUser user = userService.findById(id);
		Date time = new Date();
		for(ConfirmedReservation confirmed : user.getConfirmedReservations())
			if(confirmed.getReservation().getVehicleReservation() != null)
				if(confirmed.getReservation().getEndDate().after(time) &&
						confirmed.getReservation().getVehicleReservation().getVehicle().getId() == vehicleId) {
					for(VehicleRating vehicleRating : user.getVehicleRatings())
						if(vehicleRating.getVehicle().getId() == vehicleId) {
							vehicleRating.setRating(rating);
							ratingRepository.updateVehicleRating(vehicleId);
							userService.save(user);
							logger.info("< vehicle rated");
							return;
						}
					VehicleRating vehicleRating = new VehicleRating(user, confirmed.getReservation().getVehicleReservation().getVehicle());
					vehicleRating.setRating(rating);
					ratingRepository.updateVehicleRating(vehicleId);
					userService.save(user);
					logger.info("< vehicle rated");
					return;
				}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested vehicle can't be rated by this user.");
	}
	
}
