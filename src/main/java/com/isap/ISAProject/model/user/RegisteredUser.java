package com.isap.ISAProject.model.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isap.ISAProject.model.airline.Passenger;

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
	@ManyToMany
	private List<Reservation> confirmedReservations;
	
	@JsonIgnore
	@ManyToMany
	private List<Reservation> pendingReservations;
	
	@JsonIgnore
	@ManyToMany
	private List<Reservation> ratedReservations;
	
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
	
	@JsonIgnore
	@OneToOne
	private ConfirmationToken confirmationToken;
	
	@JsonIgnore
	@OneToOne
	private Passenger passenger;
	
	public RegisteredUser() {}
	
	public List<Friendship> getFriendships() { return friendships; }

	public void setFriendships(List<Friendship> friendships) { this.friendships = friendships; }

	public int getBonusPoints() { return bonusPoints; }

	public void setBonusPoints(int bonusPoints) { this.bonusPoints = bonusPoints; }

	public List<FriendRequest> getSentRequests() { return sentRequests; }

	public void setSentRequests(List<FriendRequest> sentRequests) { this.sentRequests = sentRequests; }

	public List<FriendRequest> getReceivedRequests() { return receivedRequests; }

	public void setReceivedRequests(List<FriendRequest> receivedRequests) { this.receivedRequests = receivedRequests; }
	
	@Override
	public AuthorizationLevel getAuthority() { return this.authority; }

	@Override
	public void setAuthority(AuthorizationLevel authority) { this.authority = authority; }
	
	public ConfirmationToken getConfirmationToken() { return confirmationToken; }

	public void setConfirmationToken(ConfirmationToken confirmationToken) { this.confirmationToken = confirmationToken; }

	public List<Reservation> getConfirmedReservations() { return confirmedReservations; }

	public List<Reservation> getPendingReservations() { return pendingReservations; }

	public List<Reservation> getRatedReservations() { return ratedReservations; }

	public Passenger getPassenger() { return passenger; }

	public void setPassenger(Passenger passenger) { this.passenger = passenger; }

}
