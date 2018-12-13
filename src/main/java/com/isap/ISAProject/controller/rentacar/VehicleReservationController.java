package com.isap.ISAProject.controller.rentacar;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.RentACar.VehicleReservation;
import com.isap.ISAProject.exception.ResourceNotFoundException;
import com.isap.ISAProject.repository.rentacar.VehicleReservationRepository;


@RestController
@RequestMapping("/vehicle_reservations")
public class VehicleReservationController {
	@Autowired
	VehicleReservationRepository vehicleReservationRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<VehicleReservation> getAllVehicleReservations(){
		return vehicleReservationRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public VehicleReservation createVehicleReservation(@Valid @RequestBody VehicleReservation veh) {
		return vehicleReservationRepository.save(veh);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public VehicleReservation getVehicleReservationById(@PathVariable(value="id") Long vehId) {
		return vehicleReservationRepository.findById(vehId).orElseThrow(() -> new ResourceNotFoundException("VehicleReservation", "id", vehId));
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public VehicleReservation updateVehicleReservation(@PathVariable(value="id") Long vehId, @Valid @RequestBody VehicleReservation vehDetails) {
		VehicleReservation veh = vehicleReservationRepository.findById(vehId).orElseThrow(() -> new ResourceNotFoundException("VehicleReservation", "id", vehId));
		
		veh.setBeginDate(vehDetails.getBeginDate());
		veh.setEndDate(vehDetails.getEndDate());
		veh.setPrice(vehDetails.getPrice());
		veh.setVehicle(vehDetails.getVehicle());
		
		return vehicleReservationRepository.save(veh);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteVehicleReservation(@PathVariable(value="id") Long vehId){
		VehicleReservation veh = vehicleReservationRepository.findById(vehId).orElseThrow(() -> new ResourceNotFoundException("VehicleReservation", "id", vehId));
		
		vehicleReservationRepository.delete(veh);
		
		return ResponseEntity.ok().build();
	}
}

