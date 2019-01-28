package com.isap.ISAProject.controller.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.user.UsersAdmin;
import com.isap.ISAProject.service.user.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users/usersAdmins")
public class UsersAdminController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Memoriše i vraća admina korisnika.", notes = "Povratna vrednost servisa je resurs registrovanog admina korisnika.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = UsersAdmin.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni korisnik nije validan.")
	})
	public ResponseEntity<Resource<UsersAdmin>> createRegisteredUser(@RequestBody @Valid UsersAdmin admin) {
		return new ResponseEntity<Resource<UsersAdmin>>(HATEOASImplementor.createUsersAdmin(userService.createUserAdmin(admin)), HttpStatus.CREATED);
	}
	
}
