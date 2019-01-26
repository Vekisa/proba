package com.isap.ISAProject.serviceInterface.rentacar;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.rentacar.VehicleReservation;

public interface VehicleReservationServiceInterface {
	List<VehicleReservation> getAllVehicleReservation(Pageable pageable);
	
	VehicleReservation getVehicleReservationById(Long id);
	
	VehicleReservation saveVehicleReservation(VehicleReservation vr);
	
	VehicleReservation updateVehicleReservation(Long id, VehicleReservation vr);
	
	void deleteVehicleReservation(Long id);
}
