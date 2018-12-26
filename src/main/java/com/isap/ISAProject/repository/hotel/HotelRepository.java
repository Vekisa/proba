package com.isap.ISAProject.repository.hotel;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.hotel.Hotel;

public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long> {

}
