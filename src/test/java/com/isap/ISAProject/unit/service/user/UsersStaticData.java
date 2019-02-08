package com.isap.ISAProject.unit.service.user;

import com.isap.ISAProject.model.user.AuthorizationLevel;
import com.isap.ISAProject.model.user.UserState;

public class UsersStaticData {

	public static final String COMPANY_ADMIN_1_EMAIL = "compadmin1@mail.com";
	public static final String COMPANY_ADMIN_1_USERNAME = "company_admin1";
	public static final String COMPANY_ADMIN_1_PASSWORD = "password1";
	public static final String COMPANY_ADMIN_1_FIRSTNAME = "Bepis";
	public static final String COMPANY_ADMIN_1_LASTNAME = "Pepsi";
	public static final String COMPANY_ADMIN_1_CITY = "Grad";	
	public static final String COMPANY_ADMIN_1_PHONE_NUMBER = "555-333";
	public static final UserState COMPANY_ADMIN_1_STATE = UserState.INACTIVE;
	public static final AuthorizationLevel COMPANY_ADMIN_1_AUTHORIZATION = AuthorizationLevel.HOTEL_ADMIN;
	public static final Long COMPANY_ADMIN_1_ID = 1L;
	
	public static final String USERS_ADMIN_1_EMAIL = "usersadmin1@mail.com";
	public static final String USERS_ADMIN_1_USERNAME = "USERS_admin1";
	public static final String USERS_ADMIN_1_PASSWORD = "password1";
	public static final String USERS_ADMIN_1_FIRSTNAME = "Admin";
	public static final String USERS_ADMIN_1_LASTNAME = "Adminovic";
	public static final String USERS_ADMIN_1_CITY = "Grad2";	
	public static final String USERS_ADMIN_1_PHONE_NUMBER = "222-333";
	public static final UserState USERS_ADMIN_1_STATE = UserState.INACTIVE;
	public static final AuthorizationLevel USERS_ADMIN_1_AUTHORIZATION = AuthorizationLevel.USERS_ADMIN;
	public static final Long USERS_ADMIN_1_ID = 3L;
	
	public static final String REGISTERED_1_EMAIL = "registeredusersadmin1@mail.com";
	public static final String REGISTERED_1_USERNAME = "reguser";
	public static final String REGISTERED_1_PASSWORD = "password3";
	public static final String REGISTERED_1_FIRSTNAME = "Korisnik";
	public static final String REGISTERED_1_LASTNAME = "Bla";
	public static final String REGISTERED_1_CITY = "Grad3";	
	public static final String REGISTERED_1_PHONE_NUMBER = "222-111";
	public static final UserState REGISTERED_1_STATE = UserState.INACTIVE;
	public static final AuthorizationLevel REGISTERED_1_AUTHORIZATION = AuthorizationLevel.REGULAR_USER;
	public static final Long REGISTERED_1_ID = 1L;	
	
	public static final String REGISTERED_2_EMAIL = "user2@mail.com";
	public static final String REGISTERED_2_USERNAME = "reguser2";
	public static final String REGISTERED_2_PASSWORD = "passwor43";
	public static final String REGISTERED_2_FIRSTNAME = "Korisnikx";
	public static final String REGISTERED_2_LASTNAME = "Blaaa";
	public static final String REGISTERED_2_CITY = "Graaad";	
	public static final String REGISTERED_2_PHONE_NUMBER = "222-222";
	public static final UserState REGISTERED_2_STATE = UserState.INACTIVE;
	public static final AuthorizationLevel REGISTERED_2_AUTHORIZATION = AuthorizationLevel.REGULAR_USER;
	public static final Long REGISTERED_2_ID = 2L;	
	
}