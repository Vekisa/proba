package com.isap.ISAProject.controller.rentacar;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.RentACar.BranchOffice;
import com.isap.ISAProject.exception.ResourceNotFoundException;
import com.isap.ISAProject.repository.rentacar.BranchOfficeRepository;


@RestController
@RequestMapping("/branch_offices")
public class BranchOfficeController {
	@Autowired
	BranchOfficeRepository branchOfficeRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<BranchOffice> getAllBranchOffices(){
		return branchOfficeRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public BranchOffice createBranchOffice(@Valid @RequestBody BranchOffice bro) {
		return branchOfficeRepository.save(bro);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public BranchOffice getBranchOfficeById(@PathVariable(value="id") Long broId) {
		return branchOfficeRepository.findById(broId).orElseThrow(() -> new ResourceNotFoundException("RentACar", "id", broId));
	}
	
	/*@PutMapping("/branch_offices/{id}")
	public BranchOffice updateBranchOffice(@PathVariable(value="id") Long broId, @Valid @RequestBody BranchOffice broDetails) {
		BranchOffice bro = branchOfficeRepository.findById(broId).orElseThrow(() -> new ResourceNotFoundException("RentACar", "id", broId));
		bro.setAddress(broDetails.getAddress());
		bro.setRentACar(broDetails.getRentACar());
		 
		bro.removeAllVehicles();
		for(Vehicle veh : broDetails.getVehicles()) {
			bro.addVehicle(veh);
		}
		 
		return branchOfficeRepository.save(bro);
	}*/
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteBranchOffice(@PathVariable(value="id") Long broId){
		BranchOffice bro = branchOfficeRepository.findById(broId).orElseThrow(() -> new ResourceNotFoundException("RentACar", "id", broId));
		
		branchOfficeRepository.delete(bro);
		
		return ResponseEntity.ok().build();
	}
	
}
