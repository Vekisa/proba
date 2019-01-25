package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;

public interface PassengerServiceInterface {

	List<Passenger> findAll(Pageable pageable);
	
	Passenger findById(Long id);
	
	void deletePassenger(Passenger passenger);
	
	Passenger updatePassenger(Passenger oldPassenger, Passenger newPassenger);
	
	List<FlightSeat> getSeatsWithPassenger(Passenger passenger);
	
	List<Flight> getFlightsWithPassenger(Passenger passenger);
	
	List<Ticket> getTicketsOfPassenger(Passenger passenger);
	 
}
