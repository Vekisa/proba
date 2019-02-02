package com.isap.ISAProject.repository.hotel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.isap.ISAProject.model.hotel.RoomReservation;

public class RoomReservationSpecifications {
	
	public static Specification<RoomReservation> findByHotelBeginDate(Long hotelId, Date begin){
		return new Specification<RoomReservation>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<RoomReservation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<Predicate>();
				if(hotelId != null) {
					final Predicate hoteIdPredicate = criteriaBuilder.equal(root.join("room").join("floor").join("hotel").get("id"), hotelId);
					predicates.add(hoteIdPredicate);
				}
				if(begin != null) {
					final Predicate beginPredicate = criteriaBuilder.lessThan(root.get("beginDate"), begin);
					final Predicate endPredicate = criteriaBuilder.lessThan(root.get("endDate"), begin);
					predicates.add(beginPredicate);
					predicates.add(endPredicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	public static Specification<RoomReservation> findByHotelEndDate(Long hotelId, Date end){
		return new Specification<RoomReservation>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<RoomReservation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<Predicate>();
				if(hotelId != null) {
					final Predicate hoteIdPredicate = criteriaBuilder.equal(root.join("room").join("floor").join("hotel").get("id"), hotelId);
					predicates.add(hoteIdPredicate);
				}
				if(end != null) {
					final Predicate beginPredicate = criteriaBuilder.greaterThan(root.get("beginDate"), end);
					final Predicate endPredicate = criteriaBuilder.greaterThan(root.get("endDate"), end);
					predicates.add(beginPredicate);
					predicates.add(endPredicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
}
