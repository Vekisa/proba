package com.isap.ISAProject.controller.rentacar;

import java.net.URI;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.service.rentacar.BranchOfficeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/branch_offices")
public class BranchOfficeController {
	@Autowired
	BranchOfficeService service;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pruža uvid u sve registrovane filijale.", responseContainer = "List", notes = "Vraća onoliko BranchOffice objekata koliko se zahteva paginacijom.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<BranchOffice>>> getAllBranchOffices(Pageable pageable){
		return new ResponseEntity<List<Resource<BranchOffice>>>(HATEOASImplementorRentacar.branchOfficeLinksList(service.getAllBranchOffices(pageable)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Filijala sa traženim ID-em.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Filijala sa traženim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<BranchOffice>> getBranchOfficeById(@PathVariable(value="id") Long broId) {
		return new ResponseEntity<Resource<BranchOffice>>(HATEOASImplementorRentacar.branchOfficeLinks(service.getBranchOfficeById(broId)), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreiranje i sačuvavanje nove filijale.", notes = "Povratna vrednost je ID nove filijale.", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = BranchOffice.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđena filijala nije validna.")
	})
	public ResponseEntity<Object> createBranchOffice(@Valid @RequestBody BranchOffice bro) {
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(service.saveBranchOffice(bro).getId()).toUri();
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
		return new ResponseEntity<Resource<BranchOffice>>(HATEOASImplementorRentacar.branchOfficeLinks(service.updateBranchOffice(broId, broDetails)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Brisanje filijale.", notes = "Briše filijalu sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Filijala sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteBranchOfficeWithID(@PathVariable(value = "id") Long id){
		service.deleteBranchOfficeWithId(id);
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
		return new ResponseEntity<List<Resource<Vehicle>>>(HATEOASImplementorRentacar.vehicleLinksList(service.getVehiclesForBranchOfficeWithId(broId)), HttpStatus.OK);
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
		return new ResponseEntity<Resource<Vehicle>>(HATEOASImplementorRentacar.vehicleLinks(service.addVehicleForBranchOfficeWithId(broId, vehicle)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{broId}/vehicles/")
	@ApiOperation(value = "Brisanje vozila date filijale.", notes = "Briše dato vozilo filijale sa prosleđenim ID-em.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Vozilo ili filijala sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteVehicleForBranchOfficeWithId(@PathVariable(value = "broId") Long broId, @Valid @RequestBody Vehicle vehicle){
		service.deleteVehicleForBranchOfficeWithId(broId, vehicle);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}/location", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažurira lokaciju zadate filijale.", notes = "Ažurira lokaciju filijale sa prosleđenim ID na osnovu parametra zahteva.", httpMethod = "PUT", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = BranchOffice.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Filijala ili destinacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<Resource<BranchOffice>> setLocationOfBranchOffice(@PathVariable("id") Long id, @RequestParam("location") Long locationId) {
		return new ResponseEntity<Resource<BranchOffice>>(HATEOASImplementorRentacar.branchOfficeLinks(service.setLocationOfBranchOffice(id, locationId)), HttpStatus.OK);
	}
	
}
