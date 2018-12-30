package com.isap.ISAProject.repository.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.user.Reservation;

public interface ReservationRepository extends PagingAndSortingRepository<Reservation, Long> {

}
