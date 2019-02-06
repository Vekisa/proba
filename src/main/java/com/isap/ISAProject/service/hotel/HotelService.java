package com.isap.ISAProject.service.hotel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.hotel.RoomType;
import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.repository.airline.LocationRepository;
import com.isap.ISAProject.repository.hotel.CatalogueRepository;
import com.isap.ISAProject.repository.hotel.FloorRepository;
import com.isap.ISAProject.repository.hotel.HotelRepository;
import com.isap.ISAProject.repository.hotel.HotelSpecifications;
import com.isap.ISAProject.repository.hotel.RoomRepository;
import com.isap.ISAProject.repository.hotel.RoomReservationRepository;
import com.isap.ISAProject.repository.hotel.RoomReservationSpecifications;

import hotelInterf.HotelServiceInterface;

@Service
@Transactional(readOnly = true)
public class HotelService implements HotelServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private LocationRepository destinationRepository;
	
	@Autowired
	private CatalogueRepository catalogueRepository;
	
	@Autowired
	private FloorRepository floorRepository;
	
	@Autowired
	private RoomReservationRepository rrRepo;
	
	@Autowired
	private RoomRepository roomRepo;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Hotel findById(long id) {
			logger.info("> Hotel findById id:{}", id);
			Optional<Hotel> hotel = hotelRepository.findById(id);
			logger.info("< Hotel findById id:{}", id);
			if(hotel.isPresent())
				return hotel.get();
			else 
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel sa zadatim id-em ne postoji");
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Hotel> findAll(Pageable pageable) {
		logger.info("> Hotel findAll");
		Page<Hotel> hotels = hotelRepository.findAll(pageable);
		logger.info("< Hotel findAll");
		if(!hotels.isEmpty())
			return hotels.getContent();
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hoteli ne postoje");
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Hotel saveWithLocation(Hotel hotel, Long id) {
		logger.info("> Hotel create");
		Location location = this.findDestination(id);
		hotel.setLocation(location);
		hotel.setRating(0);
		this.save(hotel);
		logger.info("< Hotel create");
		return hotel;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Hotel save(Hotel hotel) {
		logger.info("> saving hotel");
		hotelRepository.save(hotel);
		logger.info("< hotel saved");
		return hotel;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void deleteById(long id) {
		logger.info("> Hotel delete");
		this.findById(id);
		hotelRepository.deleteById(id);
		logger.info("< Hotel delete");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
	public Hotel updateHotelById(Long hotelId, Hotel newHotel) {
		logger.info("> Hotel update");
		Hotel oldHotel = this.findById(hotelId);
		oldHotel.setAddress(newHotel.getAddress());
		oldHotel.setDescription(newHotel.getDescription());
		oldHotel.setName(newHotel.getName());
		this.save(oldHotel);
		logger.info("< Hotel update");
		return oldHotel;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<Floor> getFloors(Long id){
		logger.info("> get floors for hotel");
		Hotel hotel = this.findById(id);
		List<Floor> floorList = hotel.getFloors();
		logger.info("< get floors for hotel");
		if(!floorList.isEmpty())
			return floorList ;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Spratovi za dati hotel ne postoje");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public Floor createFloor(Long hotelId, Floor floor){
		logger.info("> create floor for hotel");
		Hotel hotel = this.findById(hotelId);
		floor.setHotel(hotel);
		floorRepository.save(floor);
		logger.info("< create floor for hotel");
		return floor;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public List<ExtraOption> getExtraOptions(Long id){
		logger.info("> get extra-options for hotel");
		Hotel hotel = this.findById(id);
		List<ExtraOption> extraOptionList = hotel.getExtraOptions();
		logger.info("< get extra-options for hotel");
		if(!extraOptionList.isEmpty())
			return extraOptionList ;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Extra-optioni za dati hotel ne postoje");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public ExtraOption createExtraOption(Long hotelId, ExtraOption extraOption){
		logger.info("> create extra-option for hotel");
		Hotel hotel = this.findById(hotelId);
		hotel.getExtraOptions().add(extraOption);
		extraOption.setHotel(hotel);
		this.save(hotel);
		logger.info("< create extra-option for hotel");
		return extraOption;
	}
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public Catalogue getCatalogue(Long id){
		logger.info("> get catalogue for hotel");
		Hotel hotel = this.findById(id);
		Catalogue catalogue = hotel.getCatalogue();
		logger.info("< get catalogue for hotel");
		if(catalogue != null)
			return catalogue;
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cenovnik za dati hotel ne postoji");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public Catalogue createCatalogue(Long hotelId, Long catalogueId){
		logger.info("> create catalogue for hotel");
		Hotel hotel = this.findById(hotelId);
		Catalogue catalogue = this.findCatalogue(catalogueId);
		hotel.setCatalogue(catalogue);
		this.save(hotel);
		logger.info("< create catalogue for hotel");
		return catalogue;
	}
	
	@Override
	public Catalogue findCatalogue(Long catalogueId) {
		logger.info("fetching catalogue with id {}", catalogueId);
		Optional<Catalogue> catalogue = catalogueRepository.findById(catalogueId);
		logger.info("< catalogue fetched");
		if(catalogue.isPresent()) return catalogue.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested catalogue doesn't exist.");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public Hotel changeLocationOfHotel(Long hotelId, Long destinationId) {
		logger.info("> changing location of hotel with id {}", hotelId);
		Hotel hotel = this.findById(hotelId);
		Location destination = this.findDestination(destinationId);
		destination.getHotels().add(hotel);
		hotel.setLocation(destination);
		destinationRepository.save(destination);
		logger.info("< location changed");
		return hotel;
	}
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Location findDestination(Long id) {
		logger.info("> fetching destination with id {}", id);
		Optional<Location> destination = destinationRepository.findById(id);
		logger.info("< destination fetched");
		if(destination.isPresent()) return destination.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested destination doesn't exist.");
	}
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<CompanyAdmin> getAdminsOfHotel(Long id) {
		logger.info("> fetching admins of hotel with id {}", id);
		Hotel hotel = this.findById(id);
		List<CompanyAdmin> list = hotel.getAdmins();
		logger.info("< admins fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested admins do not exist.");
	}
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Location getLocationOfHotel(Long id) {
		logger.info("> fetching location of hotel with id {}", id);
		Hotel hotel = this.findById(id);
		Location location = hotel.getLocation();
		logger.info("< location fetched");
		if(location != null) return location;
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested destination doesn't exist.");
	}
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Map<Long, Double> getIncomeFor(Long id, Date beginDate, Date endDate) {
		logger.info("> calculating income");
		Hotel hotel = this.findById(id);
		Map<Long, Double> incomeMap = new HashMap<Long, Double>();
		for(Floor floor : hotel.getFloors())
			for(Room room : floor.getRooms())
				for(RoomReservation reservation : room.getRoomReservations())
					if(reservation.getBeginDate().after(beginDate) && reservation.getEndDate().before(endDate))
						if(incomeMap.containsKey(reservation.getBeginDate().getTime())) {
							incomeMap.put(reservation.getBeginDate().getTime(), incomeMap.get(reservation.getBeginDate().getTime()) + reservation.getPrice());
						} else {
							incomeMap.put(reservation.getBeginDate().getTime(), reservation.getPrice());
						}
		logger.info("< income calculated");
		return incomeMap;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Room> getRooms(Long id) {
		logger.info("> fetching rooms for hotel with id {}", id);
		Hotel hotel = this.findById(id);
		List<Room> rooms = new ArrayList<>();
		for(Floor f : hotel.getFloors())
			rooms.addAll(f.getRooms());
		logger.info("< rooms fetched");
		return rooms;
	}

	@Override
	public List<RoomType> getRoomTypes(Long id) {
		logger.info("> fetching room types for hotel with id {}", id);
		Hotel hotel = this.findById(id);
		logger.info("< room types fetched");
		return hotel.getCatalogue().getRoomTypes();
	}
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Hotel> search(Pageable pageable, String locationName, String hotelName, Date beginDate, Date endDate) {
		logger.info("> searching hotels");
		List<Hotel> hotels = hotelRepository.findAll(HotelSpecifications.findByHotelNameLocationName(hotelName, locationName));
		logger.info("hotels: " + hotels);
		List<RoomReservation> reservations = new ArrayList<RoomReservation>();
		for(Hotel h : hotels) {
			reservations.addAll(rrRepo.findAll(RoomReservationSpecifications.findByHotelBeginDate(h.getId(), beginDate)));
			reservations.addAll(rrRepo.findAll(RoomReservationSpecifications.findByHotelEndDate(h.getId(), endDate)));
		}
		logger.info("reservations: " + reservations);
		List<Hotel> ret = new ArrayList<Hotel>();
		for(RoomReservation rr : reservations) {
			if(!ret.contains(rr.getRoom().getFloor().getHotel())) {
				ret.add(rr.getRoom().getFloor().getHotel());
			}
		}
		
		for(Room r : roomRepo.findAll()) {
			if(r.getRoomReservations().isEmpty() && hotels.contains(r.getFloor().getHotel())) {
				if(!ret.contains(r.getFloor().getHotel())) {
					ret.add(r.getFloor().getHotel());
				}
			}
		}
		logger.info("ret" + ret);
		logger.info("< hotels found");
		return ret;
	}

	@Override
	public Map<Long, Integer> getStatisticFor(Long id, Date beginDate, Date endDate) {
		logger.info("> calculating statistic");
		Hotel hotel = this.findById(id);
		Map<Long, Integer> statisticMap = new HashMap<Long, Integer>();
		for(Floor floor : hotel.getFloors())
			for(Room room : floor.getRooms())
				for(RoomReservation reservation : room.getRoomReservations())
					if(reservation.getEndDate().after(beginDate) && reservation.getBeginDate().before(endDate)) {
						if(statisticMap.containsKey(reservation.getBeginDate().getTime())) {
							statisticMap.put(reservation.getBeginDate().getTime(), statisticMap.get(reservation.getBeginDate().getTime()) + reservation.getNumberOfGuests());
						} else {
							statisticMap.put(reservation.getBeginDate().getTime(), reservation.getNumberOfGuests());
						}
						if(statisticMap.containsKey(reservation.getEndDate().getTime())) {
							statisticMap.put(reservation.getEndDate().getTime(), statisticMap.get(reservation.getEndDate().getTime()) + reservation.getNumberOfGuests());
						} else {
							statisticMap.put(reservation.getEndDate().getTime(), reservation.getNumberOfGuests());
						}
					}
		logger.info("< statistic calculated");
		return statisticMap;
	}

	public List<RoomReservation> getQuickRoomReservations(Long id) {
		logger.info("> fetching quick room reservations");
		Hotel hotel = this.findById(id);
		Date time = new Date();
		List<RoomReservation> list = new ArrayList<>();
		for(Floor floor : hotel.getFloors())
			for(Room room : floor.getRooms())
				for(RoomReservation roomReservation : room.getRoomReservations())
					if(roomReservation.getBeginDate().after(time) && roomReservation.getReservation() == null)
						list.add(roomReservation);
		logger.info("< quick room reservations fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}
	
}
