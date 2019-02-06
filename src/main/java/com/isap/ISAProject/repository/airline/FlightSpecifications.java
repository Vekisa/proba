package com.isap.ISAProject.repository.airline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.SeatState;
import com.isap.ISAProject.model.airline.TripType;

public class FlightSpecifications {

	public static Specification<Flight> findByStartDestFinishDestDepTimeArrTimeTripType(String startDest,
			String finishDest, Date depTime, Date arrTime, TripType tripType, String category,
			String airlineName){
		return new Specification<Flight>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Flight> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<Predicate>();
				if(startDest != null) {
					final Predicate startDestIdPredicate = criteriaBuilder.like(root.join("startDestination").get("name"), "%"+startDest+"%");
					predicates.add(startDestIdPredicate);
				}
				if(finishDest != null) {
					final Predicate finishDestPredicate = criteriaBuilder.like(root.join("finishDestination").get("name"), "%"+finishDest+"%");
					predicates.add(finishDestPredicate);
				}
				if(depTime != null) {
					final Predicate depTimePredicate = criteriaBuilder.greaterThan(root.get("departureTime"), depTime);
					predicates.add(depTimePredicate);
				}
				if(arrTime != null) {
					final Predicate arrTimePredicate = criteriaBuilder.lessThan(root.get("arrivalTime"), arrTime);
					predicates.add(arrTimePredicate);
				}
				if(tripType != null) {
					final Predicate tripTypePredicate = criteriaBuilder.equal(root.get("tripType"), tripType);
					predicates.add(tripTypePredicate);
				}
				if(category != null) {
					final Predicate categoryPredicate = criteriaBuilder.equal(root.join("category").get("name"), category);
					predicates.add(categoryPredicate);
				}
				if(airlineName != null) {
					final Predicate airlineNamePredicate = criteriaBuilder.like(root.join("airline").get("name"), "%"+airlineName+"%");
					predicates.add(airlineNamePredicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	public static Specification<Flight> findByIdPrice(Long id, Double priceBegin, Double priceEnd){
		return new Specification<Flight>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Flight> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<Predicate>();
				if(id != null) {
					final Predicate idPredicate = criteriaBuilder.equal(root.get("id"), id);
					predicates.add(idPredicate);
				}
				if(priceBegin != null) {
					final Predicate beginPred = criteriaBuilder.greaterThan(root.get("basePrice"), priceBegin);
					predicates.add(beginPred);
				}
				if(priceEnd != null) {
					final Predicate endPred = criteriaBuilder.lessThan(root.get("basePrice"), priceEnd);
					predicates.add(endPred);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	public static Specification<Flight> withStartingLocation(String name) {
		if (name == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.equal(root.join("startDestination").get("name"), name);
		}
	}

	public static Specification<Flight> withFinishLocation(String name) {
		if (name == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.equal(root.join("finishDestination").get("name"), name);
		}
	}

	public static Specification<Flight> withOutgoingFlightDate(Date date) {
		if(date == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("departure_time"), date);
		}
	}
	
	public static Specification<Flight> withReturningFlightDate(Date date) {
		if(date == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("departure_time"), date);
		}
	}
	
	public static Specification<Flight> withNumberOfTransfers(Integer number) {
		if(number == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.equal(root.get("number_of_transfers"), number);
		}
	}
	
	public static Specification<Flight> forNumberOfPassengers(Long number) {
		if(number == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.greaterThanOrEqualTo(cb.count(cb.equal(root.join("seats").get("state"), SeatState.FREE)), number);
		}
	}

}
