package com.isap.ISAProject.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.service.user.FriendshipService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/friendships")
public class FriendshipController {

	@Autowired
	private FriendshipService service;

	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sva prijateljstva.", notes = "Povratna vrednost servisa je lista prijateljstava koji pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Friendship>>> getFriendships(Pageable pageable){
		return new ResponseEntity<List<Resource<Friendship>>>(HATEOASImplementorUsers.createFriendshipList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća prijateljstvo sa ID.", notes = "Povratna vrednost servisa je resurs prijateljstva koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FriendRequest.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Prijateljstvo sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<Friendship>> getFriendshipById(@PathVariable(value="id") Long friendshipId) {
		return new ResponseEntity<Resource<Friendship>>(HATEOASImplementorUsers.createFriendship(service.findById(friendshipId)), HttpStatus.OK);
	}

}
