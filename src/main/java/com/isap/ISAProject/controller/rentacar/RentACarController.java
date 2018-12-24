package com.isap.ISAProject.controller.rentacar;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.isap.ISAProject.exception.ResourceNotFoundException;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.repository.rentacar.RentACarRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/rent-a-cars")
public class RentACarController {
	@Autowired
	private RentACarRepository rentACarRepository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pruža uvid u sve registrovane rent-a-car kompanije.", responseContainer = "List", notes = "Vraća onoliko RentACar objekata koliko se zahteva paginacijom.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<RentACar>>> getAllRentACars(Pageable pageable){
		Page<RentACar> rentacars =  rentACarRepository.findAll(pageable);
		if(rentacars.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return new ResponseEntity<List<Resource<RentACar>>>(HATEOASImplementorRentacar.rentacarLinksList(rentacars.getContent()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Rent-a-car kompanija sa traženim ID-em.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rent-a-car kompanija sa traženim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<RentACar>> getRentACarById(@PathVariable(value="id") Long racId) {
		RentACar rentacar = rentACarRepository.findById(racId).get();
		if(rentacar == null) {
			throw new ResourceNotFoundException("id: " + racId);
		}
		return new ResponseEntity<Resource<RentACar>>(HATEOASImplementorRentacar.rentacarLinks(rentacar), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreiranje i sačuvavanje nove renta-a-car kompanije.", notes = "Povratna vrednost je ID nove rent-a-car kompanije.", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = RentACar.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđena rent-a-car kompanija nije validna.")
	})
	public ResponseEntity<Object> createRentACar(@Valid @RequestBody RentACar rac) {
		RentACar savedRentacar = rentACarRepository.save(rac);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedRentacar.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažuriranje rent-a-car kompanije.", notes = "Ažurira rent-a-car kompaniju sa prosleđenim ID-em na osnovu prosleđene rent-a-car kompanije. Kolekcija originalne rent-a-car kompanije ostaje netaknuta.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili rent-a-car kompanija nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Rent-a-car kompanija sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<RentACar>> updateRentACar(@PathVariable(value="id") Long racId, @Valid @RequestBody RentACar racDetails) {
		Optional<RentACar> stariRac = rentACarRepository.findById(racId);
		if(stariRac.get() == null) {
			throw new ResourceNotFoundException("id: " + racId);
		}
		else if(stariRac.isPresent()) {
			stariRac.get().copyFieldsFrom(racDetails);
		}
		return new ResponseEntity<Resource<RentACar>>(HATEOASImplementorRentacar.rentacarLinks(stariRac.get()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Brisanje rent-a-car kompanije.", notes = "Briše rent-a-car kompaniju sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rent-a-car kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteRentACarWithID(@PathVariable(value = "id") Long id){
		RentACar deletedRac = rentACarRepository.findById(id).get();
		if(deletedRac == null) {
			throw new ResourceNotFoundException("id: " + id);
		}
		rentACarRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/branch_offices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pruža uvid u sve filijale date rent-a-car kompanije.", notes = "Povratna vrednost je lista svih filijala rent-a-car kompanije sa navedenim ID-em.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje informacije o filijalama za datu rent-a-car kompaniju."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rent-a-car kompanija sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<List<Resource<BranchOffice>>> getBranchOfficesForRentACarWithId(@PathVariable(value = "id") Long racId) {
		Optional<RentACar> rentacar = rentACarRepository.findById(racId);
		if(rentacar.isPresent()) {
			List<BranchOffice> list = rentacar.get().getBranchOffices();
			if(list.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<BranchOffice>>>(HATEOASImplementorRentacar.branchOfficeLinksList(list), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/branch_offices", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira novu filijalu za datu rent-a-car kompaniju.", notes = "Povratna vrednost je dodata filijala.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = BranchOffice.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili informacija o filijali nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Rent-a-car kompanija sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<BranchOffice>> addBranchOfficeForRentACarWithId(@PathVariable(value = "id") Long racId,
			@Valid @RequestBody BranchOffice branch) {
		Optional<RentACar> rentacar = rentACarRepository.findById(racId);
		if(rentacar.isPresent()) {
			rentacar.get().addBranchOffice(branch);
			rentACarRepository.save(rentacar.get());
			return new ResponseEntity<Resource<BranchOffice>>(HATEOASImplementorRentacar.branchOfficeLinks(branch), HttpStatus.CREATED);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{racId}/branch_offices/")
	@ApiOperation(value = "Brisanje filijale date rent-a-car kompanije.", notes = "Briše datu filijalu rent-a-car kompanije sa prosleđenim ID-em.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rent-a-car kompanija ili filijala sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteBranchOfficeForRentACarWithId(@PathVariable(value = "racId") Long racId, @Valid @RequestBody BranchOffice branch){
		RentACar rentacar = rentACarRepository.findById(racId).get();
		if(rentacar == null) {
			throw new ResourceNotFoundException("Rent-a-car kompanija sa id-em: " + racId + " ne postoji.");
		}
		else if(!rentacar.getBranchOffices().contains(branch)) {
			throw new ResourceNotFoundException("Prosleđena filijala ne postoji u okviru date rent-a-car kompanije.");
		}
		rentacar.getBranchOffices().remove(branch);
		rentACarRepository.save(rentacar);
		return ResponseEntity.ok().build();
	}
	
}
