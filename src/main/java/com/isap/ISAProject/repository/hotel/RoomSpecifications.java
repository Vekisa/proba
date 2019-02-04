package com.isap.ISAProject.repository.hotel;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.isap.ISAProject.model.hotel.Room;

public class RoomSpecifications {
	public static Specification<Room> findByHotelRoomType(Long hotelId, Long roomTypeId){
		return new Specification<Room>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Collection<Predicate> predicates = new ArrayList<>();
				if(hotelId != null) {
					final Predicate hotelIdPredicate = criteriaBuilder.equal(root.join("floor").join("hotel").get("id"), hotelId);
					predicates.add(hotelIdPredicate);
				}
				if(roomTypeId != null) {
					final Predicate roomTypeIdPredicate = criteriaBuilder.equal(root.join("roomType").get("id"), roomTypeId);
					predicates.add(roomTypeIdPredicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
