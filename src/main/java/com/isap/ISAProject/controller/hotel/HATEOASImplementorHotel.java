package com.isap.ISAProject.controller.hotel;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Resource;

import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.hotel.RoomType;

public class HATEOASImplementorHotel {

	
	public static Resource<Hotel> createHotel(Hotel hotel) {
		Resource<Hotel> resource = new Resource<Hotel>(hotel);
		resource.add(linkTo(methodOn(HotelController.class).getHotelById(hotel.getId())).withRel("self"));
		resource.add(linkTo(methodOn(HotelController.class).getAllHotels(null)).slash("?page=0&size=5").withRel("all-hotels"));
		resource.add(linkTo(methodOn(HotelController.class).getFloorsForHotelWithId(hotel.getId())).withRel("hotel-floors"));
		resource.add(linkTo(methodOn(HotelController.class).getExtraOptionsForHotelWithId(hotel.getId())).withRel("hotel-extra-options"));
		resource.add(linkTo(methodOn(HotelController.class).getCatalogueForHotelWithId(hotel.getId())).withRel("hotel-catalogue"));
		return resource;
	}
	
	public static List<Resource<Hotel>> createHotelsList(List<Hotel> hotels) {
		List<Resource<Hotel>> hotelList = new ArrayList<Resource<Hotel>>();
		for(Hotel hotel : hotels) {
			hotelList.add(HATEOASImplementorHotel.createHotel(hotel));
		}
		return hotelList;
	}
	
	public static Resource<Floor> createFloor(Floor floor){
		Resource<Floor> resource = new Resource<Floor>(floor);
		resource.add(linkTo(methodOn(FloorController.class).getFloorById(floor.getId())).withRel("self"));
		resource.add(linkTo(methodOn(FloorController.class).getAllFloors(null)).slash("?page=0&size=5").withRel("all-floors"));
		resource.add(linkTo(methodOn(FloorController.class).getRoomsForFloorWithId(floor.getId())).withRel("floor-rooms"));
		resource.add(linkTo(methodOn(FloorController.class).getHotelForFloorWithId(floor.getId())).withRel("hotel"));
		return resource;
	}
	
	public static List<Resource<Floor>> createFloorList(List<Floor> floors){
		List<Resource<Floor>> floorList = new ArrayList<Resource<Floor>>();
		for(Floor item : floors) {
			floorList.add(HATEOASImplementorHotel.createFloor(item));
		}
		return floorList;
	}
	
	public static Resource<ExtraOption> createExtraOption(ExtraOption extraOption){
		Resource<ExtraOption> resource = new Resource<ExtraOption>(extraOption);
		resource.add(linkTo(methodOn(ExtraOptionController.class).getExtraOptionById(extraOption.getId())).withRel("self"));
		resource.add(linkTo(methodOn(ExtraOptionController.class).getAllExtraOptions(null)).slash("?page=0&size=5").withRel("all-extra-options"));
		resource.add(linkTo(methodOn(ExtraOptionController.class).getHotelForExtraOptionWithId(extraOption.getId())).withRel("hotel"));
		resource.add(linkTo(methodOn(ExtraOptionController.class).getRoomReservationForExtraOptionWithId(extraOption.getId())).withRel("room-reservation"));
		return resource;
	}
	
	public static List<Resource<ExtraOption>> createExtraOptionList(List<ExtraOption> extraOptions){
		List<Resource<ExtraOption>> extraOptionList = new ArrayList<Resource<ExtraOption>>();
		for(ExtraOption item : extraOptions) {
			extraOptionList.add(HATEOASImplementorHotel.createExtraOption(item));
		}
		return extraOptionList;
	}
	
	public static Resource<Catalogue> createCatalogue(Catalogue catalogue){
		Resource<Catalogue> resource = new Resource<Catalogue>(catalogue);
		resource.add(linkTo(methodOn(CatalogueController.class).getCatalogueById(catalogue.getId())).withRel("self"));
		resource.add(linkTo(methodOn(CatalogueController.class).getAllCatalogues(null)).slash("?page=0&size=5").withRel("all-catalogues"));
		resource.add(linkTo(methodOn(CatalogueController.class).getRoomTypesForCatalogueWithId(catalogue.getId())).withRel("catalogue-room-types"));
		resource.add(linkTo(methodOn(CatalogueController.class).getHotelForCatalogueWithId(catalogue.getId())).withRel("hotel"));
		return resource;
	}
	
	public static List<Resource<Catalogue>> createCatalogueList(List<Catalogue> catalogues){
		List<Resource<Catalogue>> catalogueList = new ArrayList<Resource<Catalogue>>();
		for(Catalogue item : catalogues) {
			catalogueList.add(HATEOASImplementorHotel.createCatalogue(item));
		}
		return catalogueList;
	}
	
	public static Resource<Room> createRoom(Room room){
		Resource<Room> resource = new Resource<Room>(room);
		resource.add(linkTo(methodOn(RoomController.class).getRoomById(room.getId())).withRel("self"));
		resource.add(linkTo(methodOn(RoomController.class).getAllRooms(null)).slash("?page=0&size=5").withRel("all-rooms"));
		resource.add(linkTo(methodOn(RoomController.class).getRoomReservationsForRoomWithId(room.getId())).withRel("room-reservations"));
		resource.add(linkTo(methodOn(RoomController.class).getHotelForRoomWithId(room.getId())).withRel("hotel"));
		resource.add(linkTo(methodOn(RoomController.class).getFloorForRoomWithId(room.getId())).withRel("floor"));
		resource.add(linkTo(methodOn(RoomController.class).getRoomTypeForRoomWithId(room.getId())).withRel("room-type"));
		return resource;
	}
	
	public static List<Resource<Room>> createRoomList(List<Room> rooms){
		List<Resource<Room>> roomList = new ArrayList<Resource<Room>>();
		for(Room item : rooms) {
			roomList.add(HATEOASImplementorHotel.createRoom(item));
		}
		return roomList;
	}
	
	public static Resource<RoomReservation> createRoomReservation(RoomReservation roomReservation){
		Resource<RoomReservation> resource = new Resource<RoomReservation>(roomReservation);
		resource.add(linkTo(methodOn(RoomReservationController.class).getRoomReservationById(roomReservation.getId())).withRel("self"));
		resource.add(linkTo(methodOn(RoomReservationController.class).getAllRoomReservations(null)).slash("?page=0&size=5").withRel("all-room-reservations"));
		resource.add(linkTo(methodOn(RoomReservationController.class).getExtraOptionsForRoomReservationWithId(roomReservation.getId())).withRel("room-reservations-extra-options"));
		resource.add(linkTo(methodOn(RoomReservationController.class).getRoomForRoomReservationWithId(roomReservation.getId())).withRel("room"));
		return resource;
	}
	
	public static List<Resource<RoomReservation>> createRoomReservationList(List<RoomReservation> roomReservations){
		List<Resource<RoomReservation>> roomReservationList = new ArrayList<Resource<RoomReservation>>();
		for(RoomReservation item : roomReservations) {
			roomReservationList.add(HATEOASImplementorHotel.createRoomReservation(item));
		}
		return roomReservationList;
	}
	
	public static Resource<RoomType> createRoomType(RoomType roomType){
		Resource<RoomType> resource = new Resource<RoomType>(roomType);
		resource.add(linkTo(methodOn(RoomTypeController.class).getRoomTypeById(roomType.getId())).withRel("self"));
		resource.add(linkTo(methodOn(RoomTypeController.class).getAllRoomTypes(null)).slash("?page=0&size=5").withRel("all-room-types"));
		resource.add(linkTo(methodOn(RoomTypeController.class).getCatalogueForRoomType(roomType.getId())).withRel("catalogue"));
		return resource;
	}
	
	public static List<Resource<RoomType>> createRoomTypeList(List<RoomType> roomTypes){
		List<Resource<RoomType>> roomTypeList = new ArrayList<Resource<RoomType>>();
		for(RoomType item : roomTypes) {
			roomTypeList.add(HATEOASImplementorHotel.createRoomType(item));
		}
		return roomTypeList;
	}
	
}
