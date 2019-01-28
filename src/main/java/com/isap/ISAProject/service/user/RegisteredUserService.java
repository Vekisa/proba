package com.isap.ISAProject.service.user;

import java.util.ArrayList;
import java.util.Date;
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

import com.isap.ISAProject.model.user.AuthorizationLevel;
import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.repository.user.FriendRequestRepository;
import com.isap.ISAProject.repository.user.FriendshipRepository;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;
import com.isap.ISAProject.serviceInterface.user.RegisteredUserServiceInterface;

@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class RegisteredUserService implements RegisteredUserServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RegisteredUserRepository repository;
	
	@Autowired
	private FriendshipRepository friendshipRepository;
	
	@Autowired
	private FriendRequestRepository friendRequestRepository;
	
	@Override
	public List<RegisteredUser> findAll(Pageable pageable) {
		logger.info("> fetch users at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<RegisteredUser> users = repository.findAll(pageable);
		logger.info("< users fetched");
		return users.getContent();
	}

	@Override
	public RegisteredUser findById(Long id) {
		logger.info("> fetch user with id {}", id);
		Optional<RegisteredUser> user = repository.findById(id);
		logger.info("< user fetched");
	
		if(user.isPresent()) return user.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user doesn't exist.");
	}

	private boolean usernameExists(String username) {
		return repository.findByUsername(username) != null;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public RegisteredUser updateUser(Long oldUserId, RegisteredUser newUser) {
		logger.info("> updating user with id {}", oldUserId);
		RegisteredUser oldUser = this.findById(oldUserId);
		if(usernameExists(newUser.getUsername()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already in use.");
		oldUser.setBonusPoints(newUser.getBonusPoints());
		oldUser.setCity(newUser.getCity());
		oldUser.setFirstName(newUser.getFirstName());
		oldUser.setLastName(newUser.getLastName());
		oldUser.setPhoneNumber(newUser.getPhoneNumber());
		oldUser.setUsername(newUser.getUsername());
		oldUser.setPassword(newUser.getPassword());
		logger.info("< updated user");
		return null;
	}

	@Override
	public List<RegisteredUser> getFriends(Long id) {
		logger.info("> fetching friends from user with id {}", id);
		List<RegisteredUser> list = repository.findFriendsOfUser(id);
		logger.info("< fetched friends");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested friends do not exist.");
	}
	
	@Override
	public List<FriendRequest> getReceivedFriendRequestsOfUser(Long id) {
		logger.info("> fetching received friend requests of user with id {}", id);
		RegisteredUser oldUser = this.findById(id);
		List<FriendRequest> list = oldUser.getReceivedRequests();
		logger.info("< received friend requests fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested friend requests do not exist.");
	}

	@Override
	public List<FriendRequest> getSentFriendRequestOfUser(Long id) {
		logger.info("> fetching sent friend requests of user with id {}", id);
		RegisteredUser oldUser = this.findById(id);
		List<FriendRequest> list = oldUser.getSentRequests();
		logger.info("< sent friend requests fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested friend requests do not exist.");
	}

	private boolean checkIfFriends(RegisteredUser user1, RegisteredUser user2) {
		return friendshipRepository.findFriendshipOf(user1.getId(), user2.getId()) != null;
	}
	
	private boolean checkIfRequestExists(RegisteredUser user1, RegisteredUser user2) {
		return friendRequestRepository.getFriendRequestBetween(user1.getId(), user2.getId()) != null;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public FriendRequest sendFriendRequest(Long sendingUserId, Long receivingUserId) {
		logger.info("> sending friend request from user with id {} to user with id {}", sendingUserId, receivingUserId);
		RegisteredUser sendingUser = this.findById(sendingUserId);
		RegisteredUser receivingUser = this.findById(receivingUserId);
		if(checkIfFriends(sendingUser, receivingUser)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Users are already friends.");
		if(checkIfRequestExists(sendingUser, receivingUser)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request already exists.");
		FriendRequest request = new FriendRequest();
		List<FriendRequest> sentRequests = sendingUser.getSentRequests();
		List<FriendRequest> receivedRequests = receivingUser.getReceivedRequests();
		sentRequests.add(request);
		receivedRequests.add(request);
		request.setSender(sendingUser);
		request.setReceiver(receivingUser);
		request.setRequestTime(new Date());
		repository.save(sendingUser);
		repository.save(receivingUser);
		logger.info("< friend request sent");
		return request;
	}

	@Override
	public List<Friendship> getFriendshipsOfUser(Long userId) {
		logger.info("> fetching friendships of user with id {}", userId);
		RegisteredUser user = this.findById(userId);
		List<Friendship> list = user.getFriendships();
		logger.info("< friendships fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested friendships do not exist.");
	}

	private FriendRequest getRequest(Long receivingUserId, Long sendingUserId) {
		logger.info("> fetching request of user with id {} from user with id {}", receivingUserId, sendingUserId);
		List<FriendRequest> list = this.getReceivedFriendRequestsOfUser(receivingUserId);
		for(FriendRequest fq : list)
			if(fq.getSender().getId().equals(sendingUserId))
				return fq;
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested friend request doesn't exist.");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	public Friendship acceptFriendRequest(Long recevingUserId, Long sendingUserId) {
		logger.info("> accepting friend request of user with id {}", recevingUserId);
		RegisteredUser friend1 = this.findById(sendingUserId);
		RegisteredUser friend2 = this.findById(recevingUserId);
		FriendRequest request = this.getRequest(recevingUserId, sendingUserId);
		Friendship friendship = new Friendship();
		friendship.setFriendsSince(new Date());
		friendship.getFriends().add(friend1);
		friendship.getFriends().add(friend2);
		friend1.getFriendships().add(friendship);
		friend1.getSentRequests().remove(request);
		friend2.getFriendships().add(friendship);
		friendshipRepository.save(friendship);
		repository.save(friend1);
		logger.info("< friend request accepted");
		return friendship;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void declineFriendRequest(Long receivingUserId, Long sendingUserId) {
		logger.info("> declining friend request receivied by user with id {} sent by user with id {}", receivingUserId, sendingUserId);
		RegisteredUser sender = this.findById(sendingUserId);
		RegisteredUser receiver = this.findById(receivingUserId);
		List<FriendRequest> requests = this.getSentFriendRequestOfUser(sendingUserId);
		FriendRequest request = null;
		for(FriendRequest rq : requests)
			if(rq.getReceiver().equals(receiver))
				request = rq;
		requests.remove(request);
		repository.save(sender);
		logger.info("< friend request declined");
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void removeFriend(Long self, Long friend) {
		logger.info("> removing friend with id {} as user with id {} (self)", friend, self);
		Friendship friendship = friendshipRepository.findFriendshipOf(self, friend);
		friendshipRepository.delete(friendship);
		logger.info("< friend removed");
	}

	@Override
	public List<Reservation> getActiveReservationsOfUser(Long id) {
		logger.info("> fetching active reservations of user with id {}", id);
		RegisteredUser user = this.findById(id);
		List<Reservation> list = filterActiveReservations(user.getReservations());
		logger.info("< active reservations fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested reservations do not exist.");
	}

	@Override
	public List<Reservation> getReservationHistoryOfUser(Long id) {
		logger.info("> fetching active reservations of user with id {}", id);
		RegisteredUser user = this.findById(id);
		List<Reservation> list = filterFinishedReservations(user.getReservations());
		logger.info("< active reservations fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested reservations do not exist.");
	}

	private List<Reservation> filterActiveReservations(List<Reservation> reservations) {
		List<Reservation> result = new ArrayList<Reservation>();
		Date currentDate = new Date();
		for(Reservation r : reservations)
			if(r.getEndDate().after(currentDate))
				result.add(r);
		return result;
	}
	
	private List<Reservation> filterFinishedReservations(List<Reservation> reservations) {
		List<Reservation> result = new ArrayList<Reservation>();
		Date currentDate = new Date();
		for(Reservation r : reservations)
			if(r.getEndDate().before(currentDate))
				result.add(r);
		return result;
	}

	@Override
	public Reservation createReservationForUser(Long id) {
		// TODO : implement
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Method not implemented.");
	}
	
	public RegisteredUser confirmAccount(String token) {
		RegisteredUser user = this.findById(Long.parseLong(token));
		user.setAuthority(AuthorizationLevel.REGULAR_USER);
		return user;
	}
	
}
