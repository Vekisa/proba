package com.isap.ISAProject.serviceInterface.user;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.user.AuthorizationLevel;
import com.isap.ISAProject.model.user.CompanyAdmin;

public interface CompanyAdminServiceInterface {

	List<CompanyAdmin> findAll(Pageable pageable);
	
	CompanyAdmin findById(Long id);
	
	void delete(Long id);
	
	CompanyAdmin updateAdmin(Long id, CompanyAdmin newAdmin);
	
	CompanyAdmin setAuthorization(Long id, AuthorizationLevel authorization);
	
	CompanyAdmin setCompany(Long id, Long companyId);
	
}
