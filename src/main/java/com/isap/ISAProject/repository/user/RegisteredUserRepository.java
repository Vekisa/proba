package com.isap.ISAProject.repository.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.user.RegisteredUser;

public interface RegisteredUserRepository extends PagingAndSortingRepository<RegisteredUser, Long> {

}
