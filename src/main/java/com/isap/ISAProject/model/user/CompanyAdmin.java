package com.isap.ISAProject.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.rentacar.RentACar;

@Entity
@Table(name = "company_admin")
public class CompanyAdmin extends SystemUser {

	private static final long serialVersionUID = 6802189965555660737L;

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private AuthorizationLevel authority;
	
	@JsonIgnore
	@ManyToOne
	private Hotel hotel;

	@JsonIgnore
	@ManyToOne
	private RentACar rentACar;
	
	@JsonIgnore
	@ManyToOne
	private Airline airline;

	public CompanyAdmin() {}
	
	public CompanyAdmin(SystemUser user, AuthorizationLevel authorization) {
		super(user);
		this.setAuthority(authorization);
	}

	public Hotel getHotel() { return hotel; }

	public void setHotel(Hotel hotel) { this.hotel = hotel; }

	public RentACar getRentACar() { return rentACar; }

	public void setRentACar(RentACar rentACar) { this.rentACar = rentACar; }

	public Airline getAirline() { return airline; }

	public void setAirline(Airline airline) { this.airline = airline; }

	@Override
	public AuthorizationLevel getAuthority() { return this.authority; }

	@Override
	public void setAuthority(AuthorizationLevel authority) { this.authority = authority; }
	
}
