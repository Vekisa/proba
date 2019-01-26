package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;

public interface FlightSeatServiceInterface {

	List<FlightSeat> findAll(Pageable pageable);
	
	FlightSeat findById(Long id);
	
	void deleteSeat(Long flightSeatId);
	
	FlightSeat setLuggageInfoForSeat(Long flightSeatId, Long luggageInfoId);
	
	LuggageInfo getLuggageInfoOfSeat(Long flightSeatId);
	
	FlightSeat setPassengerToSeat(Long flightSeatId, Long passengerId);
	
	Passenger getPassengerOfSeat(Long flightSeatId);
	
	FlightSeatCategory getCategoryOfSeat(Long flightSeatId);
	
	Flight getFlightOfSeat(Long flightSeatId);
	
	Ticket getTicketOfSeat(Long flightSeatId);
	
}
