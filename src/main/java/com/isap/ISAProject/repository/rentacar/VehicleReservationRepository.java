package com.isap.ISAProject.repository.rentacar;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.rentacar.VehicleReservation;


@Repository
public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long> {
	
	List<VehicleReservation> findAllByBeginDateBetween(Date begin, Date end);
	
	List<VehicleReservation> findAllByEndDateBetween(Date begin, Date end);
}
