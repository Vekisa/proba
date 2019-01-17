package com.isap.ISAProject.controller.hotel;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import service.hotel.HotelService;

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
		Page<Hotel> hotels = hotelService.findAll(pageable); 
		if(hotels.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return new ResponseEntity<List<Resource<Hotel>>>(HATEOASImplementorHotel.createHotelsList(hotels.getContent()), HttpStatus.OK);
	}
	
	//Kreiranje hotela
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše avio kompaniju.", notes = "Povratna vrednost servisa je sačuvan hotel.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Hotel>> createHotel(@Valid @RequestBody Hotel hotel) {
		Hotel createdHotel =  hotelService.save(hotel);
		return new ResponseEntity<Resource<Hotel>>(HATEOASImplementorHotel.createHotel(createdHotel), HttpStatus.CREATED);
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
		Optional<Hotel> hotel = hotelService.findById(hotelId);
		if(hotel.isPresent())
			return new ResponseEntity<Resource<Hotel>>(HATEOASImplementorHotel.createHotel(hotel.get()), HttpStatus.OK);
		else
			return ResponseEntity.noContent().build();
	}
	
	//Brisanje hotela sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše hotel.", notes = "Briše hotel sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteHotelWithId(@PathVariable(value="id") Long hotelId){
		if(!hotelService.findById(hotelId).isPresent())
			return ResponseEntity.notFound().build();
		
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
	public ResponseEntity<Resource<Hotel>> updateHotelWithId(@PathVariable(value = "id") Long hotelId,
			@Valid @RequestBody Hotel newHotel) {
		Optional<Hotel> oldHotel = hotelService.findById(hotelId);
		if(oldHotel.isPresent()) {
			oldHotel.get().copyFieldsFrom(newHotel);
			hotelService.save(oldHotel.get());
			return new ResponseEntity<Resource<Hotel>>(HATEOASImplementorHotel.createHotel(oldHotel.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
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
		Optional<Hotel> hotel = hotelService.findById(hotelId);
		if(hotel.isPresent()) {
			List<Floor> floorList = hotel.get().getFloor();
			if(floorList.isEmpty())
				return ResponseEntity.noContent().build();
			else
				return new ResponseEntity<List<Resource<Floor>>>(HATEOASImplementorHotel.createFloorList(floorList), HttpStatus.OK);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Kreira sprat u hotelu
	@RequestMapping(value = "/{id}/floors", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira sprat u okviru zadatog hotela", notes = "Povratna vrednost metode je kreiran sprat.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Floor>> createFloorForHotelWithId(@PathVariable(value = "id") Long hotelId,
			@Valid @RequestBody Floor floor) {
		Optional<Hotel> hotel = hotelService.findById(hotelId);
		if(hotel.isPresent()) {
			hotel.get().add(floor);
			hotelService.save(hotel.get());
			return new ResponseEntity<Resource<Floor>>(HATEOASImplementorHotel.createFloor(floor), HttpStatus.CREATED);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Vraca sve extra option-e u okviru hotela
	@RequestMapping(value = "/{id}/extra-options", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća extra-options za dati hotel.", notes = "Povratna vrednost servisa je lista extra option-a.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<ExtraOption>>> getExtraOptionsForHotelWithId(@PathVariable(value = "id") Long hotelId) {
		Optional<Hotel> hotel = hotelService.findById(hotelId);
		if(hotel.isPresent()) {
			List<ExtraOption> extraOptionList = hotel.get().getExtraOption();
			if(extraOptionList.isEmpty())
				return ResponseEntity.noContent().build();
			else
				return new ResponseEntity<List<Resource<ExtraOption>>>(HATEOASImplementorHotel.createExtraOptionList(extraOptionList), HttpStatus.OK);
		}else {
			return ResponseEntity.notFound().build();			
		}
	}
	
	//Kreira extra option u hotelu
	@RequestMapping(value = "/{id}/extra-options", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira sprat u okviru zadatog hotela", notes = "Povratna vrednost metode je kreiran sprat.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<ExtraOption>> createExtraOptionForHotelWithId(@PathVariable(value = "id") Long hotelId,
			@Valid @RequestBody ExtraOption extraOption) {
		Optional<Hotel> hotel = hotelService.findById(hotelId);
		if(hotel.isPresent()) {
			hotel.get().add(extraOption);
			hotelService.save(hotel.get());
			return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(extraOption), HttpStatus.CREATED);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Vraca cenovnik u okviru hotela
	@RequestMapping(value = "/{id}/catalogue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća cenovnik za dati hotel.", notes = "Povratna vrednost servisa je cenovnik",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Catalogue>> getCatalogueForHotelWithId(@PathVariable(value = "id") Long hotelId) {
		Optional<Hotel> hotel = hotelService.findById(hotelId);
		if(hotel.isPresent()) {
			Catalogue catalogue = hotel.get().getCatalogue();
			if(catalogue == null)
				return ResponseEntity.noContent().build();
			else
				return new ResponseEntity<Resource<Catalogue>>(HATEOASImplementorHotel.createCatalogue(catalogue), HttpStatus.OK);
		}else {
			return ResponseEntity.notFound().build();			
		}
	}
	
	//Kreira cenovnik u hotelu
	@RequestMapping(value = "/{id}/catalogue", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira cenovnik u okviru zadatog hotela", notes = "Povratna vrednost metode je kreiran cenovnik.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Catalogue>> createCatalogueForHotelWithId(@PathVariable(value = "id") Long hotelId,
			@Valid @RequestBody Catalogue catalogue) {
		Optional<Hotel> hotel = hotelService.findById(hotelId);
		if(hotel.isPresent()) {
			hotel.get().setCatalogue(catalogue);
			hotelService.save(hotel.get());
			return new ResponseEntity<Resource<Catalogue>>(HATEOASImplementorHotel.createCatalogue(catalogue), HttpStatus.CREATED);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
