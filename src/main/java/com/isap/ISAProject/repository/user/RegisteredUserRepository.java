package com.isap.ISAProject.repository.user;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.user.RegisteredUser;

public interface RegisteredUserRepository extends PagingAndSortingRepository<RegisteredUser, Long> {
	Optional<RegisteredUser> findByUsername(String username);
}
