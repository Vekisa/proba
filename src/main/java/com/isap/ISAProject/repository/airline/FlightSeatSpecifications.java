package com.isap.ISAProject.repository.airline;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.isap.ISAProject.model.airline.FlightSeat;

public class FlightSeatSpecifications {
	
	private static Logger logger = LoggerFactory.getLogger(FlightSeatSpecifications.class);
	
	public static Specification<FlightSeat> findByStateFlightLuggage(Long flightId, Double weight){
		return new Specification<FlightSeat>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<FlightSeat> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();
				if(flightId != null) {
					final Predicate flightPredicate = criteriaBuilder.equal(root.join("flight").get("id"), flightId);
					predicates.add(flightPredicate);
				}
				if(weight != null) {
					final Predicate luggagePredicate = criteriaBuilder.lessThan(root.join("luggageInfo").get("maxWeight"), weight);
					predicates.add(luggagePredicate);
				}
				final Predicate statePredicate = criteriaBuilder.equal(root.get("state"), 1);
				predicates.add(statePredicate);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
