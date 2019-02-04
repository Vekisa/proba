package com.isap.ISAProject.serviceInterface.user;

import com.isap.ISAProject.domain.json.request.AuthenticationRequest;
import com.isap.ISAProject.domain.json.response.AuthenticationResponse;
import com.isap.ISAProject.domain.security.CerberusUser;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.UsersAdmin;

public interface UserServiceInterface {

	CompanyAdmin createCompanyAdmin(CompanyAdmin admin);
	
	UsersAdmin createUserAdmin(UsersAdmin admin);
	
	RegisteredUser createRegisteredUser(RegisteredUser user);
	
	AuthenticationResponse signin(AuthenticationRequest ar/*, Device d*/);
	
	void signout();
	
	CerberusUser currentUser();
}
