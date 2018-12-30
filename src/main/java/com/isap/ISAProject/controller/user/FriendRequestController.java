package com.isap.ISAProject.controller.user;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.repository.user.FriendRequestRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/friendRequests")
public class FriendRequestController {
	
	@Autowired
	FriendRequestRepository friendRequestRepository;
	
	//Lista svih zahteva za prijateljstvo
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća zahteve za prijateljstvo.", notes = "Povratna vrednost metode je lista zahteva za prijateljstvo"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<FriendRequest>>> getAllFriendRequests(Pageable pageable){
		Page<FriendRequest> friendRequests = friendRequestRepository.findAll(pageable); 
		if(friendRequests.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return new ResponseEntity<List<Resource<FriendRequest>>>(HATEOASImplementorUser.createFriendRequestList(friendRequests.getContent()), HttpStatus.OK);
	}
	
	//Kreiranje zahtev za prijateljstvo
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše zahtev za prijateljstvo.", notes = "Povratna vrednost servisa je sačuvan zhtev za prijateljstvo.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = FriendRequest.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<FriendRequest>> createFriendRequest(@Valid @RequestBody FriendRequest friendRequest) {
		FriendRequest createdFriendRequest =  friendRequestRepository.save(friendRequest);
		return new ResponseEntity<Resource<FriendRequest>>(HATEOASImplementorUser.createFriendRequest(createdFriendRequest), HttpStatus.CREATED);
	}
	
	//Vraca zahtev za prijateljstvo sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća zahtev za priajteljstvo sa zadatim ID-em.", notes = "Povratna vrednost metode je zahtev za prijateljstvo "
			+ "koji ima zadati ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FriendRequest.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<FriendRequest>> getFriendRequestById(@PathVariable(value="id") Long friendRequestId) {
		Optional<FriendRequest> friendRequest = friendRequestRepository.findById(friendRequestId);
		if(friendRequest.isPresent())
			return new ResponseEntity<Resource<FriendRequest>>(HATEOASImplementorUser.createFriendRequest(friendRequest.get()), HttpStatus.OK);
		else
			return ResponseEntity.noContent().build();
	}
	
	//Brisanje zahteva a prijateljstvo sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše zahtev za prijateljstvo.", notes = "Briše zahtev za prijateljstvo sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteFriendRequestWithId(@PathVariable(value="id") Long friendRequestId){
		if(!friendRequestRepository.findById(friendRequestId).isPresent())
			return ResponseEntity.notFound().build();
		
		friendRequestRepository.deleteById(friendRequestId);
		return ResponseEntity.ok().build();
	}	

}
