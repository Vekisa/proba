package com.isap.ISAProject.service.airline;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Coordinates;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.hotel.Floor;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.Room;
import com.isap.ISAProject.model.hotel.RoomReservation;
import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.rentacar.VehicleReservation;
import com.isap.ISAProject.repository.airline.LocationRepository;
import com.isap.ISAProject.repository.hotel.HotelRepository;
import com.isap.ISAProject.serviceInterface.airline.LocationServiceInterface;

@Service
public class LocationService implements LocationServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LocationRepository repository;
	
	@Autowired
	private HotelRepository hotelRepository;

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Location> findAll(Pageable pageable) {
		if(pageable != null) {
			logger.info("> fetch destinations at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
			Page<Location> destinations = repository.findAll(pageable);
			logger.info("< destinations fetched");
			if(destinations.hasContent()) return destinations.getContent();
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested locations do not exist.");
		} else {
			logger.info("> fetch all destinations");
			List<Location> destinations = repository.findAll();
			logger.info("< destinations fetched");
			if(!destinations.isEmpty()) return destinations;
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested locations do not exist.");
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Location findById(Long id) {
		logger.info("> fetch destination with id {}", id);
		Optional<Location> destination = repository.findById(id);
		logger.info("< destination fetched");
		if(destination.isPresent()) return destination.get();
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested location doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Location saveDestination(Location destination) {
		logger.info("> saving destination");
		repository.save(destination);
		logger.info("< destination saved");
		return destination;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Location updateDestination(Long oldDestinationId, Location newDestination) {
		logger.info("> updating destination with id {}", oldDestinationId);
		Location oldDestination = this.findById(oldDestinationId);
		oldDestination.setName(newDestination.getName());
		repository.save(oldDestination);
		logger.info("< destination updated");
		return oldDestination;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void deleteDestination(Long destinationId) {
		logger.info("> deleting destination with id {}", destinationId);
		repository.deleteById(destinationId);
		repository.removeSelfFromAirlines(destinationId);
		repository.removeSelfFromBranchOffices(destinationId);
		repository.removeSelfFromHotels(destinationId);
		logger.info("< destination deleted");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Flight addFlightToDestination(Flight flight, Long destinationId) {
		logger.info("> adding finish destination to flight with id {}", flight.getId());
		Location destination = this.findById(destinationId);
		destination.getFlightsFromHere().add(flight);
		flight.setStartDestination(destination);
		repository.save(destination);
		logger.info("< finish destination added");
		return flight;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Flight> getFlightsFromDestination(Long destinationId) {
		logger.info("> fetching flights from destination with id {}", destinationId);
		Location destination = this.findById(destinationId);
		List<Flight> list = destination.getFlightsFromHere();
		logger.info("< flights from destination fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flights do not exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Flight> getFlightsToDestination(Long destinationId) {
		logger.info("> fetching flights to destination with id {}", destinationId);
		Location destination = this.findById(destinationId);
		List<Flight> list = destination.getFlightsToHere();
		logger.info("< flights to destination fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flights do not exist.");
	}
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Coordinates getCoordinatesForCity(Long id) {
		Location location = this.findById(id);
		String city = location.getName();
		try {
			logger.info("> fetching coordinates of city {}", city);
			city = city.replace(" ", "%20");
			URL url = new URL("https://nominatim.openstreetmap.org/search?city=" + city + "&format=json");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			if(conn.getResponseCode() != 200) throw new Exception();
			logger.info("< coordinates fetched");
			Scanner sc = new Scanner(url.openStream());
			String currentLine;
			Coordinates coordinates = new Coordinates();
			logger.info("> parsing data");
			while(sc.hasNext()) {
				currentLine = sc.nextLine();
				String[] data = currentLine.split(",");
				for(String s : data) {
					if(s.contains("\"lon\"")) {
						String tmp = s.split(":")[1];
						coordinates.setLon(Double.parseDouble(tmp.substring(1, tmp.length()-1)));
					}
					if(s.contains("\"lat\"")) {
						String tmp = s.split(":")[1];
						coordinates.setLat(Double.parseDouble(tmp.substring(1, tmp.length()-1)));
					}
					if(coordinates.hasValues()) break;
				}
			}
			logger.info("< data parsed");
			sc.close();
			return coordinates;
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There was an error retrieving coordinates.");
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Airline> getAirlinesOnLocation(Long id) {
		logger.info("> fetching airlines on location with id {}", id);
		Location destination = this.findById(id);
		List<Airline> airlines = destination.getAirlines();
		logger.info("< airlines fetched");
		if(!airlines.isEmpty()) return airlines;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested airlines do not exist.");
	}
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<BranchOffice> getBranchOfficesOnLocation(Long id) {
		logger.info("> fetching offices on location with id {}", id);
		Location destination = this.findById(id);
		List<BranchOffice> offices = destination.getBranchOffices();
		logger.info("< offices fetched");
		if(!offices.isEmpty()) return offices;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested offices do not exist.");
	}
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<Hotel> getHotelsOnLocation(Long id) {
		logger.info("> fetching hotels on location with id {}", id);
		Location destination = this.findById(id);
		List<Hotel> hotels = destination.getHotels();
		logger.info("< hotels fetched");
		if(!hotels.isEmpty()) return hotels;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested hotels do not exist.");
	}
	
	public List<RoomReservation> getQuickRoomReservations(Long id) {
		logger.info("> fetching quick room reservations");
		List<Hotel> hotels = this.getHotelsOnLocation(id);
		System.out.println("hoteli: " + hotels.size());
		Date time = new Date();
		List<RoomReservation> list = new ArrayList<>();
		
		for(Hotel hotel : hotels) {	
			for(Floor floor : hotel.getFloors())
				for(Room room : floor.getRooms())
					for(RoomReservation roomReservation : room.getRoomReservations())
						if(roomReservation.getBeginDate().after(time) && roomReservation.getReservation() == null)
							list.add(roomReservation);
		}
		logger.info("< quick room reservations fetched");
		System.out.println("rezervacije: " + list.size());
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}
	
	public List<VehicleReservation> getQuickVehicleReservations(Long id){
		logger.info("> fetching quick vehicle reservations");
		List<BranchOffice> branchOffices = this.getBranchOfficesOnLocation(id);
		Date time = new Date();
		List<VehicleReservation> list = new ArrayList<>();
		for(BranchOffice branchOffice : branchOffices)
			for(Vehicle vehicle : branchOffice.getVehicles())
				for(VehicleReservation vehicleReservation : vehicle.getVehicleReservations())
					if(vehicleReservation.getBeginDate().after(time) && vehicleReservation.getReservation() == null)
						list.add(vehicleReservation);
		logger.info("< quick vehicle reservations fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested vehicles do not exist.");
	}

}
