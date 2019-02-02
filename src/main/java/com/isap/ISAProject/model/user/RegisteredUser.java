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
import com.isap.ISAProject.model.rating.AirlineRating;
import com.isap.ISAProject.model.rating.FlightRating;
import com.isap.ISAProject.model.rating.HotelRating;
import com.isap.ISAProject.model.rating.RentACarRating;
import com.isap.ISAProject.model.rating.RoomRating;
import com.isap.ISAProject.model.rating.VehicleRating;

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
	
	@JsonIgnore
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy = "user")
	private List<AirlineRating> airlineRatings;
	
	@JsonIgnore
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy = "user")
	private List<FlightRating> flightRatings;
	
	@JsonIgnore
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy = "user")
	private List<HotelRating> hotelRatings;
	
	@JsonIgnore
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy = "user")
	private List<RoomRating> roomRatings;
	
	@JsonIgnore
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy = "user")
	private List<RentACarRating> rentACarRatings;
	
	@JsonIgnore
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy = "user")
	private List<VehicleRating> vehicleRatings;
	
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

	public Passenger getPassenger() { return passenger; }

	public void setPassenger(Passenger passenger) { this.passenger = passenger; }

	public List<AirlineRating> getAirlineRatings() { return airlineRatings; }

	public List<FlightRating> getFlightRatings() { return flightRatings; }

	public List<HotelRating> getHotelRatings() { return hotelRatings; }

	public List<RoomRating> getRoomRatings() { return roomRatings; }

	public List<RentACarRating> getRentACarRatings() { return rentACarRatings; }

	public List<VehicleRating> getVehicleRatings() { return vehicleRatings; }

}
