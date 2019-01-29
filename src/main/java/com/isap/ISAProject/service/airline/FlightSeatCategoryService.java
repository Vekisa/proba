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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.repository.airline.FlightSeatCategoryRepository;
import com.isap.ISAProject.serviceInterface.airline.FlightSeatCategoryServiceInterface;

@Service
public class FlightSeatCategoryService implements FlightSeatCategoryServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FlightSeatCategoryRepository repository;	
	
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<FlightSeatCategory> findAll(Pageable pageable) {
		logger.info("> fetch categories at page {} with page size {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<FlightSeatCategory> categories = repository.findAll(pageable);
		logger.info("< categories fetched");
		return categories.getContent();
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public FlightSeatCategory findById(Long id) {
		logger.info("> fetch category with id {}", id);
		Optional<FlightSeatCategory> category = repository.findById(id);
		logger.info("< category fetched");
		if(category.isPresent()) return category.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested category doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public FlightSeatCategory updateFlightSeatCategory(Long oldCategoryId, FlightSeatCategory newCategory) {
		logger.info("> updating category with id {}", oldCategoryId);
		FlightSeatCategory oldCategory = this.findById(oldCategoryId);
		oldCategory.setPrice(newCategory.getPrice());
		oldCategory.setName(newCategory.getName());
		repository.save(oldCategory);
		logger.info("< category updated");
		return oldCategory;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void deleteFlightSeatCategory(Long categoryId) {
		logger.info("> deleting category with id {}", categoryId);
		repository.deleteById(categoryId);
		logger.info("< category deleted");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<FlightSeat> getSeatsInCategory(Long categoryId) {
		logger.info("> fetching seats for category with id {}", categoryId);
		FlightSeatCategory category = this.findById(categoryId);
		List<FlightSeat> list = category.getSeats();
		logger.info("< seats fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested seats do not exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<FlightSegment> getSegmentsOfCategory(Long categoryId) {
		logger.info("> fetching segments for category with id {}", categoryId);
		FlightSeatCategory category = this.findById(categoryId);
		List<FlightSegment> list = category.getSegments();
		logger.info("< segments fetched");
		if(!list.isEmpty()) return list;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested segments do not exist.");
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Airline getAirlineOfCategory(Long categoryId) {
		logger.info("> fetching airline for category with id {}", categoryId);
		FlightSeatCategory category = this.findById(categoryId);
		Airline airline = category.getAirline();
		logger.info("< airline fetched");
		if(airline != null) return airline;
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Requested airline does not exist.");
	}

}
