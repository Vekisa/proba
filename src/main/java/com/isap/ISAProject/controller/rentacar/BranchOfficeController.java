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
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.repository.rentacar.BranchOfficeRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/branch_offices")
public class BranchOfficeController {
	@Autowired
	BranchOfficeRepository branchOfficeRepository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pruža uvid u sve registrovane filijale.", responseContainer = "List", notes = "Vraća onoliko BranchOffice objekata koliko se zahteva paginacijom.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<BranchOffice>>> getAllBranchOffices(Pageable pageable){
		Page<BranchOffice> branches =  branchOfficeRepository.findAll(pageable);
		if(branches.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return new ResponseEntity<List<Resource<BranchOffice>>>(HATEOASImplementorRentacar.branchOfficeLinksList(branches.getContent()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Filijala sa traženim ID-em.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Filijala sa traženim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<BranchOffice>> getBranchOfficeById(@PathVariable(value="id") Long broId) {
		BranchOffice branch = branchOfficeRepository.findById(broId).get();
		if(branch == null) {
			throw new ResourceNotFoundException("id: " + broId);
		}
		return new ResponseEntity<Resource<BranchOffice>>(HATEOASImplementorRentacar.branchOfficeLinks(branch), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreiranje i sačuvavanje nove filijale.", notes = "Povratna vrednost je ID nove filijale.", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = BranchOffice.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđena filijala nije validna.")
	})
	public ResponseEntity<Object> createBranchOffice(@Valid @RequestBody BranchOffice bro) {
		BranchOffice savedBro = branchOfficeRepository.save(bro);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedBro.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažuriranje filijale.", notes = "Ažurira filijalu sa prosleđenim ID-em na osnovu prosleđene filijale. Kolekcija originalne filijale ostaje netaknuta.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili filijala nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Filijala sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<BranchOffice>> updateBranchOffice(@PathVariable(value="id") Long broId, @Valid @RequestBody BranchOffice broDetails) {
		Optional<BranchOffice> stariBro = branchOfficeRepository.findById(broId);
		if(stariBro.get() == null) {
			throw new ResourceNotFoundException("id: " + broId);
		}
		else if(stariBro.isPresent()) {
			stariBro.get().copyFieldsFrom(broDetails);
		}
		return new ResponseEntity<Resource<BranchOffice>>(HATEOASImplementorRentacar.branchOfficeLinks(stariBro.get()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Brisanje filijale.", notes = "Briše filijalu sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Filijala sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteBranchOfficeWithID(@PathVariable(value = "id") Long id){
		BranchOffice deletedBro = branchOfficeRepository.findById(id).get();
		if(deletedBro == null) {
			throw new ResourceNotFoundException("id: " + id);
		}
		branchOfficeRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/vehicles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pruža uvid u sva vozila date filijale.", notes = "Povratna vrednost je lista svih vozila filijale sa navedenim ID-em.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje informacije o vozilima za datu filijalu."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Filijala sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<List<Resource<Vehicle>>> getVehiclesForBranchOfficeWithId(@PathVariable(value = "id") Long broId) {
		Optional<BranchOffice> bro = branchOfficeRepository.findById(broId);
		if(bro.isPresent()) {
			List<Vehicle> list = bro.get().getVehicles();
			if(list.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<Vehicle>>>(HATEOASImplementorRentacar.vehicleLinksList(list), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/vehicles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Dodaje novo vozilo za datu filijalu.", notes = "Povratna vrednost je dodato vozilo.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = BranchOffice.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili informacija o vozilu nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Filijala sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<Vehicle>> addVehicleForBranchOfficeWithId(@PathVariable(value = "id") Long broId,
			@Valid @RequestBody Vehicle vehicle) {
		Optional<BranchOffice> branch = branchOfficeRepository.findById(broId);
		if(branch.isPresent()) {
			branch.get().addVehicle(vehicle);
			branchOfficeRepository.save(branch.get());
			return new ResponseEntity<Resource<Vehicle>>(HATEOASImplementorRentacar.vehicleLinks(vehicle), HttpStatus.CREATED);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{broId}/vehicles/")
	@ApiOperation(value = "Brisanje vozila date filijale.", notes = "Briše dato vozilo filijale sa prosleđenim ID-em.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Vozilo ili filijala sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteVehicleForBranchOfficeWithId(@PathVariable(value = "broId") Long broId, @Valid @RequestBody Vehicle vehicle){
		BranchOffice branch = branchOfficeRepository.findById(broId).get();
		if(branch == null) {
			throw new ResourceNotFoundException("Filijala sa id-em: " + broId + " ne postoji.");
		}
		else if(!branch.getVehicles().contains(vehicle)) {
			throw new ResourceNotFoundException("Prosleđeno vozilo ne postoji u okviru date filijale.");
		}
		branch.getVehicles().remove(vehicle);
		branchOfficeRepository.save(branch);
		return ResponseEntity.ok().build();
	}
}
