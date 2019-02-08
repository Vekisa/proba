package com.isap.ISAProject.controller.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.user.AuthorizationLevel;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.service.user.CompanyAdminService;
import com.isap.ISAProject.service.user.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users/companyAdmins")
public class CompanyAdminController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyAdminService service;
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Memoriše i vraća admina kompanije.", notes = "Povratna vrednost servisa je resurs registrovanog admina kompanije.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = CompanyAdmin.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni korisnik nije validan.")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN')")
	public ResponseEntity<Resource<CompanyAdmin>> createRegisteredUser(@RequestBody @Valid CompanyAdmin admin) {
		return new ResponseEntity<Resource<CompanyAdmin>>(HATEOASImplementorUsers.createCompanyAdmin(userService.createCompanyAdmin(admin)), HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća admine kompanija.", notes = "Povratna vrednost servisa je lista admina kompanija koji pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<CompanyAdmin>>> getAllCompanyAdmins(Pageable pageable) {
		return new ResponseEntity<List<Resource<CompanyAdmin>>>(HATEOASImplementorUsers.createCompanyAdminsList(service.findAll(pageable)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća admina kompanije sa ID.", notes = "Povratna vrednost servisa je admin kompanije koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = CompanyAdmin.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Admin kompanije sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<CompanyAdmin>> getCompanyAdminById(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<CompanyAdmin>>(HATEOASImplementorUsers.createCompanyAdmin(service.findById(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira admina kompanije.", notes = "Ažurira admina kompanije sa prosleđenim ID na osnovu prosleđenog admina kompanije.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = CompanyAdmin.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili admin kompanije nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Admin kompanije sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN') OR @securityServiceImpl.isCurrentAdmin(#id)")
	public ResponseEntity<Resource<CompanyAdmin>> updateAdminWithId(@PathVariable("id") Long id, @RequestBody @Valid CompanyAdmin admin) {
		return new ResponseEntity<Resource<CompanyAdmin>>(HATEOASImplementorUsers.createCompanyAdmin(service.updateAdmin(id, admin)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Briše admina kompanije.", notes = "Briše admina kompanije sa prosleđenim ID", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Admin kompanije sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN')")
	public ResponseEntity<?> deleteCompanyAdminWithId(@PathVariable("id") Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/authorization", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Postavlja autorizaciju za admina kompanije.", notes = "Dodeljuje ulogu adminu kompanije.", httpMethod = "PUT", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = CompanyAdmin.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili nivo autorizacije nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Admin kompanije sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN')")
	public ResponseEntity<Resource<CompanyAdmin>> setAuthorizationForAdminWithId(@PathVariable("id") Long id, @RequestParam("authorization") AuthorizationLevel authorization) {
		return new ResponseEntity<Resource<CompanyAdmin>>(HATEOASImplementorUsers.createCompanyAdmin(service.setAuthorization(id, authorization)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/company", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Dodeljuje kompaniju adminu kompanije.", notes = "Dodeljuje kompaniju adminu kompanije.", httpMethod = "PUT", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = CompanyAdmin.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Admin kompanije ili kompanija sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN')")
	public ResponseEntity<Resource<CompanyAdmin>> setCompanyForAdminWithId(@PathVariable("id") Long id, @RequestParam("company") Long companyId) {
		return new ResponseEntity<Resource<CompanyAdmin>>(HATEOASImplementorUsers.createCompanyAdmin(service.setCompany(id, companyId)), HttpStatus.OK);
	}
	
}
