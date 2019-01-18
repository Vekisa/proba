package com.isap.ISAProject.controller.airline;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	LuggageInfoRepository repository;

	@Autowired
	AirlineRepository airlineRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Resource<LuggageInfo>>> getAllLuggageInfos(Pageable pageable) {
		Page<LuggageInfo> luggageInfos = repository.findAll(pageable);
		if(luggageInfos.isEmpty())
			return ResponseEntity.notFound().build();
		else
			return new ResponseEntity<List<Resource<LuggageInfo>>>(HATEOASImplementor.createLuggageInfosList(luggageInfos.getContent()), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<LuggageInfo> getLuggageInfoWithId(@PathVariable(value = "id") Long luggageInfoId) {
		Optional<LuggageInfo> luggageInfo = repository.findById(luggageInfoId);
		if(luggageInfo.isPresent())
			return new ResponseEntity<LuggageInfo>(luggageInfo.get(), HttpStatus.OK);
		else
			return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<LuggageInfo> updateLuggageInfoWithId(@PathVariable(value = "id") Long luggageInfoId,
			@Valid @RequestBody LuggageInfo newLuggageInfo) {
		Optional<LuggageInfo> oldLuggageInfo = repository.findById(luggageInfoId);
		if(oldLuggageInfo.isPresent()) {
			oldLuggageInfo.get().copyFieldsFrom(newLuggageInfo);
			repository.save(oldLuggageInfo.get());
			return new ResponseEntity<LuggageInfo>(oldLuggageInfo.get(), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLuggageInfoWithId(@PathVariable(value = "id") Long luggageInfoId) {
		if(!repository.findById(luggageInfoId).isPresent()) return ResponseEntity.notFound().build();
		repository.deleteById(luggageInfoId);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}/airline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource<Airline>> getAirlineForLuggageInfoWithId(@PathVariable(value = "id") Long id) {
		Optional<LuggageInfo> luggageInfo = repository.findById(id);
		if(luggageInfo.isPresent()) {
			Airline airline = luggageInfo.get().getAirline();
			if(airline == null) {
				return ResponseEntity.noContent().build();
			} else {
				return new ResponseEntity<Resource<Airline>>(HATEOASImplementor.createAirline(airline), HttpStatus.OK);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
