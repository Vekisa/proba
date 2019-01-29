package com.isap.ISAProject.controller.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.isap.ISAProject.domain.json.request.AuthenticationRequest;
import com.isap.ISAProject.domain.json.response.AuthenticationResponse;
import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;
import com.isap.ISAProject.security.TokenUtils;
import com.isap.ISAProject.service.user.RegisteredUserService;
import com.isap.ISAProject.service.user.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users/registered")
public class RegisteredUserController {

	@Autowired
	private RegisteredUserService service;

	@Autowired
	private UserService userService;
	
	@Value("X-Auth-Token")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private UserDetailsService userDetailsService;

	@RequestMapping(method = RequestMethod.POST, value = "/auth")
	public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest/*,
			Device device*/) throws AuthenticationException {

		// Perform the authentication
		Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Reload password post-authentication so we can generate token
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String token = this.tokenUtils.generateToken(userDetails/*, device*/);

		// Return the token
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/logout")
	public ResponseEntity<?> logout(){
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/protected")
	// @PreAuthorize("hasRole('ADMIN')")
	@PreAuthorize("@securityService.hasProtectedAccess()")
	public ResponseEntity<?> getDaHoney() {
		return ResponseEntity.ok(":O");
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća registrovane korisnike.", notes = "Povratna vrednost servisa je lista registrovanih korisnika koji pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<RegisteredUser>>> getAllRegisteredUsers(Pageable pageable) {
			return new ResponseEntity<List<Resource<RegisteredUser>>>(HATEOASImplementorUsers.createRegisteredUserList(service.findAll(pageable)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća registrovanog korisnika sa ID.", notes = "Povratna vrednost servisa je resurs registrovanog korisnika koja ima traženi ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RegisteredUser.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa traženim ID ne postoji.")
	})
	public ResponseEntity<Resource<RegisteredUser>> getRegisteredUserById(@PathVariable("id") Long userId) {
			return new ResponseEntity<Resource<RegisteredUser>>(HATEOASImplementorUsers.createRegisteredUser(service.findById(userId)), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Memoriše i vraća registrovanog korisnika.", notes = "Povratna vrednost servisa je resurs registrovanog korisnika.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = RegisteredUser.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni korisnik nije validan.")
	})
	public ResponseEntity<Resource<RegisteredUser>> createRegisteredUser(@RequestBody @Valid RegisteredUser user) {
		return new ResponseEntity<Resource<RegisteredUser>>(HATEOASImplementorUsers.createRegisteredUser(userService.createRegisteredUser(user)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira registrovanog korisnika.", notes = "Ažurira registrovanog korisnika sa prosleđenim ID na osnovu prosleđenog korisnika. Kolekcije originalnog korisnika ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RegisteredUser.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili korisnik nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<RegisteredUser>> updateUserWithId(@PathVariable("id") Long userId, @Valid @RequestBody RegisteredUser newRegisteredUser) {
			return new ResponseEntity<Resource<RegisteredUser>>(HATEOASImplementorUsers.createRegisteredUser(service.updateUser(userId, newRegisteredUser)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/activeReservations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća aktivne rezervacije za datog korisnika.", notes = "Povratna vrednost servisa je lista aktivnih rezervacija (sve one koje se još nisu završile).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje aktivne rezervacije za datog korisnika."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Reservation>>> getReservationsForUserWithId(@PathVariable("id") Long userId) {
				return new ResponseEntity<List<Resource<Reservation>>>(HATEOASImplementorUsers.createReservationList(service.getActiveReservationsOfUser(userId)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/history", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća istoriju rezervacija za datog korisnika.", notes = "Povratna vrednost servisa je lista istorije rezervacija (sve one koje su se završile).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji istorija rezervacija za datog korisnika."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Reservation>>> getReservationsHistoryForUserWithId(@PathVariable("id") Long userId) {
				return new ResponseEntity<List<Resource<Reservation>>>(HATEOASImplementorUsers.createReservationList(service.getReservationHistoryOfUser(userId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/reservations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira rezervaciju za datog korisnika.", notes = "Povratna vrednost servisa je kreirana rezervacija.", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Reservation.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Reservation>> createReservationForUserWithId(@PathVariable("id") Long userId) {
			return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUsers.createReservation(service.createReservationForUser(userId)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/receivedRequests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća zahteve poslate datom korisniku.", notes = "Povratna vrednost servisa je lista zahteva koje je korisnik primio.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje zahtevi koji su poslati traženom korisniku."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FriendRequest>>> getReceivedFriendRequestsForUserWithId(@PathVariable("id") Long userId) {
				return new ResponseEntity<List<Resource<FriendRequest>>>(HATEOASImplementorUsers.createFriendRequestList(service.getReceivedFriendRequestsOfUser(userId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{from}/sendRequestTo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća zahtev za prijateljstvo koje korisnik šalje.", notes = "Povratna vrednost servisa je zahtev za prijateljstvo koje je traženi korisnik poslao zahtevanom korisniku.", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = FriendRequest.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<FriendRequest>> sendFriendRequest(@PathVariable("from") Long sendingUserId, @RequestParam("to") Long receivingUserId) {
			return new ResponseEntity<Resource<FriendRequest>>(HATEOASImplementorUsers.createFriendRequest(service.sendFriendRequest(sendingUserId, receivingUserId)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/sentRequests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća zahteve koje je poslao dati korisnik.", notes = "Povratna vrednost servisa je lista zahteva koje je korisnik poslao.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje zahtevi koje je traženi korisnik poslao."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<FriendRequest>>> getSentFriendRequestsForUserWithId(@PathVariable("id") Long userId) {
				return new ResponseEntity<List<Resource<FriendRequest>>>(HATEOASImplementorUsers.createFriendRequestList(service.getSentFriendRequestOfUser(userId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/friendships", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća prijateljstva datoga korisnika.", notes = "Povratna vrednost servisa je lista prijateljstava datog korisnika.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje prijateljstva traženog korisnika."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<Friendship>>> getFriendshipsForUserWithId(@PathVariable("id") Long userId) {
				return new ResponseEntity<List<Resource<Friendship>>>(HATEOASImplementorUsers.createFriendshipList(service.getFriendshipsOfUser(userId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{self}/acceptRequest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Prihvata zahtev za prijateljstvo.", notes = "Povratna vrednost servisa je prijateljstvo koje se kreira na osnovu prihvaćenog zahteva za prijateljstvo.", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Friendship.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Friendship>> acceptFriendRequest(@PathVariable(value = "self") Long receivingUserId, @RequestParam("from") Long sendingUserId) {
			return new ResponseEntity<Resource<Friendship>>(HATEOASImplementorUsers.createFriendship(service.acceptFriendRequest(receivingUserId, sendingUserId)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{self}/friends", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća sve prijatelje traženog korisnika.", notes = "Povratna vrednost servisa je lista resursa registrovanih korisnika sa kojima su u prijateljstvu.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Registrovani korisnik nema prijatelja."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<RegisteredUser>>> getFriendsOfUser(@PathVariable("self") Long id) {
		return new ResponseEntity<List<Resource<RegisteredUser>>>(HATEOASImplementorUsers.createRegisteredUserList(service.getFriends(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{self}/declineRequest", method = RequestMethod.DELETE)
	@ApiOperation(value = "Odbija zahtev za prijateljstvo.", notes = "Ovu operaciju izvršava korisnik kome je poslat zahtev za prijateljstvo.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> declineFriendRequest(@PathVariable("self") Long selfId, @RequestParam("friend") Long friendId) {
		service.declineFriendRequest(friendId, selfId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{self}/cancelRequest", method = RequestMethod.DELETE)
	@ApiOperation(value = "Otkazuje zahtev za prijateljstvo.", notes = "Ovu operaciju izvršava korisnik koji je poslao zahtev za prijateljstvo.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> cancelFriendRequest(@PathVariable("self") Long selfId, @RequestParam("friend") Long friendId) {
		service.declineFriendRequest(selfId, friendId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{self}/removeFriend", method = RequestMethod.DELETE)
	@ApiOperation(value = "Otkazuje prijateljstvo.", notes = "Ovu operaciju izvršava bilo koji od korisnika (prijatelja).", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Registrovani korisnik sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> removeFriend(@PathVariable("self") Long selfId, @RequestParam("friend") Long friendId) {
		service.removeFriend(selfId, friendId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{token}/confirm-account", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraca potvrdjenog korisnika.", notes = "Potvrdjuje korisnika.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RegisteredUser.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found")
	})
	public ModelAndView confirmAccountWithToken(@PathVariable("token") String userToken) {
		service.confirmAccount(userToken);
		return new ModelAndView("redirect:/signin.html");
	}
	
}
