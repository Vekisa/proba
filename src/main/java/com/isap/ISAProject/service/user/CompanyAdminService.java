package com.isap.ISAProject.service.user;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.Company;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.model.user.AuthorizationLevel;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.repository.airline.AirlineRepository;
import com.isap.ISAProject.repository.hotel.HotelRepository;
import com.isap.ISAProject.repository.rentacar.RentACarRepository;
import com.isap.ISAProject.repository.user.CompanyAdminRepository;
import com.isap.ISAProject.serviceInterface.user.CompanyAdminServiceInterface;

@Service
public class CompanyAdminService implements CompanyAdminServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CompanyAdminRepository companyAdminsRepository;

	@Autowired
	private AirlineRepository airlineRepository;

	@Autowired
	private HotelRepository hotelRepository;
	
	@Autowired
	private RentACarRepository rentACarRepository;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<CompanyAdmin> findAll(Pageable pageable) {
		logger.info("> fetching company admins");
		Page<CompanyAdmin> admins = companyAdminsRepository.findAll(pageable);
		logger.info("< company admins fetched");
		return admins.getContent();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public CompanyAdmin findById(Long id) {
		logger.info("> fetching company admin with id {}", id);
		Optional<CompanyAdmin> admin = companyAdminsRepository.findById(id);
		logger.info("< company admin fetched");
		if(admin.isPresent()) return admin.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested admin doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void delete(Long id) {
		logger.info("> deleting company admin with id {}", id);
		CompanyAdmin admin = this.findById(id);
		companyAdminsRepository.delete(admin);
		logger.info("< company admin deleted");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public CompanyAdmin updateAdmin(Long id, CompanyAdmin newAdmin) {
		logger.info("> updating company admin with id {}", id);
		CompanyAdmin oldAdmin = this.findById(id);
		oldAdmin.setCity(newAdmin.getCity());
		oldAdmin.setFirstName(newAdmin.getFirstName());
		oldAdmin.setLastName(newAdmin.getLastName());
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		oldAdmin.setPassword(bc.encode(newAdmin.getPassword()));
		oldAdmin.setPhoneNumber(newAdmin.getPhoneNumber());
		oldAdmin.setUsername(newAdmin.getUsername());
		logger.info("< company admin updated");
		return oldAdmin;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public CompanyAdmin setAuthorization(Long id, AuthorizationLevel authorization) {
		logger.info("> setting authorization level for company admin with id {}", id);
		CompanyAdmin admin = this.findById(id);
		checkIfValidAuthorization(authorization);
		if(admin.getAuthority() != authorization) {
			admin.setAirline(null);
			admin.setHotel(null);
			admin.setRentACar(null);
			admin.setAuthority(authorization);
		}
		companyAdminsRepository.save(admin);
		logger.info("< authorization level set");
		return admin;
	}
	
	private void checkIfValidAuthorization(AuthorizationLevel authorization) {
		if(authorization.equals(AuthorizationLevel.AIRLINE_ADMIN)) return;
		if(authorization.equals(AuthorizationLevel.HOTEL_ADMIN)) return;
		if(authorization.equals(AuthorizationLevel.RENT_A_CAR_ADMIN)) return;
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid authorization level.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public CompanyAdmin setCompany(Long id, Long companyId) {
		logger.info("> setting company for company admin with id {}", id);
		CompanyAdmin admin = this.findById(id);
		Company company = this.findCompany(companyId, admin.getAuthority());
		setCompanyForAdmin(admin, company);
		companyAdminsRepository.save(admin);
		logger.info("< company set");
		return admin;
	}

	private void setCompanyForAdmin(CompanyAdmin admin, Company company) {
		if(company instanceof Airline) admin.setAirline((Airline) company);
		if(company instanceof Hotel) admin.setHotel((Hotel) company);
		if(company instanceof RentACar) admin.setRentACar((RentACar) company);
	}

	private Company findCompany(Long companyId, AuthorizationLevel authority) {
		Company company = null;
		if(authority.equals(AuthorizationLevel.AIRLINE_ADMIN)) {
			Optional<Airline> airline = airlineRepository.findById(companyId);
			if(airline.isPresent()) company = airline.get();
		} else if(authority.equals(AuthorizationLevel.HOTEL_ADMIN)) {
			Optional<Hotel> hotel = hotelRepository.findById(companyId);
			if(hotel.isPresent()) company = hotel.get();
		} else if(authority.equals(AuthorizationLevel.RENT_A_CAR_ADMIN)) {
			Optional<RentACar> rentACar = rentACarRepository.findById(companyId);
			if(rentACar.isPresent()) company = rentACar.get();
		}
		if(company != null) return company;
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested company doesn't exist.");
	}

}
