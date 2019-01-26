package com.isap.ISAProject.serviceInterface.user;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.user.Friendship;

public interface FriendshipServiceInterface {

	List<Friendship> findAll(Pageable pageable);
	
	Friendship findById(Long id);
	
}
