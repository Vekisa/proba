package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.LuggageInfo;

public interface LuggageInfoServiceInterface {

	List<LuggageInfo> findAll(Pageable pageable);
	
	LuggageInfo findById(Long id);
	
	LuggageInfo updateLuggageInfo(Long oldLuggageInfoId, LuggageInfo newLuggageInfo);
	
	void deleteLuggageInfo(Long luggageInfoId);
	
	Airline getAirlineForLuggageInfo(Long luggageInfoId);
	
	List<FlightSeat> getSeatsUsingLuggageInfo(Long luggageInfoId);
	
}
