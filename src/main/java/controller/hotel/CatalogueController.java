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

import com.isap.ISAProject.exception.ResourceNotFoundException;
import com.isap.ISAProject.model.Hotel.Catalogue;

import repository.hotel.CatalogueRepository;

@RestController
@RequestMapping("/catalogues")
public class CatalogueController {
	@Autowired
	CatalogueRepository catalogueRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Catalogue> getAllCatalogues(){
		return catalogueRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Catalogue createCatalogue(@Valid @RequestBody Catalogue catalogue) {
		return catalogueRepository.save(catalogue);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public Catalogue getCatalogueById(@PathVariable(value="id") Long catalogueId) {
		return catalogueRepository.findById(catalogueId).orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", catalogueId));
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteCatalogue(@PathVariable(value="id") Long catalogueId){
		Catalogue catalogue = catalogueRepository.findById(catalogueId).orElseThrow(() -> 
			new ResourceNotFoundException("Catalogue", "id", catalogueId));
		
		catalogueRepository.delete(catalogue);
		
		return ResponseEntity.ok().build();
	}
}
