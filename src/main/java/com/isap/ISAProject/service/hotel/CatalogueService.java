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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.repository.hotel.CatalogueRepository;
import com.isap.ISAProject.repository.hotel.RoomTypeRepository;
import com.isap.ISAProject.serviceInterface.hotel.CatalogueServiceInterface;

@Service
@Transactional(readOnly = true)
public class CatalogueService implements CatalogueServiceInterface {
	
private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CatalogueRepository catalogueRepository;

	@Autowired
	private RoomTypeRepository roomTypeRepository;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Catalogue findById(long id) {
		logger.info("> Catalogue findById id:{}", id);
		Optional<Catalogue> catalogue = catalogueRepository.findById(id);
		logger.info("< Catalogue findById id:{}", id);
		if(catalogue.isPresent())
			return catalogue.get();
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cenovnik sa zadatim id-em ne postoji");
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Catalogue> findAll(Pageable pageable) {
		logger.info("> Catalogue findAll");
		Page<Catalogue> catalogues = catalogueRepository.findAll(pageable);
		logger.info("< Catalogue findAll");
		if(!catalogues.isEmpty())
			return catalogues.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cenovnici ne postoje");
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Catalogue save(Catalogue catalogue) {
		logger.info("> Catalogue create");
		Catalogue savedCatalogue = catalogueRepository.save(catalogue);
		logger.info("< Catalogue create");
		return savedCatalogue;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteById(long id) {
		logger.info("> Catalogue delete");
		this.findById(id);
		catalogueRepository.deleteById(id);
		logger.info("< Catalogue delete");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public Catalogue updateCatalogueById(Long catalogueId, Catalogue newCatalogue) {
		logger.info("> Catalogue update");
		Catalogue oldCatalogue = this.findById(catalogueId);
		catalogueRepository.save(oldCatalogue);
		logger.info("< Catalogue update");
		return oldCatalogue;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<RoomType> getRoomTypes(Long id){
		logger.info("> get room-types for catalogue");
		Catalogue catalogue = this.findById(id);
		List<RoomType> roomTypeList = catalogue.getRoomTypes();
		logger.info("< get room-types for catalogue");
		if(!roomTypeList.isEmpty())
			return roomTypeList;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipovi soba za dati cenovnik ne postoje");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED) 
	public RoomType createRoomType(Long catalogueId, RoomType roomType){
		logger.info("> create room-type for catalogue");
		Catalogue catalogue = this.findById(catalogueId);
		roomType.setCatalogue(catalogue);
		roomTypeRepository.save(roomType);
		logger.info("< create room-type for catalogue");
		return roomType;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Hotel> getHotels(Long catalogueId) {
		logger.info("> hotel from catalogue", catalogueId);
		Catalogue catalogue = this.findById(catalogueId);
		List<Hotel> hotels = catalogue.getHotels();
		logger.info("< hotel from catalogue");
		if(!hotels.isEmpty())
			return hotels;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Hotel za dati cenovnik nije postavljen");
	}

}
