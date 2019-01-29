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

import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.repository.rentacar.RentACarRepository;
import com.isap.ISAProject.serviceInterface.rentacar.RentACarServiceInterface;

@Service
@Transactional(readOnly = true)
public class RentACarService implements RentACarServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RentACarRepository repository;
	
	@Override
	public List<RentACar> getAllRentACars(Pageable pageable) {
		logger.info("> fetch rent-a-cars at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<RentACar> rentacars = repository.findAll(pageable);
		logger.info("< rent-a-cars fetched");
		return rentacars.getContent();
	}

	@Override
	public RentACar getRentACarById(Long id) {
		logger.info("> fetch rent-a-car with id {}", id);
		Optional<RentACar> rcar = repository.findById(id);
		logger.info("< rent-a-car fetched");
		if(rcar.isPresent()) return rcar.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Traženi rent-a-car servis nije pronađen.");
	}

	@Override
	@Transactional(readOnly = false)
	public RentACar saveRentACar(RentACar rentacar) {
		logger.info("> saving rent-a-car");
		repository.save(rentacar);
		logger.info("< rent-a-car saved");
		return rentacar;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public RentACar updateRentACar(Long id, RentACar rentacar) {
		logger.info("> updating rent-a-car with id {}", id);
		RentACar oldRcar = this.getRentACarById(id);
		oldRcar.setAddress(rentacar.getAddress());
		oldRcar.setDescription(rentacar.getDescription());
		oldRcar.setName(rentacar.getName());
		this.saveRentACar(oldRcar);
		logger.info("< rent-a-car updated and saved");
		return oldRcar;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRentACar(Long id) {
		logger.info("> deleting rent-a-car with id {}", id);
		repository.delete(this.getRentACarById(id));
		logger.info("< rent-a-car deleted");
	}

	@Override
	public List<BranchOffice> getBranchOffices(Long id) {
		logger.info("> fetching branch offices for rent-a-car with id {}", id);
		RentACar rcar = this.getRentACarById(id);
		List<BranchOffice> boffs = rcar.getBranchOffices();
		logger.info("< branch offices fetched");
		return boffs;
	}

	@Override
	@Transactional(readOnly = false)
	public BranchOffice addBranchOffice(Long id, BranchOffice brOff) {
		logger.info("> adding branch office for rent-a-car with id {}", id);
		RentACar rcar = this.getRentACarById(id);
		rcar.addBranchOffice(brOff);
		brOff.setRentACar(rcar);
		this.saveRentACar(rcar);
		logger.info("< branch office added");
		return brOff;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteBranchOffice(Long id, BranchOffice brOff) {
		logger.info("> deleting branch office for rent-a-car with id {}", id);
		RentACar rcar = this.getRentACarById(id);
		rcar.removeBranchOffice(brOff);
		this.saveRentACar(rcar);
		logger.info("< branch office of rent-a-car with id {} deleted", id);
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<CompanyAdmin> getAdminsOfRentACar(Long id) {
		logger.info("> fetching admins of rent-a-car with id {}", id);
		RentACar rentACar = this.getRentACarById(id);
		List<CompanyAdmin> list = rentACar.getAdmins();
		logger.info("< admins fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested admins do not exist.");
	}
	
}
