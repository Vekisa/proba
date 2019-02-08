package com.isap.ISAProject.repository.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.user.RegisteredUser;

@Repository
public interface RatingRepository extends PagingAndSortingRepository<RegisteredUser, Long> {

	@Modifying
	@Query(value = "update hotel set rating = (select avg(rating) from hotel_rating where rating > 0 && hotel_id = ?1) where id = ?1", nativeQuery = true)
	void updateHotelRating(Long hotelId);

	@Modifying
	@Query(value = "update room set rating = (select avg(rating) from room_rating where rating > 0 && room_id = ?1) where id = ?1", nativeQuery = true)
	void updateRoomRating(Long roomId);

	@Modifying
	@Query(value = "update airline set rating = (select avg(rating) from airline_rating where rating > 0 && airline_id = ?1) where id = ?1", nativeQuery = true)
	void updateAirlineRating(Long airlineId);

	@Modifying
	@Query(value = "update flight set rating = (select avg(rating) from flight_rating where rating > 0 && flight_id = ?1) where id = ?1", nativeQuery = true)
	void updateFlightRating(Long flightId);

	@Modifying
	@Query(value = "update rent_a_car set rating = (select avg(rating) from rentacar_rating where rating > 0 && rentacar_id = ?1) where id = ?1", nativeQuery = true)
	void updateRentACarRating(Long rentACarId);

	@Modifying
	@Query(value = "update vehicle set rating = (select avg(rating) from vehicle_rating where rating > 0 && vehicle_id = ?1) where id = ?1", nativeQuery = true)
	void updateVehicleRating(Long vehicleId);

	
	
}
