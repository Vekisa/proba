package com.isap.ISAProject.model.user;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.isap.ISAProject.model.user.base.UserBase;

@MappedSuperclass
public abstract class SystemUser extends UserBase{
	
	private static final long serialVersionUID = 689204864806648978L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Version
	private Long version;
	
	@Column(nullable = false, unique = true, length = 30)
	private String email;
	
	@Column(nullable = false, unique = true, length = 30)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private UserState state;
	
	public SystemUser() {
		super();
	}
	
	public SystemUser(String email, String username, String password, String firstName, String lastName, String city,
			String phoneNumber) {
		super();
		this.email = email;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
		this.phoneNumber = phoneNumber;
	}

	public SystemUser(SystemUser user) {
		this.email = user.getEmail();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.city = user.getCity();
		this.phoneNumber = user.getPhoneNumber();
	}

	public Long getId() { return id; }

	public String getEmail() { return email; }

	public String getPassword() { return password; }
	
	public void setPassword(String password) { this.password = password; }
	
	public String getFirstName() { return firstName; }
	
	public void setFirstName(String firstName) { this.firstName = firstName; }
	
	public String getLastName() { return lastName; }
	
	public void setLastName(String lastName) { this.lastName = lastName; }
	
	public String getCity() { return city; }
	
	public void setCity(String city) { this.city = city; }
	
	public String getPhoneNumber() { return phoneNumber; }
	
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	
	public String getUsername() { return username; }
	
	public void setUsername(String username) { this.username = username; }
	
	public abstract AuthorizationLevel getAuthority();
	
	public abstract void setAuthority(AuthorizationLevel authority);

	public UserState getState() { return state; }

	public void setState(UserState state) { this.state = state; }
	
}
