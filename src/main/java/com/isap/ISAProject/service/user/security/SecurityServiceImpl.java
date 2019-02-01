package com.isap.ISAProject.service.user.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.repository.airline.AirlineRepository;
import com.isap.ISAProject.repository.hotel.HotelRepository;
import com.isap.ISAProject.repository.rentacar.RentACarRepository;
import com.isap.ISAProject.repository.user.CompanyAdminRepository;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private AirlineRepository airlineRepository;
	
	@Autowired
	private HotelRepository hotelRepository;
	
	@Autowired
	private RentACarRepository rentACarRepository;
	
	@Autowired
	private CompanyAdminRepository companyAdminRepository;
	
	private CompanyAdmin getCurrentAdmin() {
		CompanyAdmin admin = companyAdminRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if(admin != null) {
			return admin;
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested admin doesn't exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Boolean hasAccessToAirline(Long companyId) {
		Optional<Airline> airline = airlineRepository.findById(companyId);
		if(airline.isPresent()) {
				return airline.get().getAdmins().contains(this.getCurrentAdmin());
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested company doesn't exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Boolean hasAccessToHotel(Long companyId) {
		Optional<Hotel> hotel = hotelRepository.findById(companyId);
		if(hotel.isPresent()) {
				return hotel.get().getAdmins().contains(this.getCurrentAdmin());
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested company doesn't exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Boolean hasAccessToRentACar(Long companyId) {
		Optional<RentACar> rentACar = rentACarRepository.findById(companyId);
		if(rentACar.isPresent()) {
				return rentACar.get().getAdmins().contains(this.getCurrentAdmin());
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested company doesn't exist.");
	}
	
	

}
