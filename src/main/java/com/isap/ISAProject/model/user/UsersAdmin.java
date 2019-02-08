package com.isap.ISAProject.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_admin")
public class UsersAdmin extends SystemUser {

	private static final long serialVersionUID = 2980358171810520586L;

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private AuthorizationLevel authority;
	
	public UsersAdmin() {}
	
	public UsersAdmin(String email, String username, String password, String firstName, String lastName, String city,
			String phoneNumber) {
		super(email, username, password, firstName, lastName, city, phoneNumber);
		this.setAuthority(AuthorizationLevel.USERS_ADMIN);
	}

	@Override
	public AuthorizationLevel getAuthority() { return this.authority; }

	@Override
	public void setAuthority(AuthorizationLevel authority) { this.authority = authority; }
	
}
