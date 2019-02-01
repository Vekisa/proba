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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.repository.airline.LocationRepository;
import com.isap.ISAProject.repository.rentacar.BranchOfficeRepository;
import com.isap.ISAProject.serviceInterface.rentacar.BranchOfficeServiceInterface;

@Service
@Transactional(readOnly = true)
public class BranchOfficeService implements BranchOfficeServiceInterface{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BranchOfficeRepository repository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<BranchOffice> getAllBranchOffices(Pageable pageable) {
		logger.info("> fetch branch offices at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<BranchOffice> branchOffices = repository.findAll(pageable);
		logger.info("< branch offices fetched");
		return branchOffices.getContent();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public BranchOffice getBranchOfficeById(Long id) {
		logger.info("> fetch branch office with id {}", id);
		Optional<BranchOffice> broff = repository.findById(id);
		logger.info("< branch office fetched");
		if(broff.isPresent()) return broff.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tražena filijala rent-a-car servisa nije pronađena.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public BranchOffice saveBranchOffice(BranchOffice branchOffice) {
		logger.info("> saving branch office");
		repository.save(branchOffice);
		logger.info("< branch office saved");
		return branchOffice;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public BranchOffice updateBranchOffice(Long id, BranchOffice branchOffice) {
		logger.info("> updating branch offices with id {}", id);
		BranchOffice oldBrOff = this.getBranchOfficeById(id);
		oldBrOff.setAddress(branchOffice.getAddress());
		oldBrOff.setRentACar(branchOffice.getRentACar());
		this.saveBranchOffice(oldBrOff);
		logger.info("< branch office updated and saved");
		return oldBrOff;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteBranchOfficeWithId(Long id) {
		logger.info("> deleting branch office with id {}", id);
		repository.delete(this.getBranchOfficeById(id));
		logger.info("< branch office deleted");
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Vehicle> getVehiclesForBranchOfficeWithId(Long id) {
		logger.info("fetching vehicles for branch office with id {}", id);
		BranchOffice brOff = this.getBranchOfficeById(id);
		List<Vehicle> vehicles = brOff.getVehicles();
		logger.info("< vehicles fetched");
		return vehicles;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public Vehicle addVehicleForBranchOfficeWithId(Long id, Vehicle vehicle) {
		logger.info("adding vehicle for branch office with id {}", id);
		BranchOffice brOff = this.getBranchOfficeById(id);
		brOff.addVehicle(vehicle);
		vehicle.setBranchOffice(brOff);
		this.saveBranchOffice(brOff);
		logger.info("< vehicle added");
		return vehicle;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteVehicleForBranchOfficeWithId(Long id, Vehicle vehicle) {
		logger.info("> deleting vehicle for branch office with id {}", id);
		BranchOffice brOff = this.getBranchOfficeById(id);
		brOff.removeVehicle(vehicle);
		this.saveBranchOffice(brOff);
		logger.info("< vehicle of branch office with id {} deleted", id);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public BranchOffice setLocationOfBranchOffice(Long id, Long locationId) {
		logger.info("> changing location of branch office with id {}", id);
		BranchOffice office = this.getBranchOfficeById(id);
		Location location = this.getLocationOfRentACar(locationId);
		location.getBranchOffices().add(office);
		office.setLocation(location);
		locationRepository.save(location);
		logger.info("< changed location of branch office with id {}", id);
		return office;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Location getLocationOfRentACar(Long id) {
		logger.info("> fetching location with id {}", id);
		Optional<Location> location = locationRepository.findById(id);
		logger.info("< location fetched");
		if(location.isPresent()) return location.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested location doesn't exist.");
	}
	
}
