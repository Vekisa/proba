package com.isap.ISAProject.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Resource;

import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;

public class HATEOASImplementorUser {

	public static Resource<RegisteredUser> createRegisteredUser(RegisteredUser registeredUser) {
		Resource<RegisteredUser> resource = new Resource<RegisteredUser>(registeredUser);
		return resource;
	}
	
	public static List<Resource<RegisteredUser>> createRegisteredUserList(List<RegisteredUser> registeredUsers) {
		List<Resource<RegisteredUser>> registeredUserList = new ArrayList<Resource<RegisteredUser>>();
		for(RegisteredUser registeredUser : registeredUsers) {
			registeredUserList.add(HATEOASImplementorUser.createRegisteredUser(registeredUser));
		}
		return registeredUserList;
	}
	
	public static Resource<Reservation> createReservation(Reservation reservation) {
		Resource<Reservation> resource = new Resource<Reservation>(reservation);
		return resource;
	}
	
	public static List<Resource<Reservation>> createReservationList(List<Reservation> reservations) {
		List<Resource<Reservation>> reservationList = new ArrayList<Resource<Reservation>>();
		for(Reservation reservation : reservations) {
			reservationList.add(HATEOASImplementorUser.createReservation(reservation));
		}
		return reservationList;
	}
	
	public static Resource<FriendRequest> createFriendRequest(FriendRequest request) {
		Resource<FriendRequest> resource = new Resource<FriendRequest>(request);
		return resource;
	}
	
	public static List<Resource<FriendRequest>> createFriendRequestList(List<FriendRequest> requests) {
		List<Resource<FriendRequest>> requestList = new ArrayList<Resource<FriendRequest>>();
		for(FriendRequest request : requests) {
			requestList.add(HATEOASImplementorUser.createFriendRequest(request));
		}
		return requestList;
	}
	
	public static Resource<Friendship> createFriendship(Friendship friendship) {
		Resource<Friendship> resource = new Resource<Friendship>(friendship);
		return resource;
	}
	
	public static List<Resource<Friendship>> createFriendshipList(List<Friendship> friendships) {
		List<Resource<Friendship>> requestList = new ArrayList<Resource<Friendship>>();
		for(Friendship friendship : friendships) {
			requestList.add(HATEOASImplementorUser.createFriendship(friendship));
		}
		return requestList;
	}
}
