package com.isap.ISAProject.serviceInterface.user;

import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.UsersAdmin;

public interface UserServiceInterface {

	CompanyAdmin createCompanyAdmin(CompanyAdmin admin);
	
	UsersAdmin createUserAdmin(UsersAdmin admin);
	
	RegisteredUser createRegisteredUser(RegisteredUser user);
	
}
