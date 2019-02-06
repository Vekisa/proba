package com.isap.ISAProject.service.rentacar;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.repository.rentacar.VehicleRepository;
import com.isap.ISAProject.repository.rentacar.VehicleReservationRepository;
import com.isap.ISAProject.serviceInterface.rentacar.VehicleReservationServiceInterface;

@Service
@Transactional(readOnly = true)
public class VehicleReservationService implements VehicleReservationServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private VehicleReservationRepository repository;
	
	@Autowired
	private VehicleRepository vRepo;	
	
	@Autowired
	private VehicleService vService;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<VehicleReservation> getAllVehicleReservation(Pageable pageable) {
		logger.info("> fetch vehicle reservations at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<VehicleReservation> vrs = repository.findAll(pageable);
		logger.info("< vehicle reservations fetched");
		return vrs.getContent();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public VehicleReservation getVehicleReservationById(Long id) {
		logger.info("> fetch vehicle reservation with id {}", id);
		Optional<VehicleReservation> vr = repository.findById(id);
		logger.info("< vehicle reservation fetched");
		if(vr.isPresent()) return vr.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tražena rezervacija vozila nije pronađena.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public VehicleReservation saveVehicleReservation(VehicleReservation vr) {
		logger.info("> saving vehicle reservation");
		repository.save(vr);
		logger.info("< vehicle reservation saved");
		return vr;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public VehicleReservation createVehicleReservationWithVehicleAndDates(Long vehicleId, Date beginDate, Date endDate) {
		logger.info("> creating vehicle reservation");
		Vehicle vehicle = vRepo.findById(vehicleId).get();
		if(!vService.checkIfVehicleIsFree(beginDate, endDate, vehicleId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Traženo vozilo nije slobodno.");
		}
		VehicleReservation vr  = new VehicleReservation(beginDate, endDate, vehicle);
		vr.setPrice(((endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24)) * vehicle.getPricePerDay());
		this.saveVehicleReservation(vr);
		vehicle.addVehicleReservation(vr);
		vService.saveVehicle(vehicle);
		return vr;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public VehicleReservation updateVehicleReservation(Long id, VehicleReservation vr) {
		logger.info("> updating vehicle reservation with id {}", id);
		VehicleReservation oldVr = this.getVehicleReservationById(id);
		oldVr.setBeginDate(vr.getBeginDate());
		oldVr.setEndDate(vr.getEndDate());
		oldVr.setPrice(vr.getPrice());
		this.saveVehicleReservation(oldVr);
		logger.info("< vehicle reservation updated and saved");
		return oldVr;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteVehicleReservation(Long id) {
		logger.info("> deleting vehicle reservation with id {}", id);
		repository.delete(this.getVehicleReservationById(id));
		logger.info("< vehicle reservation deleted");
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public VehicleReservation saveQuickVehicleReservation(VehicleReservation vehicleReservation, Long id) {
		Vehicle vehicle = vService.getVehicleById(id);
		if(!vService.checkIfVehicleIsFree(vehicleReservation.getBeginDate(), vehicleReservation.getEndDate(), vehicle.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soba za dati period nije slobodna");
		}
		Long difference = vehicleReservation.getEndDate().getTime() - vehicleReservation.getBeginDate().getTime();
		vehicleReservation.setPrice((int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS) * vehicle.getPricePerDay());
		vehicleReservation.setVehicle(vehicle);
		this.saveVehicleReservation(vehicleReservation);
		return vehicleReservation;
	}
	
}
