package com.isap.ISAProject.repository.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.user.RegisteredUser;

@Repository
public interface RegisteredUserRepository extends PagingAndSortingRepository<RegisteredUser, Long> {
	public RegisteredUser findByUsername(String username);
}
