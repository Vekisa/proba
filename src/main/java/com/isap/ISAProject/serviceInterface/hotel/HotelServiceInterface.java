package com.isap.ISAProject.serviceInterface.hotel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.model.user.CompanyAdmin;

public interface HotelServiceInterface {

	Hotel findById(long id);
	
	List<Hotel> findAll(Pageable pageable);
	
	Hotel saveWithLocation(Hotel hotel, Long id);
	
	Hotel save(Hotel hotel);
	
	void deleteById(long id);
	
	Hotel updateHotelById(Long hotelId, Hotel newHotel);
	
	List<Floor> getFloors(Long id);
	
	Floor createFloor(Long hotelId, Floor floor);
	
	List<ExtraOption> getExtraOptions(Long id);
	
	ExtraOption createExtraOption(Long hotelId, ExtraOption extraOption);
	
	Catalogue getCatalogue(Long id);
	
	Catalogue createCatalogue(Long hotelId, Long catalogueId);
	
	Catalogue findCatalogue(Long catalogueId);
	
	Hotel changeLocationOfHotel(Long hotelId, Long destinationId);
	
	Location findDestination(Long id);
	
	List<CompanyAdmin> getAdminsOfHotel(Long id);
	
	Location getLocationOfHotel(Long id);
	
	Map<Long, Double> getIncomeFor(Long id, Date beginDate, Date endDate);
	
	List<Room> getRooms(Long id);
	
	List<RoomType> getRoomTypes(Long id);
	
	List<Hotel> search(Pageable pageable, String locationName, String hotelName, Date beginDate, Date endDate);
	
	Map<Long, Integer> getStatisticFor(Long id, Date beginDate, Date endDate);
}
