package com.isap.ISAProject.controller.airline;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.repository.airline.AirlineRepository;
import com.isap.ISAProject.repository.airline.LuggageInfoRepository;

@RestController
@RequestMapping("/luggageInfos")
public class LuggageInfoController {

	@Autowired
	LuggageInfoRepository luggageInfoRepository;
	
	@Autowired
	AirlineRepository airlineRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<LuggageInfo>> getAllLuggageInfos() {
		List<LuggageInfo> luggageInfos = luggageInfoRepository.findAll();
		if(luggageInfos.isEmpty())
			return ResponseEntity.notFound().build();
		else
			return new ResponseEntity<List<LuggageInfo>>(luggageInfos, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<LuggageInfo> getLuggageInfoWithId(@PathVariable(value = "id") Long luggageInfoId) {
		try {
			Optional<LuggageInfo> luggageInfo = luggageInfoRepository.findById(luggageInfoId);
			if(luggageInfo.isPresent())
				return new ResponseEntity<LuggageInfo>(luggageInfo.get(), HttpStatus.OK);
			else
				return ResponseEntity.notFound().build();
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<LuggageInfo> updateLuggageInfoWithId(@PathVariable(value = "id") Long luggageInfoId,
															   @Valid @RequestBody LuggageInfo newLuggageInfo) {
		try {
			Optional<LuggageInfo> oldLuggageInfo = luggageInfoRepository.findById(luggageInfoId);
			if(oldLuggageInfo.isPresent()) {
				oldLuggageInfo.get().copyFieldsFrom(newLuggageInfo);
				return new ResponseEntity<LuggageInfo>(oldLuggageInfo.get(), HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLuggageInfoWithId(@PathVariable(value = "id") Long luggageInfoId) {
		try {
			luggageInfoRepository.deleteById(luggageInfoId);
			return ResponseEntity.ok().build();
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(value = "/airlines/{id}/luggageInfos", method = RequestMethod.GET)
	public ResponseEntity<List<LuggageInfo>> getLuggageInfosForAirlineWithId(@PathVariable(value = "id") Long airlineId) {
		try {
			Optional<Airline> airline = airlineRepository.findById(airlineId);
			if(airline.isPresent()) {
				if(airline.get().getLuggageInfos().isEmpty())
					return ResponseEntity.notFound().build();
				else
					return new ResponseEntity<List<LuggageInfo>>(airline.get().getLuggageInfos(), HttpStatus.OK);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
}
