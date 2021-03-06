package com.isap.ISAProject.controller.hotel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.isap.ISAProject.controller.airline.HATEOASImplementorAirline;
import com.isap.ISAProject.controller.user.HATEOASImplementorUsers;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.service.hotel.HotelService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/hotels")
public class HotelController {
	
	@Autowired
	private HotelService hotelService;
	
	//Lista svih hotela
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća hotele.", notes = "Povratna vrednost metode je lista hotela"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<Hotel>>> getAllHotels(Pageable pageable){
			return new ResponseEntity<List<Resource<Hotel>>>(HATEOASImplementorHotel.createHotelsList(hotelService.findAll(pageable)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pretraga rent-a-car servisa", responseContainer = "List", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Hotel>>> search(Pageable pageable, 
			@RequestParam(value="locationName", required=false) String locationName,
			@RequestParam(value="name", required=false) String name,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value="beginDate", required=false) Date begin, 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value="endDate", required=false) Date end){
		List<Hotel> ret = hotelService.search(pageable, locationName, name, begin, end);
		return new ResponseEntity<List<Resource<Hotel>>>(HATEOASImplementorHotel.createHotelsList(ret), HttpStatus.OK);
	}
	
	//Kreiranje hotela
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše hotel.", notes = "Povratna vrednost servisa je sačuvan hotel.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN')")
	public ResponseEntity<Resource<Hotel>> createHotel(@Valid @RequestBody Hotel hotel, @RequestParam("location") Long id) {
		return new ResponseEntity<Resource<Hotel>>(HATEOASImplementorHotel.createHotel(hotelService.saveWithLocation(hotel, id)), HttpStatus.CREATED);
	}
	
	//Vraca hotel sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća hotel sa zadatim ID-em.", notes = "Povratna vrednost metode je hotel koji ima zadati ID.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Hotel>> getHotelById(@PathVariable(value="id") Long hotelId) {
			return new ResponseEntity<Resource<Hotel>>(HATEOASImplementorHotel.createHotel(hotelService.findById(hotelId)), HttpStatus.OK);
	}
	
	//Brisanje hotela sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše hotel.", notes = "Briše hotel sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('USERS_ADMIN')")
	public ResponseEntity<?> deleteHotelWithId(@PathVariable(value="id") Long hotelId){
		hotelService.deleteById(hotelId);
		return ResponseEntity.ok().build();
	}	
	
	//Update hotela sa zadatim id-em
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update hotela.", notes = "Ažurira hotel sa zadatim ID-em na osnovu prosleđenog hotela. Kolekcije originalnog hotela ostaju netaknute.",
			httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToHotel(#hotelId)")
	public ResponseEntity<Resource<Hotel>> updateHotelWithId(@PathVariable(value = "id") Long hotelId,
			@Valid @RequestBody Hotel newHotel) {
				return new ResponseEntity<Resource<Hotel>>(HATEOASImplementorHotel.createHotel(hotelService.updateHotelById(hotelId, newHotel)), HttpStatus.OK);
		}
	
	//Ocenjivanje hotela sa zadatim id-em
	@RequestMapping(value = "/{id}/add-rating", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Dodaje ocenu hotela", notes = "Povratna vrednost metode je hotel sa dodatom ocenom.", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Hotel>> addRatingToHotelWithId(@PathVariable(value = "id") Long hotelId, @RequestParam("rating") int rating){
		//razmotriti
		return null;
	}
	
	//Vraca spratove za dati hotel
	@RequestMapping(value = "/{id}/floors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća spatove za dati hotel.", notes = "Povratna vrednost servisa je lista spratova.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<Floor>>> getFloorsForHotelWithId(@PathVariable(value = "id") Long hotelId) {
				return new ResponseEntity<List<Resource<Floor>>>(HATEOASImplementorHotel.createFloorList(hotelService.getFloors(hotelId)), HttpStatus.OK);
	}
	
	//Kreira sprat u hotelu
	@RequestMapping(value = "/{id}/floors", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira sprat u okviru zadatog hotela", notes = "Povratna vrednost metode je kreiran sprat.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Floor.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToHotel(#hotelId)")
	public ResponseEntity<Resource<Floor>> createFloorForHotelWithId(@PathVariable(value = "id") Long hotelId,
			@Valid @RequestBody Floor floor) {
			return new ResponseEntity<Resource<Floor>>(HATEOASImplementorHotel.createFloor(hotelService.createFloor(hotelId, floor)), HttpStatus.CREATED);
	}
	
	//Vraca sve extra option-e u okviru hotela
	@RequestMapping(value = "/{id}/extra-options", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća extra-options za dati hotel.", notes = "Povratna vrednost servisa je lista extra option-a.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = ExtraOption.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<ExtraOption>>> getExtraOptionsForHotelWithId(@PathVariable(value = "id") Long hotelId) {
				return new ResponseEntity<List<Resource<ExtraOption>>>(HATEOASImplementorHotel.createExtraOptionList(hotelService.getExtraOptions(hotelId)), HttpStatus.OK);
	}
	
	//Kreira extra option u hotelu
	@RequestMapping(value = "/{id}/extra-options", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira sprat u okviru zadatog hotela", notes = "Povratna vrednost metode je kreiran sprat.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = ExtraOption.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToHotel(#hotelId)")
	public ResponseEntity<Resource<ExtraOption>> createExtraOptionForHotelWithId(@PathVariable(value = "id") Long hotelId,
			@Valid @RequestBody ExtraOption extraOption) {
			return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(hotelService.createExtraOption(hotelId, extraOption)), HttpStatus.CREATED);
	}
	
	//Vraca cenovnik u okviru hotela
	@RequestMapping(value = "/{id}/catalogue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća cenovnik za dati hotel.", notes = "Povratna vrednost servisa je cenovnik",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Catalogue.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Catalogue>> getCatalogueForHotelWithId(@PathVariable(value = "id") Long hotelId) {
				return new ResponseEntity<Resource<Catalogue>>(HATEOASImplementorHotel.createCatalogue(hotelService.getCatalogue(hotelId)), HttpStatus.OK);
	}
	
	//Kreira cenovnik u hotelu
	@RequestMapping(value = "/{id}/catalogue", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira cenovnik u okviru zadatog hotela", notes = "Povratna vrednost metode je kreiran cenovnik.",
			httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToHotel(#hotelId)")
	public ResponseEntity<Resource<Catalogue>> setCatalogueForHotelWithId(@PathVariable(value = "id") Long hotelId, @RequestParam("catalogue") Long catalogueId) {
			return new ResponseEntity<Resource<Catalogue>>(HATEOASImplementorHotel.createCatalogue(hotelService.createCatalogue(hotelId, catalogueId)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/location", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira lokaciju zadatog hotela.", notes = "Ažurira lokaciju hotela sa prosleđenim ID na osnovu parametra zahteva.", httpMethod = "PUT", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Hotel ili destinacija sa prosleđenim ID ne postoji.")
	})
	@PreAuthorize("hasAuthority('HOTEL_ADMIN') AND @securityServiceImpl.hasAccessToHotel(#hotelId)")
	public ResponseEntity<Resource<Hotel>> changeLocationOfHotel(@PathVariable("id") Long hotelId, @RequestParam("destination") Long id) {
		return new ResponseEntity<Resource<Hotel>>(HATEOASImplementorHotel.createHotel(hotelService.changeLocationOfHotel(hotelId, id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/location", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća lokaciju hotela.", notes = "Povratna vrednost servisa je resurs lokacija hotela.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Location.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoji lokacija za dati hotel."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Hotel sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<Location>> getLocationOfHotel(@PathVariable("id") Long id) {
		return new ResponseEntity<Resource<Location>>(HATEOASImplementorAirline.createDestination(hotelService.getLocationOfHotel(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/admins", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća admine za datu avio kompaniju.", notes = "Povratna vrednost servisa je lista resursa admina kompanije.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje admini za datu avio kompaniju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Avio kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<CompanyAdmin>>> getAdminsOfHotel(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<CompanyAdmin>>>(HATEOASImplementorUsers.createCompanyAdminsList(hotelService.getAdminsOfHotel(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/rooms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<Room>>> getRoomsOfHotel(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<Room>>>(HATEOASImplementorHotel.createRoomList(hotelService.getRooms(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/roomtypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<RoomType>>> getRoomType(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<RoomType>>>(HATEOASImplementorHotel.createRoomTypeList(hotelService.getRoomTypes(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/income", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<Long, Double>> getIncomeForHotel(@PathVariable("id") Long id, @RequestParam("begin") Long begin, @RequestParam("end") Long end) {
		return new ResponseEntity<Map<Long,Double>>(hotelService.getIncomeFor(id, new Date(begin), new Date(end)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/statistic", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<Long, Integer>> getStatisticForHotel(@PathVariable("id") Long id, @RequestParam("begin") Long begin, @RequestParam("end") Long end) {
		return new ResponseEntity<Map<Long,Integer>>(hotelService.getStatisticFor(id, new Date(begin), new Date(end)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/quicks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<RoomReservation>>> getQuickRoomReservationsForHotel(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<RoomReservation>>>(HATEOASImplementorHotel.createRoomReservationList(hotelService.getQuickRoomReservations(id)), HttpStatus.OK);
	}
	
}
