package com.isap.ISAProject.repository.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.user.UsersAdmin;

@Transactional(propagation = Propagation.MANDATORY)
public interface UsersAdminRepository extends PagingAndSortingRepository<UsersAdmin, Long> {

	UsersAdmin findByUsername(String username);

	UsersAdmin findByEmail(String email);

}
