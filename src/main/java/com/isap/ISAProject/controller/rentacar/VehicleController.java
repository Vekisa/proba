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

import com.isap.ISAProject.model.exception.ResourceNotFoundException;
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.repository.rentacar.VehicleRepository;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
	@Autowired
	VehicleRepository vehicleRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Vehicle> getAllVehicles(){
		return vehicleRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Vehicle createVehicle(@Valid @RequestBody Vehicle veh) {
		return vehicleRepository.save(veh);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public Vehicle getVehicleById(@PathVariable(value="id") Long vehId) {
		return vehicleRepository.findById(vehId).orElseThrow(() -> new ResourceNotFoundException("RentACar", "id", vehId));
	}
	
	/*@PutMapping("/vehicles/{id}")
	public Vehicle updateVehicle(@PathVariable(value="id") Long vehId, @Valid @RequestBody Vehicle vehDetails) {
		Vehicle veh = vehicleRepository.findById(vehId).orElseThrow(() -> new ResourceNotFoundException("RentACar", "id", vehId));
		
		veh.setBranchOffice(vehDetails.getBranchOffice());
		veh.setDiscount(vehDetails.getDiscount());
		veh.setPricePerDay(vehDetails.getPricePerDay());
		
		veh.removeAllVehicleReservations();
		for(VehicleReservation vr : vehDetails.getVehicleReservations()) {
			veh.addVehicleReservation(vr);
		}
		
		return vehicleRepository.save(veh);
	}*/
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteVehicle(@PathVariable(value="id") Long vehId){
		Vehicle veh = vehicleRepository.findById(vehId).orElseThrow(() -> new ResourceNotFoundException("RentACar", "id", vehId));
		
		vehicleRepository.delete(veh);
		
		return ResponseEntity.ok().build();
	}
}
