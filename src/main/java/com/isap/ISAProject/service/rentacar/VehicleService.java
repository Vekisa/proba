package com.isap.ISAProject.service.rentacar;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.repository.rentacar.VehicleRepository;
import com.isap.ISAProject.serviceInterface.rentacar.VehicleServiceInterface;

@Service
@Transactional(readOnly = true)
public class VehicleService implements VehicleServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private VehicleRepository repository;
	
	@Override
	public List<Vehicle> getAllVehicles(Pageable pageable) {
		logger.info("> fetch vehicles at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Vehicle> vehicles = repository.findAll(pageable);
		logger.info("< vehicles fetched");
		return vehicles.getContent();
	}

	@Override
	public Vehicle getVehicleById(Long id) {
		logger.info("> fetch vehicle with id {}", id);
		Optional<Vehicle> veh = repository.findById(id);
		logger.info("< rent-a-car fetched");
		if(veh.isPresent()) return veh.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Traženo vozilo nije pronađeno.");
	}

	@Override
	@Transactional(readOnly = false)
	public Vehicle saveVehicle(Vehicle vehicle) {
		logger.info("> saving vehicle");
		repository.save(vehicle);
		logger.info("< vehicle saved");
		return vehicle;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Vehicle updateVehicle(Long id, Vehicle vehicle) {
		logger.info("> updating vehicle with id {}", id);
		Vehicle oldVeh = this.getVehicleById(id);
		oldVeh.setDiscount(vehicle.getDiscount());
		oldVeh.setPricePerDay(vehicle.getPricePerDay());
		this.saveVehicle(oldVeh);
		logger.info("< vehicle updated and saved");
		return oldVeh;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteVehicle(Long id) {
		logger.info("> deleting vehicle with id {}", id);
		repository.delete(this.getVehicleById(id));
		logger.info("< vehicle deleted");
	}

	@Override
	public List<VehicleReservation> getVehicleReservations(Long id) {
		logger.info("> fetching vehicle reservations for vehicle with id {}", id);
		Vehicle veh = this.getVehicleById(id);
		List<VehicleReservation> vr = veh.getVehicleReservations();
		logger.info("< vehicle reservations fetched");
		return vr;
	}

	@Override
	@Transactional(readOnly = false)
	public VehicleReservation addVehicleReservation(Long id, VehicleReservation vr) {
		logger.info("> adding vehicle reservation for vehicle with id {}", id);
		Vehicle veh = this.getVehicleById(id);
		veh.addVehicleReservation(vr);
		vr.setVehicle(veh);
		this.saveVehicle(veh);
		logger.info("< vehicle reservation added");
		return vr;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteVehicleReservation(Long id, VehicleReservation vr) {
		logger.info("> deleting vehicke reservation for vehicle with id {}", id);
		Vehicle veh = this.getVehicleById(id);
		veh.removeVehicleReservation(vr);
		this.saveVehicle(veh);
		logger.info("< vehicle reservation of vehicle with id {} deleted", id);
	}

}
