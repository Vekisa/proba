package com.isap.ISAProject.serviceInterface.airline;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.LuggageInfo;

public interface LuggageInfoServiceInterface {

	List<LuggageInfo> findAll(Pageable pageable);
	
	LuggageInfo findById(Long id);
	
	LuggageInfo updateLuggageInfo(LuggageInfo oldLuggageInfo, LuggageInfo newLuggageInfo);
	
	void deleteLuggageInfo(LuggageInfo luggageInfo);
	
	Airline getAirlineForLuggageInfo(LuggageInfo luggageInfo);
	
	List<FlightSeat> getSeatsUsingLuggageInfo(LuggageInfo luggageInfo);
	
}
