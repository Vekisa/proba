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

import com.isap.ISAProject.exception.ResourceNotFoundException;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.repository.airline.FlightConfigurationRepository;
import com.isap.ISAProject.serviceInterface.airline.FlightConfigurationInterface;

@Service
public class FlightConfigurationService implements FlightConfigurationInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	FlightConfigurationRepository repository;

	@Override
	public List<FlightConfiguration> findAll(Pageable pageable) {
		logger.info("> fetch configurations at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<FlightConfiguration> configurations = repository.findAll(pageable);
		logger.info("< configurations fetched");
		return configurations.getContent();
	}

	@Override
	public FlightConfiguration findById(Long id) {
		logger.info("> fetch configuration with id {}", id);
		Optional<FlightConfiguration> configuration = repository.findById(id);
		logger.info("< configuration fetched");
		if(configuration.isPresent()) 
			return configuration.get();
		else
			throw new ResourceNotFoundException("Configuration with ID : " + id + " doesn't exist.");
	}

	@Override
	public void deleteConfiguration(FlightConfiguration configuration) {
		logger.info("> deleting configuration with id {}", configuration.getId());
		repository.delete(configuration);
		logger.info("< configuration deleted");
	}

	@Override
	public List<FlightSegment> getSegmentsForConfiguration(FlightConfiguration configuration) {
		logger.info("> fetching flight segments for flight configuration with id {}", configuration.getId());
		List<FlightSegment> list = configuration.getSegments();
		logger.info("< flight segments fetched");
		if(!list.isEmpty())
			return list;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested flights segments do not exist.");
	}

	@Override
	public FlightSegment createSegmentForConfiguration(FlightSegment segment, FlightConfiguration configuration) {
		logger.info("> adding flight segment to flight configuration with id {}", configuration.getId());
		for(FlightSegment fs : configuration.getSegments())
			if(segment.overlapsWith(fs)) 
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight segment overlaps with existing flight segments.");
		configuration.getSegments().add(segment);
		segment.setConfiguration(configuration);
		repository.save(configuration);
		logger.info("< flight segment added");
		return segment;
	}

	@Override
	public Airline getAirlineForConfiguration(FlightConfiguration configuration) {
		logger.info("> fetching airline for configuration with id {}", configuration.getId());
		Airline airline = configuration.getAirline();
		logger.info("< airline fetched");
		if(airline != null)
			return airline;
		else
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested airline does not exist.");
	}

}
