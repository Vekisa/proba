package com.isap.ISAProject.repository.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.user.Reservation;

@Transactional(propagation = Propagation.MANDATORY)
public interface ReservationRepository extends PagingAndSortingRepository<Reservation, Long> {

}
