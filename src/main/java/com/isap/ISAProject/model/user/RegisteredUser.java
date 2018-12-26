package com.isap.ISAProject.model.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

@Entity
@Table(name = "registered_user")
public class RegisteredUser extends SystemUser {

	@Column
	private int bonusPoints;
	
	@OneToMany(mappedBy="registeredUser")
	private List<Reservation> history;
	
	@OneToMany(mappedBy="sender")
	private List<FriendRequest> sentRequests;
	
	@OneToMany(mappedBy="receiver")
	private List<FriendRequest> receivedRequests;
	
	@OneToMany(mappedBy="friend")
	private List<Friendship> friendships;

	@OneToOne
	private RegisteredUser friend;
	
	public List<Friendship> getFriendships() {
		return friendships;
	}

	public void setFriendships(List<Friendship> friendships) {
		this.friendships = friendships;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public List<Reservation> getHistory() {
		return history;
	}

	public void setHistory(List<Reservation> history) {
		this.history = history;
	}

	public List<FriendRequest> getSentRequests() {
		return sentRequests;
	}

	public void setSentRequests(List<FriendRequest> sentRequests) {
		this.sentRequests = sentRequests;
	}

	public List<FriendRequest> getReceivedRequests() {
		return receivedRequests;
	}

	public void setReceivedRequests(List<FriendRequest> receivedRequests) {
		this.receivedRequests = receivedRequests;
	}
	
	public void copyFieldsFrom(@Valid RegisteredUser newRegisteredUser) {
		this.setBonusPoints(newRegisteredUser.getBonusPoints());
		this.setCity(newRegisteredUser.getCity());
		this.setEmail(newRegisteredUser.getEmail());
		this.setFirstName(newRegisteredUser.getFirstName());
		this.setLastName(newRegisteredUser.getLastName());
		this.setPhoneNumber(newRegisteredUser.getPhoneNumber());
		this.setPassword(newRegisteredUser.getPassword());
	}
	
	public void add(@Valid Reservation reservation) {
		this.getHistory().add(reservation);
		reservation.setRegisteredUser(this);
	}
	
	public void addReceived(@Valid FriendRequest request) {
		this.getReceivedRequests().add(request);
		request.setReceiver(this);
	}
	
	public void addSent(@Valid FriendRequest request) {
		this.getSentRequests().add(request);
		request.setSender(this);
	}
	
	public void add(@Valid Friendship friendship) {
		this.getFriendships().add(friendship);
		friendship.setFriend(this);
	}
}
