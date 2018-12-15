package com.isap.ISAProject.repository.rentacar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.rentacar.VehicleReservation;


@Repository
public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long> {

}
