package com.isap.ISAProject.service.rentacar;

import java.util.ArrayList;
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
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.repository.airline.LocationRepository;
import com.isap.ISAProject.repository.rentacar.BranchOfficeRepository;
import com.isap.ISAProject.repository.rentacar.RentACarRepository;
import com.isap.ISAProject.repository.rentacar.VehicleReservationRepository;
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

	@Override
	public List<RentACar> search(Pageable pageable, String locationName, String rentacarName/*, Date beginDate, Date endDate*/) {
		logger.info("> searching");
		List<RentACar> rentacarsByName = repository.findByName(rentacarName);
		
		List<Location> locationsByName = lRepo.findByName(locationName);
		List<BranchOffice> offices = new ArrayList<BranchOffice>();
		
		for(Location l : locationsByName) {
			List<BranchOffice> pom = broRepo.findByLocation(l);
			for(BranchOffice bro : pom) {
				offices.add(bro);
			}
		}
		
		List<RentACar> ret = new ArrayList<RentACar>();
		for(BranchOffice bro : offices) {
			if(rentacarsByName.contains(bro.getRentACar())) {
				ret.add(bro.getRentACar());
			}
		}
		
		//reservsBegin = vrRepo.findByBeginDate(beginDate);
		//reservsEnd = vrRepo.findByEndDate(endDate);
		
		/*if(!locationName.equals("")) {
			ret = searchByLocation(locationName, ret);
			System.out.println("search by location");
		}
		if(!rentacarName.equals("")) {
			ret = searchByName(rentacarName, ret);
			System.out.println("search by name");
		}
		if(beginDate != null && endDate != null) {
			ret = searchByDate(beginDate, endDate, ret);
			System.out.println("search by date");
		}*/
		logger.info("> searching done");
		return ret;
	}
	
	/*private List<RentACar> searchByLocation(String locationName, List<RentACar> source){
		List<RentACar> ret = new ArrayList<RentACar>();
		for(RentACar rac : source) {
			for(BranchOffice bro : rac.getBranchOffices()) {
				if(bro.getLocation().getName().equals(locationName)) {
					ret.add(rac);
				}
			}
		}
		return ret;
	}
	
	private List<RentACar> searchByName(String name, List<RentACar> source){
		ArrayList<RentACar> ret = new ArrayList<RentACar>();
		for(RentACar rac : source) {
			if(rac.getName().equals(name)) {
				ret.add(rac);
			}
		}
		return ret;
	}
	
	private List<RentACar> searchByDate(Date beginDate, Date endDate, List<RentACar> source){
		ArrayList<RentACar> ret = new ArrayList<RentACar>();
		for(RentACar rac : source) {
			for(BranchOffice bro : rac.getBranchOffices()) {
				for(Vehicle veh : bro.getVehicles()) {
					for(VehicleReservation res : veh.getVehicleReservations()) {
						if(!(beginDate.after(res.getBeginDate()) && beginDate.before(res.getEndDate()) && endDate.after(res.getBeginDate()) && endDate.before(res.getEndDate()))) {
							ret.add(rac);
						}
					}
				}
			}
		}
		return ret;
	}*/
}
