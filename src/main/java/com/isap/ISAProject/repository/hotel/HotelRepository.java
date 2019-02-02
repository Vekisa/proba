package com.isap.ISAProject.repository.hotel;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.hotel.Hotel;

@Transactional(propagation = Propagation.MANDATORY)
public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long> {

	@Modifying
	@Query(value = "update isap.hotel set rating = (select avg(rating) from isap.hotel_rating where rating > 0 && hotel_id = ?1) where id = ?1", nativeQuery = true)
	void updateHotelRating(Long hotelId);

	@Modifying
	@Query(value = "update isap.room set rating = (select avg(rating) from isap.room_rating where rating > 0 && room_id = ?1) where id = ?1", nativeQuery = true)
	void updateRoomRating(Long roomId);
	
}
