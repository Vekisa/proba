package com.isap.ISAProject.repository.hotel;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.isap.ISAProject.model.hotel.Hotel;

@Transactional(propagation = Propagation.MANDATORY)
public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long> {
	
}
