package com.isap.ISAProject.repository.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.user.CompanyAdmin;

public interface CompanyAdminRepository extends PagingAndSortingRepository<CompanyAdmin, Long> {

	CompanyAdmin findByUsername(String username);

	CompanyAdmin findByEmail(String email);

}
