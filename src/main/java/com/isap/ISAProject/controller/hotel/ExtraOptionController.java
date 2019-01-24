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
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.hotel.ExtraOption;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import service.hotel.ExtraOptionService;

@RestController
@RequestMapping("/extra_options")
public class ExtraOptionController {
	
	@Autowired
	ExtraOptionService extraOptionService;
	
	//Lista svih extra-optiona
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća extra-optione.", notes = "Povratna vrednost metode je lista extra-optiona"
			+ " koje pripadaju zahtevanoj strani (na osnovu paginacije).", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<List<Resource<ExtraOption>>> getAllExtraOptions(Pageable pageable){
		Page<ExtraOption> extraOptions = extraOptionService.findAll(pageable); 
		if(extraOptions.isEmpty())
			return ResponseEntity.noContent().build();
		else
			return new ResponseEntity<List<Resource<ExtraOption>>>(HATEOASImplementorHotel.createExtraOptionList(extraOptions.getContent()), HttpStatus.OK);
	}
	
	//Kreiranje extra-optiona
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira i memoriše extra option.", notes = "Povratna vrednost servisa je sačuvan extra option.",
			httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = ExtraOption.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<ExtraOption>> createExtraOption(@Valid @RequestBody ExtraOption extraOption) {
		ExtraOption createdExtraOption =  extraOptionService.save(extraOption);
		return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(createdExtraOption), HttpStatus.CREATED);
	}
	

	//Vraca extra-option sa zadatim ID-em
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća extra-option sa zadatim ID-em.", notes = "Povratna vrednost metode je extra-option koji ima zadati ID.",
			httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = ExtraOption.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<ExtraOption>> getExtraOptionById(@PathVariable(value="id") Long extraOptionId) {
		Optional<ExtraOption> extraOption = extraOptionService.findById(extraOptionId);
		if(extraOption.isPresent())
			return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(extraOption.get()), HttpStatus.OK);
		else
			return ResponseEntity.noContent().build();
	}
	
	//Brisanje extra-optiona sa zadatim id-em
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ApiOperation(value = "Briše extra-option.", notes = "Briše extra-option sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<?> deleteExtraOptionWithId(@PathVariable(value="id") Long extraOptionId){
		if(!extraOptionService.findById(extraOptionId).isPresent())
			return ResponseEntity.notFound().build();
		
		extraOptionService.deleteById(extraOptionId);
		return ResponseEntity.ok().build();
	}
	
	//Update extra-optiona sa zadatim id-em
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update extra-optiona.", notes = "Ažurira extra-option sa zadatim ID-em na osnovu prosleđenog ectra-optiona."
			+ " Kolekcije originalnog extra-optiona ostaju netaknute.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = ExtraOption.class),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Resource<ExtraOption>> updateExtraOptionWithId(@PathVariable(value = "id") Long extraOptionId,
			@Valid @RequestBody ExtraOption newExtraOption) {
		Optional<ExtraOption> oldExtraOption = extraOptionService.findById(extraOptionId);
		if(oldExtraOption.isPresent()) {
			oldExtraOption.get().copyFieldsFrom(newExtraOption);
			extraOptionService.save(oldExtraOption.get());
			return new ResponseEntity<Resource<ExtraOption>>(HATEOASImplementorHotel.createExtraOption(oldExtraOption.get()), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
