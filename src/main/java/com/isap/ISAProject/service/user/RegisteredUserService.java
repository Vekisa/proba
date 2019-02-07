package com.isap.ISAProject.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.user.AuthorizationLevel;
import com.isap.ISAProject.model.user.ConfirmationToken;
import com.isap.ISAProject.model.user.ConfirmedReservation;
import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.model.user.PendingReservation;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.model.user.UserState;
import com.isap.ISAProject.repository.user.ConfirmationTokenRepository;
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
	
	@Autowired 
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	private FriendRequestService friendRequestService;
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<RegisteredUser> findAll(Pageable pageable) {
		logger.info("> fetch users at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<RegisteredUser> users = repository.findAll(pageable);
		logger.info("< users fetched");
		return users.getContent();
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public RegisteredUser findById(Long id) {
		logger.info("> fetch user with id {}", id);
		Optional<RegisteredUser> user = repository.findById(id);
		logger.info("< user fetched");
	
		if(user.isPresent()) return user.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user doesn't exist.");
	}
	
	@Transactional(readOnly = false)
	public RegisteredUser save(RegisteredUser user) {
		logger.info("> saving user with id {}", user.getId());
		repository.save(user);
		logger.info("< user saved");
		return user;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public RegisteredUser updateUser(Long oldUserId, RegisteredUser newUser) {
		logger.info("> updating user with id {}", oldUserId);
		RegisteredUser oldUser = this.findById(oldUserId);
		oldUser.setBonusPoints(newUser.getBonusPoints());
		oldUser.setCity(newUser.getCity());
		oldUser.setFirstName(newUser.getFirstName());
		oldUser.setLastName(newUser.getLastName());
		oldUser.setPhoneNumber(newUser.getPhoneNumber());
		oldUser.setUsername(newUser.getUsername());
		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		oldUser.setPassword(bc.encode(newUser.getPassword()));
		logger.info("< updated user");
		return null;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<RegisteredUser> getFriends(Long id) {
		logger.info("> fetching friends from user with id {}", id);
		List<RegisteredUser> list = repository.findFriendsOfUser(id);
		logger.info("< fetched friends");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested friends do not exist.");
	}
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<FriendRequest> getReceivedFriendRequestsOfUser(Long id) {
		logger.info("> fetching received friend requests of user with id {}", id);
		RegisteredUser oldUser = this.findById(id);
		List<FriendRequest> list = oldUser.getReceivedRequests();
		logger.info("< received friend requests fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested friend requests do not exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
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
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void removeFriend(Long self, Long friend) {
		logger.info("> removing friend with id {} as user with id {} (self)", friend, self);
		Friendship friendship = friendshipRepository.findFriendshipOf(self, friend);
		friendshipRepository.delete(friendship);
		logger.info("< friend removed");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Reservation> getActiveReservationsOfUser(Long id) {
		logger.info("> fetching active reservations of user with id {}", id);
		RegisteredUser user = this.findById(id);
		List<Reservation> list = filterActiveReservations(user.getConfirmedReservations());
		logger.info("< active reservations fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested reservations do not exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Reservation> getReservationHistoryOfUser(Long id) {
		logger.info("> fetching active reservations of user with id {}", id);
		RegisteredUser user = this.findById(id);
		List<Reservation> list = filterFinishedReservations(user.getConfirmedReservations());
		logger.info("< active reservations fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested reservations do not exist.");
	}

	private List<Reservation> filterActiveReservations(List<ConfirmedReservation> list) {
		logger.info("> filtering active reservations");
		List<Reservation> result = new ArrayList<Reservation>();
		Date currentDate = new Date();
		for(ConfirmedReservation confirmed : list)
			if(confirmed.getReservation().getEndDate().after(currentDate))
				result.add(confirmed.getReservation());
		logger.info("< active reservations filtered");
		return result;
	}
	
	private List<Reservation> filterFinishedReservations(List<ConfirmedReservation> list) {
		logger.info("> filtering finished reservations");
		List<Reservation> result = new ArrayList<Reservation>();
		Date currentDate = new Date();
		for(ConfirmedReservation confirmed : list)
			if(confirmed.getReservation().getEndDate().before(currentDate))
				result.add(confirmed.getReservation());
		logger.info("< finished reservations filtered");
		return result;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public RegisteredUser confirmAccount(String token) {
		ConfirmationToken confTok = confirmationTokenRepository.findByString(token);
		if(confTok == null)
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Ne postoji user sa poslatim tokenom");
		confTok.getUser().setAuthority(AuthorizationLevel.REGULAR_USER);
		confTok.getUser().setState(UserState.ACTIVE);
		repository.save(confTok.getUser());
		confirmationTokenRepository.save(confTok);
		return confTok.getUser();
	}
	public List<RegisteredUser> getRequestedUsers(Long userId) {
		logger.info("> fetching requested users of user with id {}", userId);
		RegisteredUser user = this.findById(userId);
		PageRequest p = new PageRequest(0, Integer.MAX_VALUE);
		List<FriendRequest> friendRequests = friendRequestService.findAll(p);
		List<RegisteredUser> users = new ArrayList<RegisteredUser>();
		for(FriendRequest friendRequest : friendRequests) {
			if(friendRequest.getSender().getUsername().equals(user.getUsername()))
				users.add(friendRequest.getReceiver());
		}
		logger.info("< active requested users fetched");
		if(!users.isEmpty())
			return users;
		
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Useri ne postoje");	
	}

	public List<Reservation> getPendingReservationsOfUser(Long userId) {
		logger.info("> fetching pending reservations for user with id {}", userId);
		RegisteredUser user = this.findById(userId);
		List<Reservation> list = new ArrayList<>();
		Date time = new Date();
		for(PendingReservation pending : user.getPendingReservations()) {
			long difference = time.getTime() - pending.getCreationTime().getTime();
			if(((int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)) < 3)
				list.add(pending.getReservation());
		}
		logger.info("< reservations fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Rezervacije ne postoje.");
	}
	
}
