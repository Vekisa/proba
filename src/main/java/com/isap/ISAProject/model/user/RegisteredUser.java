package com.isap.ISAProject.model.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "registered_user")
public class RegisteredUser extends SystemUser {
	
	private static final long serialVersionUID = 6745667384494012132L;

	@Column
	private int bonusPoints;
	
	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private AuthorizationLevel authority;
	
	@JsonIgnore
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy="registeredUser")
	private List<Reservation> reservations;
	
	@JsonIgnore
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy="sender", orphanRemoval = true)
	private List<FriendRequest> sentRequests;
	
	@JsonIgnore
	@OneToMany(mappedBy="receiver")
	private List<FriendRequest> receivedRequests;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "friends")
	private List<Friendship> friendships;
	
	public RegisteredUser() {}
	
	public List<Friendship> getFriendships() { return friendships; }

	public void setFriendships(List<Friendship> friendships) { this.friendships = friendships; }

	public int getBonusPoints() { return bonusPoints; }

	public void setBonusPoints(int bonusPoints) { this.bonusPoints = bonusPoints; }

	public List<Reservation> getReservations() { return reservations; }

	public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }

	public List<FriendRequest> getSentRequests() { return sentRequests; }

	public void setSentRequests(List<FriendRequest> sentRequests) { this.sentRequests = sentRequests; }

	public List<FriendRequest> getReceivedRequests() { return receivedRequests; }

	public void setReceivedRequests(List<FriendRequest> receivedRequests) { this.receivedRequests = receivedRequests; }
	
	@Override
	public AuthorizationLevel getAuthority() { return this.authority; }

	@Override
	public void setAuthority(AuthorizationLevel authority) { this.authority = authority; }
	
	public void add(@Valid Reservation reservation) {
		this.getReservations().add(reservation);
		reservation.setRegisteredUser(this);
	}
}
