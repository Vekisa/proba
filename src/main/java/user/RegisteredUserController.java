package user;

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
import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import repository.user.RegisteredUserRepository;

@RestController
@RequestMapping("/registeredUsers")
public class RegisteredUserController {
	
	@Autowired
	RegisteredUserRepository registeredUserRepository;
	
	//Lista svih registrovanih usera
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća registrovane usere.", notes = "Povratna vrednost metode je lista registrovanih usera"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<RegisteredUser>>> getAllRegisteredUsers(Pageable pageable){
		Page<RegisteredUser> registeredUsers = registeredUserRepository.findAll(pageable); 
		if(registeredUsers.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return new ResponseEntity<List<Resource<RegisteredUser>>>(HATEOASImplementorUser.createRegisteredUserList(registeredUsers.getContent()), HttpStatus.OK);
	}
	
	//Kreiranje usera
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše usera.", notes = "Povratna vrednost servisa je sačuvan user.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = RegisteredUser.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<RegisteredUser>> createUser(@Valid @RequestBody RegisteredUser registeredUser) {
		
		//OVDE TREBA UBACITI NEKE PROVERE, ITD...
		RegisteredUser createdRegisteredUser =  registeredUserRepository.save(registeredUser);
		return new ResponseEntity<Resource<RegisteredUser>>(HATEOASImplementorUser.createRegisteredUser(createdRegisteredUser), HttpStatus.CREATED);
	}
	
	//Vraca user-a sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća usera sa zadatim ID-em.", notes = "Povratna vrednost metode je user koji ima zadati ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RegisteredUser.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<RegisteredUser>> getRegisteredUserById(@PathVariable(value="id") Long userId) {
		Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(userId);
		if(registeredUser.isPresent())
			return new ResponseEntity<Resource<RegisteredUser>>(HATEOASImplementorUser.createRegisteredUser(registeredUser.get()), HttpStatus.OK);
		else
			return ResponseEntity.noContent().build();
	}
	
	//Brisanje usera sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše usera.", notes = "Briše usera sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteRegisteredUserWithId(@PathVariable(value="id") Long userId){
		//OVDE RAZMOTRITI DA LI TREBA LOGICKO ILI FIZICKO BRISANJE
		if(!registeredUserRepository.findById(userId).isPresent())
			return ResponseEntity.notFound().build();
		
		registeredUserRepository.deleteById(userId);
		return ResponseEntity.ok().build();
	}	

	//Update usera sa zadatim id-em
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update hotela.", notes = "Ažurira usera sa zadatim ID-em na osnovu prosleđenog usera. Kolekcije originalnog usera ostaju netaknute.",
			httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RegisteredUser.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<RegisteredUser>> updateUserWithId(@PathVariable(value = "id") Long userId,
			@Valid @RequestBody RegisteredUser newRegisteredUser) {
		Optional<RegisteredUser> oldRegisteredUser = registeredUserRepository.findById(userId);
		if(oldRegisteredUser.isPresent()) {
			oldRegisteredUser.get().copyFieldsFrom(newRegisteredUser);
			registeredUserRepository.save(oldRegisteredUser.get());
			return new ResponseEntity<Resource<RegisteredUser>>(HATEOASImplementorUser.createRegisteredUser(oldRegisteredUser.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Vraca rezervacije datog usera
	@RequestMapping(value = "/{id}/reservations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća rezervacije prosledjenog usera.", notes = "Povratna vrednost servisa je lista rezervacija.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<Reservation>>> getReservationsForUserWithId(@PathVariable(value = "id") Long userId) {
		Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(userId);
		if(registeredUser.isPresent()) {
			List<Reservation> reservationList = registeredUser.get().getHistory();
			if(reservationList.isEmpty())
				return ResponseEntity.noContent().build();
			else
				return new ResponseEntity<List<Resource<Reservation>>>(HATEOASImplementorUser.createReservationList(reservationList), HttpStatus.OK);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Kreira rezervaciju za usera
	@RequestMapping(value = "/{id}/reservations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira rezervaciju za zadatog usera", notes = "Povratna vrednost metode je kreirana rezervacija.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Reservation.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Reservation>> createReservationForUserWithId(@PathVariable(value = "id") Long userId,
			@Valid @RequestBody Reservation reservation) {
		Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(userId);
		if(registeredUser.isPresent()) {
			registeredUser.get().add(reservation);
			registeredUserRepository.save(registeredUser.get());
			return new ResponseEntity<Resource<Reservation>>(HATEOASImplementorUser.createReservation(reservation), HttpStatus.CREATED);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Vraca primljene zahteve prijateljstva datog usera
	@RequestMapping(value = "/{id}/receivedRequests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća primljene zahteve prijateljstva prosledjenog usera.", notes = "Povratna vrednost servisa je lista zahteva.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<FriendRequest>>> getReceivedFriendRequestsForUserWithId(@PathVariable(value = "id") Long userId) {
		Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(userId);
		if(registeredUser.isPresent()) {
			List<FriendRequest> requestsList = registeredUser.get().getReceivedRequests();
			if(requestsList.isEmpty())
				return ResponseEntity.noContent().build();
			else
				return new ResponseEntity<List<Resource<FriendRequest>>>(HATEOASImplementorUser.createFriendRequestList(requestsList), HttpStatus.OK);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Kreira primljeni zahtev za prijateljstvo za usera
	@RequestMapping(value = "/{id}/receivedRequests", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira zahtev prijateljstva za zadatog usera", notes = "Povratna vrednost metode je kreiran zahtev prijateljstva.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FriendRequest.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<FriendRequest>> createReceivedFriendRequestForUserWithId(@PathVariable(value = "id") Long userId,
			@Valid @RequestBody FriendRequest friendRequest) {
		Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(userId);
		if(registeredUser.isPresent()) {
			registeredUser.get().addReceived(friendRequest);
			registeredUserRepository.save(registeredUser.get());
			return new ResponseEntity<Resource<FriendRequest>>(HATEOASImplementorUser.createFriendRequest(friendRequest), HttpStatus.CREATED);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Vraca poslate zahteve prijateljstva datog usera
	@RequestMapping(value = "/{id}/sentRequests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća poslate zahteve prijateljstva prosledjenog usera.", notes = "Povratna vrednost servisa je lista zahteva.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<FriendRequest>>> getSentFriendRequestsForUserWithId(@PathVariable(value = "id") Long userId) {
		Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(userId);
		if(registeredUser.isPresent()) {
			List<FriendRequest> requestsList = registeredUser.get().getSentRequests();
			if(requestsList.isEmpty())
				return ResponseEntity.noContent().build();
			else
				return new ResponseEntity<List<Resource<FriendRequest>>>(HATEOASImplementorUser.createFriendRequestList(requestsList), HttpStatus.OK);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Kreira poslati zahtev za prijateljstvo za usera
	@RequestMapping(value = "/{id}/sentRequests", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira poslat zahtev prijateljstva za zadatog usera", notes = "Povratna vrednost metode je kreiran zahtev prijateljstva.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = FriendRequest.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<FriendRequest>> createSentFriendRequestForUserWithId(@PathVariable(value = "id") Long userId,
			@Valid @RequestBody FriendRequest friendRequest) {
		Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(userId);
		if(registeredUser.isPresent()) {
			registeredUser.get().addSent(friendRequest);
			registeredUserRepository.save(registeredUser.get());
			return new ResponseEntity<Resource<FriendRequest>>(HATEOASImplementorUser.createFriendRequest(friendRequest), HttpStatus.CREATED);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Vraca prijateljstva datog usera
	@RequestMapping(value = "/{id}/friendships", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća prijateljstva prosledjenog usera.", notes = "Povratna vrednost servisa je lista prijateljstava.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<Friendship>>> getFriendshipsForUserWithId(@PathVariable(value = "id") Long userId) {
		Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(userId);
		if(registeredUser.isPresent()) {
			List<Friendship> friendshipList = registeredUser.get().getFriendships();
			if(friendshipList.isEmpty())
				return ResponseEntity.noContent().build();
			else
				return new ResponseEntity<List<Resource<Friendship>>>(HATEOASImplementorUser.createFriendshipList(friendshipList), HttpStatus.OK);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Kreira prijateljstvo za usera
	@RequestMapping(value = "/{id}/friendships", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira prijateljstvo za zadatog usera", notes = "Povratna vrednost metode je kreirano prijateljstvo.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Friendship.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Friendship>> createFriendshipForUserWithId(@PathVariable(value = "id") Long userId,
			@Valid @RequestBody Friendship friendship) {
		Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(userId);
		if(registeredUser.isPresent()) {
			registeredUser.get().add(friendship);
			registeredUserRepository.save(registeredUser.get());
			return new ResponseEntity<Resource<Friendship>>(HATEOASImplementorUser.createFriendship(friendship), HttpStatus.CREATED);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
