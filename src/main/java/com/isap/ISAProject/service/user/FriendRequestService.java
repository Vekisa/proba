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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.repository.user.FriendRequestRepository;
import com.isap.ISAProject.serviceInterface.user.FriendRequestServiceInterface;

@Service
public class FriendRequestService implements FriendRequestServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FriendRequestRepository repository;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<FriendRequest> findAll(Pageable pageable) {
		logger.info("> fetching friend requests at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<FriendRequest> requests = repository.findAll(pageable);
		logger.info("< requests fetched");
		return requests.getContent();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public FriendRequest findById(Long id) {
		logger.info("> fetching friend request with id {}", id);
		Optional<FriendRequest> request = repository.findById(id);
		logger.info("< request fetched");
		if(request.isPresent()) return request.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested friend request doesn't exist.");
	}

}
