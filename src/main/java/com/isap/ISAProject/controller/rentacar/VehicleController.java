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
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.repository.rentacar.VehicleRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
	@Autowired
	VehicleRepository vehicleRepository;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pruža uvid u sva registrovana vozila.", responseContainer = "List", notes = "Vraća onoliko Vehicle objekata koliko se zahteva paginacijom.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Lista je prazna."),
			@ApiResponse(code = 400, message = "Bad Request. Parametri paginacije nisu ispravni.")
	})
	public ResponseEntity<List<Resource<Vehicle>>> getAllVehicles(Pageable pageable){
		Page<Vehicle> vehicles =  vehicleRepository.findAll(pageable);
		if(vehicles.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return new ResponseEntity<List<Resource<Vehicle>>>(HATEOASImplementorRentacar.vehicleLinksList(vehicles.getContent()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Vozilo sa traženim ID-em.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Vehicle.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Vozilo sa traženim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<Vehicle>> getVehicleById(@PathVariable(value="id") Long vehId) {
		Vehicle vehicle = vehicleRepository.findById(vehId).get();
		if(vehicle == null) {
			throw new ResourceNotFoundException("id: " + vehId);
		}
		return new ResponseEntity<Resource<Vehicle>>(HATEOASImplementorRentacar.vehicleLinks(vehicle), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Kreiranje i sačuvavanje novog vozila.", notes = "Povratna vrednost je ID novog vozila.", httpMethod = "POST", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Vehicle.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeno vozilo nije validno.")
	})
	public ResponseEntity<Object> createVehicle(@Valid @RequestBody Vehicle veh) {
		Vehicle savedVeh = vehicleRepository.save(veh);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedVeh.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Ažuriranje vozila.", notes = "Ažurira vozilo sa prosleđenim ID-em na osnovu prosleđenog vozila. Kolekcija originalnog vozila ostaje netaknuta.", httpMethod = "PUT", consumes = "application/json", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Airline.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili vozilo nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Vozilo sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<Vehicle>> updateVehicle(@PathVariable(value="id") Long vehId, @Valid @RequestBody Vehicle vehDetails) {
		Optional<Vehicle> stariVeh = vehicleRepository.findById(vehId);
		if(stariVeh.get() == null) {
			throw new ResourceNotFoundException("id: " + vehId);
		}
		else if(stariVeh.isPresent()) {
			stariVeh.get().copyFieldsFrom(vehDetails);
		}
		return new ResponseEntity<Resource<Vehicle>>(HATEOASImplementorRentacar.vehicleLinks(stariVeh.get()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Brisanje vozila.", notes = "Briše vozilo sa prosleđenim ID-em", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Vozilo sa prosleđenim ID ne postoji.")
	})
	public ResponseEntity<?> deleteVehicleWithID(@PathVariable(value = "id") Long id){
		Vehicle deletedVeh = vehicleRepository.findById(id).get();
		if(deletedVeh == null) {
			throw new ResourceNotFoundException("id: " + id);
		}
		vehicleRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/vehicle-reservations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Pruža uvid u sve rezervacije datog vozila.", notes = "Povratna vrednost je lista svih rezervacija vozila sa navedenim ID-em.", httpMethod = "GET", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = List.class),
			@ApiResponse(code = 204, message = "No Content. Ne postoje informacije o rezervacijama za dato vozilo."),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Vozilo sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<List<Resource<VehicleReservation>>> getVehicleReservationsForVehicleWithId(@PathVariable(value = "id") Long vehId) {
		Optional<Vehicle> veh = vehicleRepository.findById(vehId);
		if(veh.isPresent()) {
			List<VehicleReservation> list = veh.get().getVehicleReservations();
			if(list.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<List<Resource<VehicleReservation>>>(HATEOASImplementorRentacar.vehicleReservationLinksList(list), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{id}/vehicle-reservations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Dodaje novu rezervaciju za dato vozilo.", notes = "Povratna vrednost je dodata rezervacija.", httpMethod = "POST", produces = "application/json", consumes = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created", response = Vehicle.class),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID ili informacija o rezervaciji nisu validni."),
			@ApiResponse(code = 404, message = "Not Found. Vozilo sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<Resource<VehicleReservation>> addVehicleReservationForVehicleWithId(@PathVariable(value = "id") Long vehId,
			@Valid @RequestBody VehicleReservation vehicleRes) {
		Optional<Vehicle> vehicle = vehicleRepository.findById(vehId);
		if(vehicle.isPresent()) {
			vehicle.get().addVehicleReservation(vehicleRes);
			vehicleRepository.save(vehicle.get());
			return new ResponseEntity<Resource<VehicleReservation>>(HATEOASImplementorRentacar.vehicleReservationLinks(vehicleRes), HttpStatus.CREATED);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/{vehId}/vehicle-reservations/")
	@ApiOperation(value = "Brisanje rezervacije datog vozila.", notes = "Briše datu rezervaciju vozila sa prosleđenim ID-em.", httpMethod = "DELETE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request. Prosleđeni ID nije validan."),
			@ApiResponse(code = 404, message = "Not Found. Rezervacija ili vozilo sa prosleđenim ID-em ne postoji.")
	})
	public ResponseEntity<?> deleteVehicleReservationForVehicleWithId(@PathVariable(value = "vehId") Long vehId, @Valid @RequestBody VehicleReservation vehicleRes){
		Vehicle vehicle = vehicleRepository.findById(vehId).get();
		if(vehicle == null) {
			throw new ResourceNotFoundException("Vozilo sa id-em: " + vehId + " ne postoji.");
		}
		else if(!vehicle.getVehicleReservations().contains(vehicleRes)) {
			throw new ResourceNotFoundException("Prosleđena rezervacija ne postoji za dato vozilo.");
		}
		vehicle.getVehicleReservations().remove(vehicleRes);
		vehicleRepository.save(vehicle);
		return ResponseEntity.ok().build();
	}
}
