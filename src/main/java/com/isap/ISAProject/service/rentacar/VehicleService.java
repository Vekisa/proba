package com.isap.ISAProject.service.rentacar;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.repository.rentacar.BranchOfficeRepository;
import com.isap.ISAProject.repository.rentacar.VehicleRepository;
import com.isap.ISAProject.serviceInterface.rentacar.VehicleServiceInterface;

@Service
@Transactional(readOnly = true)
public class VehicleService implements VehicleServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private VehicleRepository repository;
	
	@Autowired
	private BranchOfficeRepository officeRepository;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Vehicle> getAllVehicles(Pageable pageable) {
		logger.info("> fetch vehicles at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Vehicle> vehicles = repository.findAll(pageable);
		logger.info("< vehicles fetched");
		return vehicles.getContent();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Vehicle getVehicleById(Long id) {
		logger.info("> fetch vehicle with id {}", id);
		Optional<Vehicle> veh = repository.findById(id);
		logger.info("< rent-a-car fetched");
		if(veh.isPresent()) return veh.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Traženo vozilo nije pronađeno.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Vehicle saveVehicle(Vehicle vehicle) {
		logger.info("> saving vehicle");
		repository.save(vehicle);
		logger.info("< vehicle saved");
		return vehicle;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public Vehicle updateVehicle(Long id, Vehicle vehicle) {
		logger.info("> updating vehicle with id {}", id);
		Vehicle oldVeh = this.getVehicleById(id);
		oldVeh.setDiscount(vehicle.getDiscount());
		oldVeh.setPricePerDay(vehicle.getPricePerDay());
		oldVeh.setBrand(vehicle.getBrand());
		oldVeh.setModel(vehicle.getModel());
		oldVeh.setProductionYear(vehicle.getProductionYear());
		oldVeh.setType(vehicle.getType());
		this.saveVehicle(oldVeh);
		logger.info("< vehicle updated and saved");
		return oldVeh;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteVehicle(Long id) {
		logger.info("> deleting vehicle with id {}", id);
		repository.delete(this.getVehicleById(id));
		logger.info("< vehicle deleted");
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<VehicleReservation> getVehicleReservations(Long id) {
		logger.info("> fetching vehicle reservations for vehicle with id {}", id);
		Vehicle veh = this.getVehicleById(id);
		List<VehicleReservation> vr = veh.getVehicleReservations();
		logger.info("< vehicle reservations fetched");
		return vr;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
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
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteVehicleReservation(Long id, VehicleReservation vr) {
		logger.info("> deleting vehicke reservation for vehicle with id {}", id);
		Vehicle veh = this.getVehicleById(id);
		veh.removeVehicleReservation(vr);
		this.saveVehicle(veh);
		logger.info("< vehicle reservation of vehicle with id {} deleted", id);
	}

	public BranchOffice getBranchOfficeOfVehicle(Long id) {
		logger.info("> fetching office of vehicle with id {}", id);
		Vehicle vehicle = this.getVehicleById(id);
		BranchOffice office = vehicle.getBranchOffice();
		logger.info("< vehicle fetched");
		if(office != null) return office;
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tražena filijala nije pronađena.");
	}

	public BranchOffice setBranchOfficeOfVehicle(Long id, Long officeId) {
		logger.info("> fetching office of vehicle with id {}", id);
		Vehicle vehicle = this.getVehicleById(id);
		Optional<BranchOffice> office = officeRepository.findById(officeId);
		logger.info("< vehicle fetched");
		if(office.isPresent()) {
			vehicle.setBranchOffice(office.get());
			repository.save(vehicle);
			return office.get();
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tražena filijala nije pronađena.");
	}
	
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean checkIfVehicleIsFree(Date start, Date end, Long vehicleId) {
		logger.info("> check if vehicle is free", vehicleId);
		Vehicle vehicle = this.getVehicleById(vehicleId);
		Date reservedStart = null;
		Date reservedEnd = null;
		logger.info("< check if vehicle is free");
		
		if(start.after(end))
			return false;
		
		for(VehicleReservation vehicleReservation :vehicle.getVehicleReservations()) {
			reservedStart = vehicleReservation.getBeginDate();
			reservedEnd = vehicleReservation.getEndDate();
			if((start.after(reservedStart) && start.before(reservedEnd)) || (end.after(reservedStart) && end.before(reservedEnd)))
				return false;
		}
		return true;
	}

}
