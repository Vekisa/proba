package com.isap.ISAProject.controller.rentacar;

import java.net.URI;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.isap.ISAProject.controller.user.HATEOASImplementorUsers;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.service.rentacar.RentACarService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/rent-a-cars")
public class RentACarController {
	@Autowired
	private RentACarService service;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pruža uvid u sve registrovane rent-a-car kompanije.", responseContainer = "List", notes = "Vraća onoliko RentACar objekata koliko se zahteva paginacijom.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<RentACar>>> getAllRentACars(Pageable pageable){
		return new ResponseEntity<List<Resource<RentACar>>>(HATEOASImplementorRentacar.rentacarLinksList(service.getAllRentACars(pageable)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pretraga rent-a-car servisa", responseContainer = "List", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<RentACar>>> search(Pageable pageable, 
			@RequestParam(value="locationName", required=false) String locationName,
			@RequestParam(value="name", required=false) String name,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value="beginDate", required=false) Date begin, 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value="endDate", required=false) Date end){
		List<RentACar> ret = service.search(pageable, locationName, name, begin, end);
		return new ResponseEntity<List<Resource<RentACar>>>(HATEOASImplementorRentacar.rentacarLinksList(ret), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Rent-a-car kompanija sa traženim ID-em.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rent-a-car kompanija sa traženim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<RentACar>> getRentACarById(@PathVariable(value="id") Long racId) {
		return new ResponseEntity<Resource<RentACar>>(HATEOASImplementorRentacar.rentacarLinks(service.getRentACarById(racId)), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreiranje i sačuvavanje nove renta-a-car kompanije.", notes = "Povratna vrednost je ID nove rent-a-car kompanije.", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = RentACar.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđena rent-a-car kompanija nije validna.")
	})
	public ResponseEntity<Object> saveRentACar(@Valid @RequestBody RentACar rac) {
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(service.saveRentACar(rac).getId()).toUri();
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
		return new ResponseEntity<Resource<RentACar>>(HATEOASImplementorRentacar.rentacarLinks(service.updateRentACar(racId, racDetails)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Brisanje rent-a-car kompanije.", notes = "Briše rent-a-car kompaniju sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rent-a-car kompanija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteRentACarWithID(@PathVariable(value = "id") Long id){
		service.deleteRentACar(id);
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
		return new ResponseEntity<List<Resource<BranchOffice>>>(HATEOASImplementorRentacar.branchOfficeLinksList(service.getBranchOffices(racId)), HttpStatus.OK);
			
	}
	
	@RequestMapping(value = "/{id}/branch_offices", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreira novu filijalu za datu rent-a-car kompaniju.", notes = "Povratna vrednost je dodata filijala.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = BranchOffice.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili informacija o filijali nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Rent-a-car kompanija sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<BranchOffice>> addBranchOfficeForRentACarWithId(@PathVariable(value = "id") Long racId,
			@Valid @RequestBody BranchOffice branch, @RequestParam("location") Long locationId) {
		return new ResponseEntity<Resource<BranchOffice>>(HATEOASImplementorRentacar.branchOfficeLinks(service.addBranchOffice(racId, branch, locationId)), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{racId}/branch_offices/")
	@ApiOperation(value = "Brisanje filijale date rent-a-car kompanije.", notes = "Briše datu filijalu rent-a-car kompanije sa prosleđenim ID-em.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rent-a-car kompanija ili filijala sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteBranchOfficeForRentACarWithId(@PathVariable(value = "racId") Long racId, @Valid @RequestBody BranchOffice branch){
		service.deleteBranchOffice(racId, branch);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/admins", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vraća admine za dati rent a car.", notes = "Povratna vrednost servisa je lista resursa admina kompanije.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje admini za dati rent a car."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rent a car sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<List<Resource<CompanyAdmin>>> getAdminsOfAirlineWithId(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<CompanyAdmin>>>(HATEOASImplementorUsers.createCompanyAdminsList(service.getAdminsOfRentACar(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/vehicles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pruža uvid u sva registrovana vozila ove kompanije.", responseContainer = "List", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request.")
	})
	public ResponseEntity<List<Resource<Vehicle>>> getAllVehicles(@PathVariable("id") Long id){
		return new ResponseEntity<List<Resource<Vehicle>>>(HATEOASImplementorRentacar.vehicleLinksList(service.getAllVehicles(id)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/income", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<Long, Double>> getIncomeForRentACar(@PathVariable("id") Long id, @RequestParam("begin") Long begin, @RequestParam("end") Long end) {
		return new ResponseEntity<Map<Long,Double>>(service.getIncomeFor(id, new Date(begin), new Date(end)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/statistic", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<Long, Integer>> getStatisticForAirline(@PathVariable("id") Long id, @RequestParam("begin") Long begin, @RequestParam("end") Long end) {
		return new ResponseEntity<Map<Long,Integer>>(service.getStatisticFor(id, new Date(begin), new Date(end)), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/quicks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<VehicleReservation>>> getQuickVehicleReservationsForRentACar(@PathVariable("id") Long id) {
		return new ResponseEntity<List<Resource<VehicleReservation>>>(HATEOASImplementorRentacar.vehicleReservationLinksList(service.getQuickVehicleReservations(id)), HttpStatus.OK);
	}
	
}
