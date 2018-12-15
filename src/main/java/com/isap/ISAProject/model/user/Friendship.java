package com.isap.ISAProject.model.user;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Friendship {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private Date friendsSince;
	private RegisteredUser friend1;
	private RegisteredUser friend2;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFriendsSince() {
		return friendsSince;
	}
	public void setFriendsSince(Date friendsSince) {
		this.friendsSince = friendsSince;
	}
	public RegisteredUser getFriend1() {
		return friend1;
	}
	public void setFriend1(RegisteredUser friend1) {
		this.friend1 = friend1;
	}
	public RegisteredUser getFriend2() {
		return friend2;
	}
	public void setFriend2(RegisteredUser friend2) {
		this.friend2 = friend2;
	}

}
