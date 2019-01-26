package com.isap.ISAProject.service.user;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.repository.user.FriendshipRepository;
import com.isap.ISAProject.serviceInterface.user.FriendshipServiceInterface;

@Service
public class FriendshipService implements FriendshipServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FriendshipRepository repository;
	
	@Override
	public List<Friendship> findAll(Pageable pageable) {
		logger.info("> fetching friendships at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<Friendship> friendships = repository.findAll(pageable);
		logger.info("< friendships fetched");
		return friendships.getContent();
	}

	@Override
	public Friendship findById(Long id) {
		logger.info("> fetching friendship with id {}", id);
		Optional<Friendship> friendship = repository.findById(id);
		logger.info("< friendship fetched");
		if(friendship.isPresent()) return friendship.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested friendship request doesn't exist.");
	}

}
