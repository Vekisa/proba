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
	
	void deleteSeat(FlightSeat seat);
	
	FlightSeat setLuggageInfoForSeat(FlightSeat seat, Long luggageInfoId);
	
	LuggageInfo getLuggageInfoOfSeat(FlightSeat seat);
	
	FlightSeat setPassengerToSeat(FlightSeat seat, Long passengerId);
	
	Passenger getPassengerOfSeat(FlightSeat seat);
	
	FlightSeatCategory getCategoryOfSeat(FlightSeat seat);
	
	Flight getFlightOfSeat(FlightSeat seat);
	
	Ticket getTicketOfSeat(FlightSeat seat);
	
}
