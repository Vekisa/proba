package controller.hotel;

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
import model.hotel.ExtraOption;
import repository.hotel.ExtraOptionRepository;

@RestController
@RequestMapping("/extra_options")
public class ExtraOptionController {
	@Autowired
	ExtraOptionRepository extraOptionRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<ExtraOption> getAllExtraOptions(){
		return extraOptionRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ExtraOption createExtraOption(@Valid @RequestBody ExtraOption extraOption) {
		return extraOptionRepository.save(extraOption);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ExtraOption getExtraOptionById(@PathVariable(value="id") Long extraOptionId) {
		return extraOptionRepository.findById(extraOptionId).orElseThrow(() -> new ResourceNotFoundException("ExtraOption", "id", extraOptionId));
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteExtraOption(@PathVariable(value="id") Long extraOptionId){
		ExtraOption extraOption = extraOptionRepository.findById(extraOptionId).orElseThrow(() -> 
			new ResourceNotFoundException("ExtraOption", "id", extraOptionId));
		
		extraOptionRepository.delete(extraOption);
		
		return ResponseEntity.ok().build();
	}	
}
