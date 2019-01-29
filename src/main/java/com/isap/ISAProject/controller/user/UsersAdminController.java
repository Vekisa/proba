package com.isap.ISAProject.controller.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.user.UsersAdmin;
import com.isap.ISAProject.service.user.UserService;
import com.isap.ISAProject.service.user.UsersAdminService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users/usersAdmins")
public class UsersAdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UsersAdminService service;
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Memoriše i vraća admina korisnika.", notes = "Povratna vrednost servisa je resurs registrovanog admina korisnika.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = UsersAdmin.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni korisnik nije validan.")
	})
	public ResponseEntity<Resource<UsersAdmin>> createRegisteredUser(@RequestBody @Valid UsersAdmin admin) {
		return new ResponseEntity<Resource<UsersAdmin>>(HATEOASImplementorUsers.createUsersAdmin(userService.createUserAdmin(admin)), HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća admine korisnika.", notes = "Povratna vrednost servisa je lista admina korisnika koji pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<UsersAdmin>>> getAllUserAdmins(Pageable pageable) {
		return new ResponseEntity<List<Resource<UsersAdmin>>>(HATEOASImplementorUsers.createUsersAdminList(service.findAll(pageable)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća admina korisnika sa ID.", notes = "Povratna vrednost servisa je admin korisnika koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = UsersAdmin.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Admin korisnika sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<UsersAdmin>> getUsersAdminById(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<UsersAdmin>>(HATEOASImplementorUsers.createUsersAdmin(service.findById(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše admina korisnika.", notes = "Briše admina korisnika sa prosleđenim ID", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Admin korisnika sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteUsersAdminWithId(@PathVariable("id") Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira admina korisnika.", notes = "Ažurira admina korisnika sa prosleđenim ID na osnovu prosleđenog admina korisnika.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = UsersAdmin.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili admin korisnika nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Admin korisnika sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<UsersAdmin>> updateUsersAdminWithId(@PathVariable("id") Long id, @RequestBody @Valid UsersAdmin admin) {
		return new ResponseEntity<Resource<UsersAdmin>>(HATEOASImplementorUsers.createUsersAdmin(service.update(id, admin)), HttpStatus.OK);
	}
	
}
