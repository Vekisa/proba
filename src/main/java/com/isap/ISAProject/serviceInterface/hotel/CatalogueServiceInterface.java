package com.isap.ISAProject.serviceInterface.hotel;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.RoomType;

public interface CatalogueServiceInterface {

	Catalogue findById(long id);
	
	List<Catalogue> findAll(Pageable pageable);
	
	Catalogue save(Catalogue catalogue);
	
	void deleteById(long id);
	
	Catalogue updateCatalogueById(Long catalogueId, Catalogue newCatalogue);
	
	List<RoomType> getRoomTypes(Long id);
	
	public RoomType createRoomType(Long catalogueId, RoomType roomType);
	
	List<Hotel> getHotels(Long catalogueId);
}
