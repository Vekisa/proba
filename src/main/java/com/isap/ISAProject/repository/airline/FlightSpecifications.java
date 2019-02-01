package com.isap.ISAProject.repository.airline;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.SeatState;

public class FlightSpecifications {

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
