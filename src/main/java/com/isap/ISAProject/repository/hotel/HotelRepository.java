package com.isap.ISAProject.repository.hotel;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.hotel.Hotel;

@Transactional(propagation = Propagation.MANDATORY)

public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

	@Modifying
	@Query(value = "update hotel set rating = (select avg(rating) from hotel_rating where rating > 0 && hotel_id = ?1) where id = ?1", nativeQuery = true)
	void updateHotelRating(Long hotelId);

	@Modifying
	@Query(value = "update room set rating = (select avg(rating) from room_rating where rating > 0 && room_id = ?1) where id = ?1", nativeQuery = true)
	void updateRoomRating(Long roomId);
	
}
