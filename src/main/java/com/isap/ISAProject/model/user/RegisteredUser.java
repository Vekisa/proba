package com.isap.ISAProject.model.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredUser extends SystemUser{

	
	@Column(nullable = true)
	private Long bonusPoints;
	
	public RegisteredUser() {}
	
	private RegisteredUser(Builder builder) {
    	setId(builder.id);
    	setUsername(builder.username);
    	setPassword(builder.password);
    	setRoles(builder.roles);
    	setEmail(builder.email);
    	setFirstName(builder.firstName);
    	setLastName(builder.lastName);
    	setCity(builder.city);
    	setPhoneNumber(builder.phoneNumber);
    	setBonusPoints(builder.bonusPoints);
    }
	
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
	
	public RegisteredUser getFriend() {
		return friend;
	}

	public void setFriend(RegisteredUser friend) {
		this.friend = friend;
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
	
	public void copyFieldsFrom(@Valid RegisteredUser newRegisteredUser) {
		this.setBonusPoints(newRegisteredUser.getBonusPoints());
		this.setCity(newRegisteredUser.getCity());
		this.setEmail(newRegisteredUser.getEmail());
		this.setFirstName(newRegisteredUser.getFirstName());
		this.setLastName(newRegisteredUser.getLastName());
		this.setPhoneNumber(newRegisteredUser.getPhoneNumber());
		this.setPassword(newRegisteredUser.getPassword());
	}
	
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder{
		private Long id;
		private String username;
		private String password;
		private List<String> roles;
		private String email;
		private String firstName;
		private String lastName;
		private String city;
		private String phoneNumber;
		private Long bonusPoints;
		
		public Builder id(Long id) {
			this.id = id;
			return this;
		}
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		public Builder roles(List<String> roles) {
			this.roles = roles;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public Builder city(String city) {
			this.city = city;
			return this;
		}
		
		public Builder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}
		
		public Builder bonusPoints(Long bonus) {
			this.bonusPoints = bonus;
			return this;
		}
		
		public RegisteredUser build() {
			return new RegisteredUser(this);
		}
	}
	
	public Long getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(Long bonusPoints) {
		this.bonusPoints = bonusPoints;
	}
}
