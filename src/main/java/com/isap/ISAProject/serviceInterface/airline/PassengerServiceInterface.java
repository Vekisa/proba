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
	
	void deletePassenger(Long passengerId);
	
	Passenger updatePassenger(Long oldPassengerId, Passenger newPassenger);
	
	List<FlightSeat> getSeatsWithPassenger(Long passengerId);
	
	List<Flight> getFlightsWithPassenger(Long passengerId);
	
	List<Ticket> getTicketsOfPassenger(Long passengerId);
	 
}
