package com.isap.ISAProject.controller.hotel;

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

import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.service.hotel.CatalogueService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/catalogues")
public class CatalogueController {
	
	@Autowired
	CatalogueService catalogueService;
	
	//Lista svih cenovnika
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća cenovnike.", notes = "Povratna vrednost metode je lista cenovnika"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<Catalogue>>> getAllCatalogues(Pageable pageable){
			return new ResponseEntity<List<Resource<Catalogue>>>(HATEOASImplementorHotel.createCatalogueList(catalogueService.findAll(pageable)), HttpStatus.OK);
	}
	
	//Kreiranje cenovnika
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše cenovnik.", notes = "Povratna vrednost servisa je sačuvan cenovnik.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = Catalogue.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Catalogue>> createCatalogue(@Valid @RequestBody Catalogue catalogue) {
		return new ResponseEntity<Resource<Catalogue>>(HATEOASImplementorHotel.createCatalogue(catalogueService.save(catalogue)), HttpStatus.CREATED);
	}
	
	//Vraca cenovnik sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća cenovnik sa zadatim ID-em.", notes = "Povratna vrednost metode je cenovnik koji ima zadati ID.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Catalogue.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Catalogue>> getCatalogueById(@PathVariable(value="id") Long catalogueId) {
			return new ResponseEntity<Resource<Catalogue>>(HATEOASImplementorHotel.createCatalogue(catalogueService.findById(catalogueId)), HttpStatus.OK);
	}
	
	//Brisanje cenovnik sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše cenovnik.", notes = "Briše cenovnik sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteCatalogueWithId(@PathVariable(value="id") Long catalogueId){		
		catalogueService.deleteById(catalogueId);
		return ResponseEntity.ok().build();
	}
	
	//Update cenovnika sa zadatim id-em ???? DA LI OVO IMA SMISLAAA ????
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update cenovnika.", notes = "Ažurira cenovnik sa zadatim ID-em na osnovu prosleđenog cenovnika."
			+ " Kolekcije originalnog cenovnika ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Catalogue.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<Catalogue>> updateCatalogueWithId(@PathVariable(value = "id") Long catalogueId,
			@Valid @RequestBody Catalogue newCatalogue) {
			return new ResponseEntity<Resource<Catalogue>>(HATEOASImplementorHotel.createCatalogue(catalogueService.updateCatalogueById(catalogueId, newCatalogue)), HttpStatus.OK);
	}
	
	//Vraca tipove soba za dati cenovnik
	@RequestMapping(value = "/{id}/room-types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraca tipove soba za dati cenovnik.", notes = "Povratna vrednost servisa je lista tipova soba.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<RoomType>>> getRoomTypesForCatalogueWithId(@PathVariable(value = "id") Long catalogueId) {
				return new ResponseEntity<List<Resource<RoomType>>>(HATEOASImplementorHotel.createRoomTypeList(catalogueService.getRoomTypes(catalogueId)), HttpStatus.OK);
	}
	
	//Kreira tip sobe za dati cenovnik
	@RequestMapping(value = "/{id}/room-types", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira tip sobe za cenovnik", notes = "Povratna vrednost metode je kreirani tip sobe.",
			httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = RoomType.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<RoomType>> createRoomTypeForCatalogueWithId(@PathVariable(value = "id") Long catalogueId,
			@Valid @RequestBody RoomType roomType) {
			return new ResponseEntity<Resource<RoomType>>(HATEOASImplementorHotel.createRoomType(catalogueService.createRoomType(catalogueId, roomType)), HttpStatus.CREATED);
	}
	
	//vraca hotel od prosledjenog cenovnika
	@RequestMapping(value = "/{id}/hotel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraca hotel.", notes = "Povratna vrednost servisa je hotel kome cenovnik", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Hotel.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found")
	})
	public ResponseEntity<Resource<Hotel>> getHotelForCatalogueWithId(@PathVariable("id") Long catalogueId) {
				return new ResponseEntity<Resource<Hotel>>(HATEOASImplementorHotel.createHotel(catalogueService.getHotel(catalogueId)), HttpStatus.OK);
	}
}
