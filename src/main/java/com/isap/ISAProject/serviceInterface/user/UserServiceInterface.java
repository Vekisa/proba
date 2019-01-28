package com.isap.ISAProject.serviceInterface.user;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;

public interface UserServiceInterface {

	List<RegisteredUser> findAll(Pageable pageable);
	
	RegisteredUser findById(Long id);
	
	RegisteredUser saveUser(RegisteredUser user);
	
	void deleteUser(Long id);
	
	RegisteredUser updateUser(Long oldUserId, RegisteredUser newUser);

	List<Reservation> getActiveReservationsOfUser(Long id);
	
	List<Reservation> getReservationHistoryOfUser(Long id);
	
	Reservation createReservationForUser(Long id);
	
	List<FriendRequest> getReceivedFriendRequestsOfUser(Long id);
	
	List<FriendRequest> getSentFriendRequestOfUser(Long id);
	
	FriendRequest sendFriendRequest(Long sendingUserId, Long receivingUserId);
	
	List<Friendship> getFriendshipsOfUser(Long userId);
	
	Friendship acceptFriendRequest(Long receingUserId, Long sendingUserId);

	void declineFriendRequest(Long receivingUserId, Long sendingUserId);
	
	void removeFriend(Long self, Long friend);

	List<RegisteredUser> getFriends(Long id);

}
