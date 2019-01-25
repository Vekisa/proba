package com.isap.ISAProject.service.hotel;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.repository.hotel.CatalogueRepository;

@Service
@Transactional(readOnly = true)
public class CatalogueService {
	
private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CatalogueRepository catalogueRepository;
	
	public Catalogue findById(long id) {
		logger.info("> Catalogue findById id:{}", id);
		Optional<Catalogue> catalogue = catalogueRepository.findById(id);
		logger.info("< Catalogue findById id:{}", id);
		if(catalogue.isPresent())
			return catalogue.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cenovnik sa zadatim id-em ne postoji");
	}
	
	public List<Catalogue> findAll(Pageable pageable) {
		logger.info("> Catalogue findAll");
		Page<Catalogue> catalogues = catalogueRepository.findAll(pageable);
		logger.info("< Catalogue findAll");
		if(!catalogues.isEmpty())
			return catalogues.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cenovnici ne postoje");
	}
	
	public Catalogue save(Catalogue catalogue) {
		logger.info("> Catalogue create");
		Catalogue savedCatalogue = catalogueRepository.save(catalogue);
		logger.info("< Catalogue create");
		return savedCatalogue;
	}
	
	public void deleteById(long id) {
		logger.info("> Catalogue delete");
		this.findById(id);
		catalogueRepository.deleteById(id);
		logger.info("< Catalogue delete");
	}
	
	public Catalogue updateCatalogueById(Long catalogueId, Catalogue newCatalogue) {
		logger.info("> Catalogue update");
		Catalogue oldCatalogue = this.findById(catalogueId);
		oldCatalogue.copyFieldsFrom(newCatalogue);
		catalogueRepository.save(oldCatalogue);
		logger.info("< Catalogue update");
		return oldCatalogue;
	}
	
	public List<RoomType> getRoomTypes(Long id){
		logger.info("> get room-types for catalogue");
		Catalogue catalogue = this.findById(id);
		List<RoomType> roomTypeList = catalogue.getRoomType();
		logger.info("< get room-types for catalogue");
		if(!roomTypeList.isEmpty())
			return roomTypeList;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipovi soba za dati cenovnik ne postoje");
	}
	
	public RoomType createRoomType(Long catalogueId, RoomType roomType){
		logger.info("> create room-type for catalogue");
		Catalogue catalogue = this.findById(catalogueId);
		catalogue.getRoomType().add(roomType);
		roomType.setCatalogue(catalogue);
		this.save(catalogue);
		logger.info("< create room-type for catalogue");
		return roomType;
	}

}
