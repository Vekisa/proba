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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;
import com.isap.ISAProject.service.EmailSenderService;
import com.isap.ISAProject.serviceInterface.user.UserServiceInterface;

@Service
public class UserService implements UserServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RegisteredUserRepository repository;
	
	@Autowired
    private EmailSenderService emailSenderService;
	
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
		
		/*SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("vmosorinski@gmail.com");
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("bice10izise@gmail.com");
        mailMessage.setText("CAO");
        emailSenderService.sendEmail(mailMessage);*/
		
		if(user.isPresent()) return user.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user doesn't exist.");
	}

	@Override
	public RegisteredUser saveUser(RegisteredUser user) {
		logger.info("> saving user");
		// TODO : Biznis logika memorisanja korisnika + slanje mejla u slucaju uspesnosti + timeout zahteva
		repository.save(user);
		logger.info("< user saved");
		
		return user;
	}

	@Override
	public void deleteUser(Long id) {
		logger.info("> deleting user");
		// TODO : Biznis logika brisanja korisnika - kada se sme brisati, treba obrisati sve zahteve i prijateljstva koji su povezani, sta sa rez?
		repository.deleteById(id);
		logger.info("< user deleted");
	}

	@Override
	public RegisteredUser updateUser(Long oldUserId, RegisteredUser newUser) {
		logger.info("> updating user with id {}", oldUserId);
		RegisteredUser oldUser = this.findById(oldUserId);
		// TODO : Biznis logika promene poena i mejla
		oldUser.setBonusPoints(newUser.getBonusPoints());
		oldUser.setCity(newUser.getCity());
		oldUser.setEmail(newUser.getEmail());
		oldUser.setFirstName(newUser.getFirstName());
		oldUser.setLastName(newUser.getLastName());
		oldUser.setPhoneNumber(newUser.getPhoneNumber());
		oldUser.setPassword(newUser.getPassword());
		logger.info("< updated user");
		return null;
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

	private void checkIfFriends(RegisteredUser user1, RegisteredUser user2) {
		// TODO : implement
	}
	
	private void checkIfRequestExists(RegisteredUser user1, RegisteredUser user2) {
		// TODO : implement
	}
	
	@Override
	public FriendRequest sendFriendRequest(Long sendingUserId, Long receivingUserId) {
		logger.info("> sending friend request from user with id {} to user with id {}", sendingUserId, receivingUserId);
		RegisteredUser sendingUser = this.findById(sendingUserId);
		RegisteredUser receivingUser = this.findById(receivingUserId);
		checkIfFriends(sendingUser, receivingUser);
		checkIfRequestExists(sendingUser, receivingUser);
		FriendRequest request = new FriendRequest();
		List<FriendRequest> sentRequests = this.getSentFriendRequestOfUser(sendingUserId);
		List<FriendRequest> receivedRequests = this.getReceivedFriendRequestsOfUser(receivingUserId);
		sentRequests.add(request);
		receivedRequests.add(request);
		request.setSender(sendingUser);
		request.setReceiver(receivingUser);
		request.setRequestTime(new Date());
		logger.info("< friend request sent");
		return null;
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
	public Friendship acceptFriendRequest(Long recevingUserId, Long sendingUserId) {
		logger.info("> accepting friend request of user with id {}", recevingUserId);
		// TODO : Prihvatanje prijateljstva - neophodno je obrisati zahtev za prijateljstvo, a kreirati samo prijateljstvo
		logger.info("< friend request accepted");
		return null;
	}
	
	@Override
	public void declineFriendRequest(Long receivingUserId, Long sendingUserId) {
		logger.info("> declining friend request receivied by user with id {} sent by user with id {}", receivingUserId, sendingUserId);
		// TODO : Odbijanje zahtevanje - neophodno je obrisati zahtev za prijateljstvo
		logger.info("< friend request declinged");
	}
	
	@Override
	public void cancelFriendRequest(Long sendingUserId, Long receivingUserId) {
		logger.info("> cancelling friend request sent by user with id {} to user with id {}", sendingUserId, receivingUserId);
		// TODO : Odustajanje od zahteva prijateljstva - neophodno je obrisati zahtev za prijateljtsvo
		logger.info("< friend request cancelled");
	}
	
	@Override
	public void removeFriend(Long self, Long friend) {
		logger.info("> removing friend with id {} as user with id {} (self)", friend, self);
		// TODO : Uklanjanje prijatelja iz liste prijatelja - neophodno je obrisati instancu prijateljstva i ukloniti je iz lista
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
	
}
