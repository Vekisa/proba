package com.isap.ISAProject.serviceInterface.user;

public interface RatingServiceInterface {

	void rateHotel(Long hotelId, int rating, Long id);

	void rateRoom(Long roomId, int rating, Long id);

	void rateAirline(Long airlineId, int rating, Long id);

	void rateFlight(Long flightId, int rating, Long id);

	void rateRentACar(Long rentACarId, int rating, Long id);

	void rateVehicle(Long vehicleId, int rating, Long id);

}
