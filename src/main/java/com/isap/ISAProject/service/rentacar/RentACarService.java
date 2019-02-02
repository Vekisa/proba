package com.isap.ISAProject.service.rentacar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.repository.airline.LocationRepository;
import com.isap.ISAProject.repository.rentacar.BranchOfficeRepository;
import com.isap.ISAProject.repository.rentacar.BranchOfficeSpecifications;
import com.isap.ISAProject.repository.rentacar.RentACarRepository;
import com.isap.ISAProject.repository.rentacar.RentACarSpecifications;
import com.isap.ISAProject.repository.rentacar.VehicleRepository;
import com.isap.ISAProject.repository.rentacar.VehicleReservationRepository;
import com.isap.ISAProject.repository.rentacar.VehicleReservationSpecifications;
import com.isap.ISAProject.serviceInterface.rentacar.RentACarServiceInterface;

@Service
@Transactional(readOnly = true)
public class RentACarService implements RentACarServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RentACarRepository repository;
	
	@Autowired
	private VehicleReservationRepository vrRepo;
	
	@Autowired
	private LocationRepository lRepo;
	
	@Autowired
	private BranchOfficeRepository broRepo;
	
	@Autowired
	VehicleRepository vRepo;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<RentACar> getAllRentACars(Pageable pageable) {
		logger.info("> fetch rent-a-cars at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<RentACar> rentacars = repository.findAll(pageable);
		logger.info("< rent-a-cars fetched");
		return rentacars.getContent();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public RentACar getRentACarById(Long id) {
		logger.info("> fetch rent-a-car with id {}", id);
		Optional<RentACar> rcar = repository.findById(id);
		logger.info("< rent-a-car fetched");
		if(rcar.isPresent()) return rcar.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Traženi rent-a-car servis nije pronađen.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RentACar saveRentACar(RentACar rentacar) {
		logger.info("> saving rent-a-car");
		repository.save(rentacar);
		logger.info("< rent-a-car saved");
		return rentacar;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
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
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteRentACar(Long id) {
		logger.info("> deleting rent-a-car with id {}", id);
		repository.delete(this.getRentACarById(id));
		logger.info("< rent-a-car deleted");
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<BranchOffice> getBranchOffices(Long id) {
		logger.info("> fetching branch offices for rent-a-car with id {}", id);
		RentACar rcar = this.getRentACarById(id);
		List<BranchOffice> boffs = rcar.getBranchOffices();
		logger.info("< branch offices fetched");
		return boffs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
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
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
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
	
	@Override
	public List<RentACar> search(Pageable pageable, String locationName, String rentacarName, Date beginDate, Date endDate) {
		logger.info("> searching rentacars");
		List<RentACar> rentacars = repository.findAll(RentACarSpecifications.withName(rentacarName));
		logger.info("rentacars: " + rentacars);
		List<BranchOffice> offices = new ArrayList<BranchOffice>();
		for(RentACar r : rentacars) {
			offices.addAll(broRepo.findAll(BranchOfficeSpecifications.withRentacar(r.getId())
					.and(BranchOfficeSpecifications.withLocationName(locationName))));
		}
		logger.info("offices: " + offices);
		
		List<VehicleReservation> reservations = new ArrayList<VehicleReservation>();
		for(BranchOffice b : offices) {
			reservations.addAll(vrRepo.findAll(VehicleReservationSpecifications.withBranchOffice(b.getId())
					.and(VehicleReservationSpecifications.withBeginDate(beginDate))
					.and(VehicleReservationSpecifications.withBeginDate(endDate))));
			reservations.addAll(vrRepo.findAll(VehicleReservationSpecifications.withBranchOffice(b.getId())
					.and(VehicleReservationSpecifications.withEndDate(beginDate))
					.and(VehicleReservationSpecifications.withEndDate(endDate))));
		}
		
		logger.info("reservations: " + reservations);
		
		List<RentACar> ret = new ArrayList<RentACar>();
		for(BranchOffice b : offices) {
			if(!ret.contains(b.getRentACar())) ret.add(b.getRentACar());
		}
		for(VehicleReservation vr : reservations) {
			if(!ret.contains(vr.getVehicle().getBranchOffice().getRentACar()))
				ret.add(vr.getVehicle().getBranchOffice().getRentACar());
		}
		
		for(Vehicle v : vRepo.findAll()) {
			if(v.getVehicleReservations().isEmpty() && offices.contains(v.getBranchOffice())) {
				if(!ret.contains(v.getBranchOffice().getRentACar())) ret.add(v.getBranchOffice().getRentACar());
			}
		}
		
		logger.info("ret: " + ret);
		logger.info("< rentacars found");
		return ret;
	}
	
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Map<Long, Double> getIncomeFor(Long id, Date beginDate, Date endDate) {
		logger.info("> calculating income");
		RentACar rents = this.getRentACarById(id);
		Map<Long, Double> incomeMap = new HashMap<Long, Double>();
		for(BranchOffice bo : rents.getBranchOffices())
			for(Vehicle v : bo.getVehicles())
				for(VehicleReservation reservation : v.getVehicleReservations())
					if(reservation.getBeginDate().after(beginDate) && reservation.getBeginDate().before(endDate))
						if(incomeMap.containsKey(reservation.getBeginDate().getTime())) {
							incomeMap.put(reservation.getBeginDate().getTime(), incomeMap.get(reservation.getBeginDate().getTime()) + reservation.getPrice());
						} else {
							incomeMap.put(reservation.getBeginDate().getTime(), reservation.getPrice());
						}
		logger.info("< income calculated");
		return incomeMap;
	}
	
}
