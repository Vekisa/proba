package com.isap.ISAProject.serviceInterface.user;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.user.FriendRequest;

public interface FriendRequestServiceInterface {

	List<FriendRequest> findAll(Pageable pageable);
	
	FriendRequest findById(Long id);
	
}
