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

import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.repository.airline.FlightSegmentRepository;
import com.isap.ISAProject.serviceInterface.airline.FlightSegmentServiceInterface;

@Service
public class FlightSegmentService implements FlightSegmentServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FlightSegmentRepository repository;
	
	@Override
	public List<FlightSegment> findAll(Pageable pageable) {
		logger.info("> fetch segments at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<FlightSegment> segments = repository.findAll(pageable);
		logger.info("< segments fetched");
		return segments.getContent();
	}

	@Override
	public FlightSegment findById(Long id) {
		logger.info("> fetch segment with id {}", id);
		Optional<FlightSegment> segment = repository.findById(id);
		logger.info("< segment fetched");
		if(segment.isPresent()) return segment.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested segment doesn't exist.");
	}

	@Override
	public FlightSegment updateSegment(FlightSegment oldSegment, FlightSegment newSegment) {
		logger.info("> updating segment with id {}", oldSegment.getId());
		checkForOverlapping(oldSegment.getConfiguration(), newSegment);
		oldSegment.setStartRow(newSegment.getStartRow());
		oldSegment.setEndRow(newSegment.getEndRow());
		oldSegment.setColumns(newSegment.getColumns());
		repository.save(oldSegment);
		logger.info("< segment updated");
		return oldSegment;
	}

	@Override
	public void deleteSegment(FlightSegment segment) {
		logger.info("> deleting segment with id {}", segment.getId());
		repository.delete(segment);
		logger.info("< segment deleted");
	}

	@Override
	public FlightConfiguration getConfigurationForSegment(FlightSegment segment) {
		logger.info("> fetching configuration for segment with id {}", segment.getId());
		FlightConfiguration configuration = segment.getConfiguration();
		logger.info("< configuration fetched");
		if(configuration != null) return configuration;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested configuration does not exist.");
	}

	@Override
	public FlightSeatCategory getCategoryOfSegment(FlightSegment segment) {
		logger.info("> fetching category for segment with id {}", segment.getId());
		FlightSeatCategory category = segment.getCategory();
		logger.info("< category fetched");
		if(category != null) return category;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested category does not exist.");
	}
	
	private boolean segmentOverlapsWithAnother(FlightSegment segment1, FlightSegment segment2) {
		if((segment1.getStartRow() >= segment2.getStartRow()) && segment1.getStartRow() <= segment2.getEndRow()) return true;
		if((segment1.getEndRow() >= segment2.getStartRow()) && (segment1.getStartRow() <= segment2.getEndRow())) return true;
		if((segment1.getStartRow() <= segment2.getStartRow()) && (segment1.getEndRow() >= segment2.getEndRow())) return true;
		return false;
	}
	
	private void checkForOverlapping(FlightConfiguration configuration, FlightSegment segment) {
		for(FlightSegment fs : configuration.getSegments())
			if(segmentOverlapsWithAnother(fs, segment)) 
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight segment overlaps with existing flight segments.");
	}

}
