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

import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.repository.user.FriendshipRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/friendships")
public class FriendshipController {

	@Autowired
	FriendshipRepository friendshipRepository;
	
	//Lista svih prijateljstava
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sva prijateljstva.", notes = "Povratna vrednost metode je lista prijateljstava"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<Friendship>>> getFriendships(Pageable pageable){
		Page<Friendship> friendships = friendshipRepository.findAll(pageable); 
		if(friendships.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return new ResponseEntity<List<Resource<Friendship>>>(HATEOASImplementorUser.createFriendshipList(friendships.getContent()), HttpStatus.OK);
	}
	
	//Kreiranje rezervacije
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše prijateljstvo.", notes = "Povratna vrednost servisa je sačuvano prijateljstvo.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = Friendship.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Friendship>> createFriendship(@Valid @RequestBody Friendship friendship) {
		Friendship createdFriendship =  friendshipRepository.save(friendship);
		return new ResponseEntity<Resource<Friendship>>(HATEOASImplementorUser.createFriendship(createdFriendship), HttpStatus.CREATED);
	}
	
	//Vraca prijateljstvo sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća prijateljstvo sa zadatim ID-em.", notes = "Povratna vrednost metode je prijateljstvo"
			+ "koji ima zadati ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Friendship.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Friendship>> getFriendshipById(@PathVariable(value="id") Long friendshipId) {
		Optional<Friendship> friendship = friendshipRepository.findById(friendshipId);
		if(friendship.isPresent())
			return new ResponseEntity<Resource<Friendship>>(HATEOASImplementorUser.createFriendship(friendship.get()), HttpStatus.OK);
		else
			return ResponseEntity.noContent().build();
	}
	
	//Brisanje prijateljstva sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše prijateljstvo.", notes = "Briše prijateljstvo sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteFriendshipWithId(@PathVariable(value="id") Long friendshipId){
		if(!friendshipRepository.findById(friendshipId).isPresent())
			return ResponseEntity.notFound().build();
		
		friendshipRepository.deleteById(friendshipId);
		return ResponseEntity.ok().build();
	}	
}
