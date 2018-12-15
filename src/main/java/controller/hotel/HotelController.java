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
import com.isap.ISAProject.model.Hotel.Hotel;

import repository.hotel.HotelRepository;

@RestController
@RequestMapping("/hotels")
public class HotelController {
	@Autowired
	HotelRepository hotelRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Hotel> getAllHotels(){
		return hotelRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Hotel createHotel(@Valid @RequestBody Hotel hotel) {
		return hotelRepository.save(hotel);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public Hotel getHotelById(@PathVariable(value="id") Long hotelId) {
		return hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hotelId));
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteHotel(@PathVariable(value="id") Long hotelId){
		Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> 
			new ResourceNotFoundException("Hotel", "id", hotelId));
		
		hotelRepository.delete(hotel);
		
		return ResponseEntity.ok().build();
	}	
}
