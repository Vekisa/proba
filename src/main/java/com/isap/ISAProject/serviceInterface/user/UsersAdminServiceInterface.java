package com.isap.ISAProject.serviceInterface.user;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.user.UsersAdmin;

public interface UsersAdminServiceInterface {

	List<UsersAdmin> findAll(Pageable pageable);
	
	UsersAdmin findById(Long id);
	
	void delete(Long id);
	
	UsersAdmin update(Long id, UsersAdmin admin);
	
}
