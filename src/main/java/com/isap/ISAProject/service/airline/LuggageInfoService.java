package com.isap.ISAProject.service.airline;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.repository.airline.LuggageInfoRepository;
import com.isap.ISAProject.serviceInterface.airline.LuggageInfoServiceInterface;

@Service
public class LuggageInfoService implements LuggageInfoServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LuggageInfoRepository repository;
	
	@Override
	public List<LuggageInfo> findAll(Pageable pageable) {
		logger.info("> fetch luggage infos at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<LuggageInfo> luggageInfos = repository.findAll(pageable);
		logger.info("< luggage infos fetched");
		return luggageInfos.getContent();
	}

	@Override
	public LuggageInfo findById(Long id) {
		logger.info("> fetch luggage info with id {}", id);
		Optional<LuggageInfo> luggageInfo = repository.findById(id);
		logger.info("< luggage info fetched");
		if(luggageInfo.isPresent()) return luggageInfo.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested luggage info doesn't exist.");
	}

	@Override
	public LuggageInfo updateLuggageInfo(LuggageInfo oldLuggageInfo, LuggageInfo newLuggageInfo) {
		logger.info("> updating luggage info with id {}", oldLuggageInfo.getId());
		oldLuggageInfo.setMinWeight(newLuggageInfo.getMinWeight());
		oldLuggageInfo.setMaxWeight(newLuggageInfo.getMaxWeight());
		oldLuggageInfo.setPrice(newLuggageInfo.getPrice());
		// TODO : Azuriraj cenu karata
		repository.save(oldLuggageInfo);
		logger.info("< luggage info updated");
		return oldLuggageInfo;
	}

	@Override
	public void deleteLuggageInfo(LuggageInfo luggageInfo) {
		logger.info("> deleting luggage info with id {}", luggageInfo.getId());
		repository.delete(luggageInfo);
		logger.info("< luggage info deleted");
	}

	@Override
	public Airline getAirlineForLuggageInfo(LuggageInfo luggageInfo) {
		logger.info("> fetching airline for luggage info with id {}", luggageInfo.getId());
		Airline airline = luggageInfo.getAirline();
		logger.info("< airline fetched");
		if(airline != null) return airline;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested airline does not exist.");
	}

	@Override
	public List<FlightSeat> getSeatsUsingLuggageInfo(LuggageInfo luggageInfo) {
		logger.info("> fetching luggage infos for airline with id {}", luggageInfo.getId());
		List<FlightSeat> list = luggageInfo.getSeats();
		logger.info("< luggage infos fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}

}
