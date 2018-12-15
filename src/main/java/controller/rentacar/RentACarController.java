package controller.rentacar;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import model.exception.ResourceNotFoundException;
import model.rentacar.RentACar;
import repository.rentacar.RentACarRepository;


@RestController
@RequestMapping("/rent_a_cars")
public class RentACarController {
	@Autowired
	private RentACarRepository rentACarRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<RentACar> getAllRentACars(){
		return rentACarRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public RentACar createRentACar(@Valid @RequestBody RentACar rac) {
		return rentACarRepository.save(rac);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public RentACar getRentACarById(@PathVariable(value="id") Long racId) {
		return new RentACar(); //rentACarRepository.findById(racId).orElseThrow(() -> new ResourceNotFoundException("RentACar", "id", racId));
	}
	
	/*@PutMapping("/rent_a_cars/{id}")
	public RentACar updateRentACar(@PathVariable(value="id") Long racId, @Valid @RequestBody RentACar racDetails) {
		RentACar rac = rentACarRepository.findById(racId).orElseThrow(() -> new ResourceNotFoundException("RentACar", "id", racId));
		
		rac.setName(racDetails.getName());
		rac.setDescription(racDetails.getDescription());
		rac.setAddress(racDetails.getAddress());
		
		rac.removeAllBranchOffices();
		for(BranchOffice bof : racDetails.getBranchOffices()) {
			rac.addBranchOffice(bof);
		}
		
		return rentACarRepository.save(rac);
	}*/
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteRentACar(@PathVariable(value="id") Long racId){
		RentACar rac = rentACarRepository.findById(racId).orElseThrow(() -> new ResourceNotFoundException("RentACar", "id", racId));
		
		rentACarRepository.delete(rac);
		
		return ResponseEntity.ok().build();
	}
}
