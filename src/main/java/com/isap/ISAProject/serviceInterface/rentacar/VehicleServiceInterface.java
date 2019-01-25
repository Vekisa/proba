package com.isap.ISAProject.serviceInterface.rentacar;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.rentacar.VehicleReservation;

public interface VehicleServiceInterface {
	List<Vehicle> getAllVehicles(Pageable pageable);
	
	Vehicle getVehicleById(Long id);
	
	Vehicle saveVehicle(Vehicle vehicle);
	
	Vehicle updateVehicle(Long id, Vehicle vehicle);
	
	void deleteVehicle(Long id);
	
	List<VehicleReservation> getVehicleReservations(Long id);
	
	VehicleReservation addVehicleReservation(Long id, VehicleReservation vr);
	
	void deleteVehicleReservation(Long id, VehicleReservation vr);
}
