package com.isap.ISAProject.controller.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Resource;

import com.isap.ISAProject.domain.security.CerberusUser;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.model.user.UsersAdmin;

public class HATEOASImplementorUsers {

	public static Resource<RegisteredUser> createRegisteredUser(RegisteredUser registeredUser) {
		Resource<RegisteredUser> resource = new Resource<RegisteredUser>(registeredUser);
		resource.add(linkTo(methodOn(RegisteredUserController.class).getFriendsOfUser(registeredUser.getId())).withRel("friends"));
		
		return resource;
	}
	
	public static List<Resource<RegisteredUser>> createRegisteredUserList(List<RegisteredUser> registeredUsers) {
		List<Resource<RegisteredUser>> registeredUserList = new ArrayList<Resource<RegisteredUser>>();
		for(RegisteredUser registeredUser : registeredUsers) {
			registeredUserList.add(HATEOASImplementorUsers.createRegisteredUser(registeredUser));
		}
		return registeredUserList;
	}
	
	public static Resource<CerberusUser> createCerberusUser(CerberusUser cerberusUser) {
		Resource<CerberusUser> resource = new Resource<CerberusUser>(cerberusUser);
		resource.add(linkTo(methodOn(RegisteredUserController.class).getFriendsOfUser(cerberusUser.getId())).withRel("friends"));
		resource.add(linkTo(methodOn(RegisteredUserController.class).getReceivedFriendRequestsForUserWithId(cerberusUser.getId())).withRel("friend_requests"));
		resource.add(linkTo(methodOn(RegisteredUserController.class).getReservationsHistoryForUserWithId(cerberusUser.getId())).withRel("reservations_history"));
		resource.add(linkTo(methodOn(RegisteredUserController.class).getReservationsForUserWithId(cerberusUser.getId())).withRel("active_reservations"));
		resource.add(linkTo(methodOn(RegisteredUserController.class).getRequestedUsersFromUser(cerberusUser.getId())).withRel("sent_friend_request"));
		resource.add(linkTo(methodOn(RegisteredUserController.class).removeFriend(cerberusUser.getId(),0l)).withRel("remove_friend"));
		resource.add(linkTo(methodOn(RegisteredUserController.class).cancelFriendRequest(cerberusUser.getId(),0l)).withRel("cancel_friend_request"));
		resource.add(linkTo(methodOn(RegisteredUserController.class).sendFriendRequest(cerberusUser.getId(),0l)).withRel("send_request"));
		resource.add(linkTo(methodOn(RegisteredUserController.class).acceptFriendRequest(cerberusUser.getId(),0l)).withRel("accept_request"));
		resource.add(linkTo(methodOn(RegisteredUserController.class).declineFriendRequest(cerberusUser.getId(),0l)).withRel("decline_request"));
		return resource;
	}
	
	public static Resource<Reservation> createReservation(Reservation reservation) {
		Resource<Reservation> resource = new Resource<Reservation>(reservation);
		resource.add(linkTo(methodOn(ReservationController.class).getReservationById(reservation.getId())).withRel("self"));
		resource.add(linkTo(methodOn(ReservationController.class).getLocationOfReservation(reservation.getId())).withRel("location"));
		return resource;
	}
	
	public static List<Resource<Reservation>> createReservationList(List<Reservation> reservations) {
		List<Resource<Reservation>> reservationList = new ArrayList<Resource<Reservation>>();
		for(Reservation reservation : reservations) {
			reservationList.add(HATEOASImplementorUsers.createReservation(reservation));
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
			requestList.add(HATEOASImplementorUsers.createFriendRequest(request));
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
			requestList.add(HATEOASImplementorUsers.createFriendship(friendship));
		}
		return requestList;
	}

	public static Resource<CompanyAdmin> createCompanyAdmin(CompanyAdmin admin) {
		Resource<CompanyAdmin> resource = new Resource<CompanyAdmin>(admin);
		return resource;
	}

	public static Resource<UsersAdmin> createUsersAdmin(UsersAdmin admin) {
		Resource<UsersAdmin> resource = new Resource<UsersAdmin>(admin);
		return resource;
	}

	public static List<Resource<UsersAdmin>> createUsersAdminList(List<UsersAdmin> admins) {
		List<Resource<UsersAdmin>> list = new ArrayList<Resource<UsersAdmin>>();
		for(UsersAdmin admin : admins)
			list.add(HATEOASImplementorUsers.createUsersAdmin(admin));
		return list;
	}

	public static List<Resource<CompanyAdmin>> createCompanyAdminsList(List<CompanyAdmin> admins) {
		List<Resource<CompanyAdmin>> list = new ArrayList<Resource<CompanyAdmin>>();
		for(CompanyAdmin admin : admins)
			list.add(HATEOASImplementorUsers.createCompanyAdmin(admin));
		return list;
	}
	
	public static Resource<Location> createLocation(Location location) {
		Resource<Location> resource = new Resource<Location>(location);
		return resource;
	}
}
