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
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.repository.rentacar.VehicleReservationRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/vehicle-reservations")
public class VehicleReservationController {
	@Autowired
	VehicleReservationRepository vehicleReservationRepository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pruža uvid u svе registracije svih vozila.", responseContainer = "List", notes = "Vraća onoliko VehicleReservation objekata koliko se zahteva paginacijom.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<VehicleReservation>>> getAllVehicleReservations(Pageable pageable){
		Page<VehicleReservation> vehicleRes =  vehicleReservationRepository.findAll(pageable);
		if(vehicleRes.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return new ResponseEntity<List<Resource<VehicleReservation>>>(HATEOASImplementorRentacar.vehicleReservationLinksList(vehicleRes.getContent()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Reyervacija vozila sa traženim ID-em.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = VehicleReservation.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rezervacija sa traženim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<VehicleReservation>> getVehicleReservationById(@PathVariable(value="id") Long vehId) {
		VehicleReservation vehicleRes = vehicleReservationRepository.findById(vehId).get();
		if(vehicleRes == null) {
			throw new ResourceNotFoundException("id: " + vehId);
		}
		return new ResponseEntity<Resource<VehicleReservation>>(HATEOASImplementorRentacar.vehicleReservationLinks(vehicleRes), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreiranje i sačuvavanje nove rezervacije.", notes = "Povratna vrednost je ID nove rezervacije.", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = VehicleReservation.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđena rezervacija nije validna.")
	})
	public ResponseEntity<Object> createVehicleReservation(@Valid @RequestBody VehicleReservation vehRes) {
		VehicleReservation savedVeh = vehicleReservationRepository.save(vehRes);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedVeh.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažuriranje rezervacije vozila.", notes = "Ažurira rezervaciju vozila sa prosleđenim ID-em na osnovu prosleđene rezervacije.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili rezervacija nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Vozilo sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<VehicleReservation>> updateVehicleReservation(@PathVariable(value="id") Long vehId, @Valid @RequestBody VehicleReservation vehDetails) {
		Optional<VehicleReservation> stariVeh = vehicleReservationRepository.findById(vehId);
		if(stariVeh.get() == null) {
			throw new ResourceNotFoundException("id: " + vehId);
		}
		else if(stariVeh.isPresent()) {
			stariVeh.get().copyFieldsFrom(vehDetails);
		}
		return new ResponseEntity<Resource<VehicleReservation>>(HATEOASImplementorRentacar.vehicleReservationLinks(stariVeh.get()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Brisanje vozila.", notes = "Briše rezervaciju sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rezervacija sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteVehicleReservationWithID(@PathVariable(value = "id") Long id){
		VehicleReservation deletedVeh = vehicleReservationRepository.findById(id).get();
		if(deletedVeh == null) {
			throw new ResourceNotFoundException("id: " + id);
		}
		vehicleReservationRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}

