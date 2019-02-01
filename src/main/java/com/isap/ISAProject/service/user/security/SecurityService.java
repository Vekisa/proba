package com.isap.ISAProject.service.user.security;

public interface SecurityService {
	
	public Boolean hasAccessToAirline(Long companyId);
	
	public Boolean hasAccessToHotel(Long companyId);
	
	public Boolean hasAccessToRentACar(Long companyId);
	
}
